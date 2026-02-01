package com.shuham.ganga.presentation.dashboard.tabs.search

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shuham.ganga.presentation.components.ProductCard
import com.shuham.ganga.presentation.theme.GangaOrange
import ganga.composeapp.generated.resources.Res
import ganga.composeapp.generated.resources.ic_arrow_back
import ganga.composeapp.generated.resources.ic_bag
import ganga.composeapp.generated.resources.ic_search
import ganga.composeapp.generated.resources.ic_tune
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SearchScreenRoot(
    viewModel: SearchViewModel = koinViewModel(),
    onNavigateToProduct: (String) -> Unit = {},
    onNavigateBack: () -> Unit = {}
) {
    val state by viewModel.state.collectAsState()

    SearchScreen(
        state = state,
        onAction = { action ->
            when (action) {
                is SearchAction.OnBackClick -> onNavigateBack()
                else -> viewModel.onAction(action)
            }
        },
        onProductClick = onNavigateToProduct
    )
}

@Composable
fun SearchScreen(
    state: SearchState,
    onAction: (SearchAction) -> Unit,
    onProductClick: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        // --- 1. Header (Back + Search) ---
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(
                onClick = { onAction(SearchAction.OnBackClick) },
                modifier = Modifier
                    .size(48.dp)
                    .border(1.dp, Color(0xFFEEEEEE), RoundedCornerShape(12.dp))
            ) {
                Icon(painterResource(Res.drawable.ic_arrow_back), "Back")
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Search Field
            OutlinedTextField(
                value = state.query,
                onValueChange = { onAction(SearchAction.OnQueryChange(it)) },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Find you needed...", color = Color.Gray, fontSize = 14.sp) },
                leadingIcon = {
                    Icon(
                        painterResource(Res.drawable.ic_search),
                        null,
                        tint = Color.Gray
                    )
                },
                trailingIcon = {
                    Icon(
                        painterResource(Res.drawable.ic_tune),
                        null,
                        tint = Color.Black
                    )
                },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = GangaOrange,
                    unfocusedBorderColor = Color(0xFFEEEEEE)
                ),
                singleLine = true
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            // --- 2. Popular Search Section ---
            if (state.query.isEmpty()) {
                item(span = { GridItemSpan(2) }) {
                    Text("Popular Search", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }

                items(state.popularSearches.size) { index ->
                    // Since Grid is 2 columns, this automatically creates the 2x2 layout from screenshot
                    PopularSearchItem(
                        text = state.popularSearches[index],
                        onClick = { onAction(SearchAction.OnQueryChange(state.popularSearches[index])) }
                    )
                }

                item(span = { GridItemSpan(2) }) {
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            // --- 3. Results / Recommendations ---
            item(span = { GridItemSpan(2) }) {
                Text(
                    text = if (state.query.isEmpty()) "Recommend for You" else "Search Results",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Products
            val displayList =
                if (state.query.isNotEmpty()) state.searchResults else state.recommendedProducts

            if (state.isLoading) {
                item(span = { GridItemSpan(2) }) {
                    Box(
                        Modifier.fillMaxWidth().height(100.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = GangaOrange)
                    }
                }
            } else {
                items(displayList) { product ->
                    ProductCard(
                        product = product,
                        onClick = { onProductClick(product.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun PopularSearchItem(
    text: String,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .border(1.dp, Color(0xFFEEEEEE), RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Placeholder Image Box
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            // Placeholder icon, replace with actual product image later
            Icon(
                painterResource(Res.drawable.ic_bag),
                null,
                modifier = Modifier.size(20.dp),
                tint = Color.Gray
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Text(text, fontSize = 14.sp, fontWeight = FontWeight.Medium)
    }
}

@Preview
@Composable
fun SearchScreenPrev() {
    SearchScreen(
        state = SearchState(),
        onAction = {},
        onProductClick = {}
    )
}