package com.shuham.ganga.presentation.wishlist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shuham.ganga.data.remote.model.ProductDto
import com.shuham.ganga.presentation.components.ProductCard
import ganga.composeapp.generated.resources.Res
import ganga.composeapp.generated.resources.ic_arrow_back
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun WishlistScreenRoot(
    viewModel: WishlistViewModel = koinViewModel(),
    onNavigateBack: () -> Unit,
    onNavigateToProduct: (String) -> Unit // <-- Added Navigation Callback
) {
    val state by viewModel.state.collectAsState()

    WishlistScreen(
        state = state,
        onBackClick = onNavigateBack,
        onProductClick = onNavigateToProduct
    )
}

@Composable
fun WishlistScreen(
    state: WishlistState,
    onBackClick: () -> Unit,
    onProductClick: (String) -> Unit
) {
    Scaffold(
        containerColor = Color.White,
        topBar = {
            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp).statusBarsPadding(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_arrow_back),
                    contentDescription = "Back",
                    modifier = Modifier.clickable { onBackClick() }
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text("My Wishlist", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }
    ) { padding ->
        if (state.items.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Wishlist is empty", color = Color.Gray)
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(padding)
            ) {
                items(state.items) { product ->

                    ProductCard(
                        product = product,
                        onClick = { onProductClick(product.id) }
                    )
                }
            }
        }
    }
}