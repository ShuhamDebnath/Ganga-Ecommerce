package com.shuham.ganga.presentation.dashboard.tabs.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shuham.ganga.presentation.components.ProductCard
import com.shuham.ganga.presentation.theme.GangaOrange
import ganga.composeapp.generated.resources.Res
import ganga.composeapp.generated.resources.ic_favorite_fill
import ganga.composeapp.generated.resources.ic_location_on
import ganga.composeapp.generated.resources.ic_bag
import ganga.composeapp.generated.resources.ic_notifications
import ganga.composeapp.generated.resources.ic_search
import ganga.composeapp.generated.resources.ic_timer
import ganga.composeapp.generated.resources.ic_tune
import ganga.composeapp.generated.resources.personal_computer
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeScreenRoot(
    viewModel: HomeViewModel = koinViewModel(),
    onNavigateToProduct: (String) -> Unit
) {
    val state by viewModel.state.collectAsState()
    HomeScreen(state = state, onNavigateToProduct = onNavigateToProduct)
}

@Composable
fun HomeScreen(
    state: HomeState,
    onNavigateToProduct: (String) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxSize().background(Color(0xFFF9F9F9))
    ) {
        // --- 1. Top Bar & Location (Full Width) ---
        item(span = { GridItemSpan(2) }) {
            Column {
                HomeTopBar()
                Spacer(modifier = Modifier.height(16.dp))
                LocationBar()
                Spacer(modifier = Modifier.height(24.dp))
            }
        }

        // --- 2. Categories (Full Width) ---
        item(span = { GridItemSpan(2) }) {
            CategorySection()
        }

        // --- 3. Flash Sale Banner (Full Width) ---
        item(span = { GridItemSpan(2) }) {
            FlashSaleBanner()
        }

        // --- 4. Flash Sale List Header (Full Width) ---
        item(span = { GridItemSpan(2) }) {
            SectionHeader(
                title = "Flash Sale",
                endContent = {
                    // Timer Badge
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color(0xFFF44336)) // Red
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(painterResource(Res.drawable.ic_timer), null, tint = Color.White, modifier = Modifier.size(12.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Ends in 12 : 58 : 32", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }
            )
        }

        // --- 5. Flash Sale Items (Horizontal Scroll) ---
        item(span = { GridItemSpan(2) }) {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 16.dp) // Space for shadow
            ) {
                // Showing first 3 items as flash sale example
                items(state.products.take(3).size) { index ->
                    Box(modifier = Modifier.width(160.dp)) {
                        ProductCard(
                            product = state.products[index],
                            onClick = { onNavigateToProduct(state.products[index].id) }
                        )
                    }
                }
            }
        }

        // --- 6. Recommended Header (Full Width) ---
        item(span = { GridItemSpan(2) }) {
            SectionHeader(title = "Recommend for You")
        }

        // --- 7. Recommended Grid (The actual Grid) ---
        if (state.isLoading) {
            item(span = { GridItemSpan(2) }) {
                Box(modifier = Modifier.height(200.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = GangaOrange)
                }
            }
        } else if (state.products.isEmpty()) {
            item(span = { GridItemSpan(2) }) {
                Text("No products found.", modifier = Modifier.padding(16.dp), color = Color.Gray)
            }
        } else {
            // Display remaining products in the grid
            items(state.products) { product ->
                ProductCard(
                    product = product,
                    onClick = { onNavigateToProduct(product.id) }
                )
            }
        }
    }
}

// --- COMPONENTS ---

@Composable
fun HomeTopBar() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            // App Logo
            Icon(
                painter = painterResource(Res.drawable.ic_bag),
                contentDescription = null,
                tint = GangaOrange,
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Ganga", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        }

        Row {
            IconButton(
                onClick = {},
                modifier = Modifier.size(40.dp).background(Color.White, CircleShape)
            ) {
                Icon(painterResource(Res.drawable.ic_favorite_fill), null, tint = Color.Gray)
            }
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(
                onClick = {},
                modifier = Modifier.size(40.dp).background(Color.White, CircleShape)
            ) {
                Icon(painterResource(Res.drawable.ic_notifications), null, tint = Color.Gray)
            }
        }
    }
}

@Composable
fun LocationBar() {
    Column {
        // Search Bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.White)
                .padding(horizontal = 12.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(painterResource(Res.drawable.ic_search), null, tint = Color.Gray)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Find you needed...", color = Color.Gray, fontSize = 14.sp)
                Spacer(modifier = Modifier.weight(1f))
                Icon(painterResource(Res.drawable.ic_tune), null, tint = GangaOrange) // Filter icon
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Location Text
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(painterResource(Res.drawable.ic_location_on), null, tint = GangaOrange, modifier = Modifier.size(14.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "Deliver to: Jl. Rose No. 123 Block A, Cipete...",
                style = MaterialTheme.typography.labelSmall,
                color = Color(0xFF1A1A1A),
                maxLines = 1
            )
        }
    }
}

@Composable
fun CategorySection() {
    val categories = listOf(
        Triple("Electronic", painterResource(Res.drawable.personal_computer), Color(0xFFE3F2FD)), // Blue
        Triple("Food", painterResource(Res.drawable.personal_computer), Color(0xFFFFF3E0)), // Orange
        Triple("Accessory", painterResource(Res.drawable.personal_computer), Color(0xFFFFF8E1)), // Yellow
        Triple("Beauty", painterResource(Res.drawable.personal_computer), Color(0xFFFCE4EC)), // Pink
        Triple("Furniture", painterResource(Res.drawable.personal_computer), Color(0xFFEDE7F6)), // Purple
        Triple("Fashion", painterResource(Res.drawable.personal_computer), Color(0xFFE0F2F1)), // Teal
        Triple("Health", painterResource(Res.drawable.personal_computer), Color(0xFFFFEBEE)), // Red
        Triple("Stationery", painterResource(Res.drawable.personal_computer), Color(0xFFE1F5FE)) // Cyan

//        Triple("Electronic", Icons.Default.Computer, Color(0xFFE3F2FD)), // Blue
//        Triple("Food", Icons.Default.Fastfood, Color(0xFFFFF3E0)), // Orange
//        Triple("Accessory", Icons.Default.Watch, Color(0xFFFFF8E1)), // Yellow
//        Triple("Beauty", Icons.Default.Face, Color(0xFFFCE4EC)), // Pink
//        Triple("Furniture", Icons.Default.Chair, Color(0xFFEDE7F6)), // Purple
//        Triple("Fashion", Icons.Default.Checkroom, Color(0xFFE0F2F1)), // Teal
//        Triple("Health", Icons.Default.Favorite, Color(0xFFFFEBEE)), // Red
//        Triple("Stationery", Icons.Default.Edit, Color(0xFFE1F5FE)) // Cyan
    )

    Column {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            // First Row (4 items)
            categories.take(4).forEach { CategoryItem(it) }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            // Second Row (4 items)
            categories.drop(4).forEach { CategoryItem(it) }
        }
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun CategoryItem(data: Triple<String, Painter, Color>) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .background(data.third),
            contentAlignment = Alignment.Center
        ) {
            Icon(data.second, null, tint = Color.Black.copy(alpha = 0.7f), modifier = Modifier.size(24.dp))
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(data.first, fontSize = 10.sp, color = Color.Gray)
    }
}

@Composable
fun FlashSaleBanner() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A)) // Dark background
    ) {
        Row(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Box(
                    modifier = Modifier
                        .background(GangaOrange, RoundedCornerShape(4.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text("LIMITED TIME", color = Color.White, fontSize = 8.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text("6.6 Flash Sale", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Text("Cashback Up to 100%", color = Color.Gray, fontSize = 12.sp)
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = {},
                    colors = ButtonDefaults.buttonColors(containerColor = GangaOrange),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 0.dp),
                    modifier = Modifier.height(32.dp)
                ) {
                    Text("Shop Now", fontSize = 10.sp)
                }
            }
            // Optional: Add image on the right if assets available
        }
    }
    Spacer(modifier = Modifier.height(24.dp))
}

@Composable
fun SectionHeader(title: String, endContent: @Composable () -> Unit = { Text("See all", color = GangaOrange, fontSize = 12.sp, fontWeight = FontWeight.Bold) }) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(title, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        endContent()
    }
}