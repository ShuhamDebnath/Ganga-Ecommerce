package com.shuham.ganga.presentation.detail


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.shuham.ganga.presentation.theme.GangaOrange
import ganga.composeapp.generated.resources.Res
import ganga.composeapp.generated.resources.ic_arrow_back
import ganga.composeapp.generated.resources.ic_bag
import ganga.composeapp.generated.resources.ic_favorite
import ganga.composeapp.generated.resources.ic_share
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ProductDetailScreenRoot(
    productId: String,
    onNavigateBack: () -> Unit,
    viewModel: ProductDetailViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(productId) {
        viewModel.onAction(ProductDetailAction.LoadProduct(productId))
    }

    ProductDetailScreen(
        state = state,
        onAction = { action ->
            when (action) {
                ProductDetailAction.OnBackClick -> onNavigateBack()
                else -> viewModel.onAction(action)
            }
        }
    )
}

@Composable
fun ProductDetailScreen(
    state: ProductDetailState,
    onAction: (ProductDetailAction) -> Unit
) {
    Scaffold(
        containerColor = Color.White,
        bottomBar = {
            if (!state.isLoading && state.product != null) {
                ProductBottomBar(onAction)
            }
        }
    ) { padding ->
        if (state.isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = GangaOrange)
            }
        } else if (state.product != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    //.padding(padding)
                    .verticalScroll(rememberScrollState())
            ) {
                // --- 1. Top Bar ---
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { onAction(ProductDetailAction.OnBackClick) }) {
                        Icon(painterResource(Res.drawable.ic_arrow_back), "Back")
                    }
                    Text(
                        "Detail Product",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(onClick = { onAction(ProductDetailAction.OnShareClick) }) {
                        Icon(
                            painter = painterResource(Res.drawable.ic_share),
                            contentDescription = "Share"
                        )
                    }
                }

                // --- 2. Image Slider ---
                val pagerState = rememberPagerState(pageCount = { state.product.images.size })
                Box {
                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(350.dp)
                            .background(Color(0xFFF9F9F9))
                    ) { index ->
                        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                            AsyncImage(
                                model = state.product.images[index],
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize().padding(24.dp),
                                contentScale = ContentScale.Fit
                            )
                        }
                    }

                    // Page Indicator (1/3)
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(16.dp)
                            .background(Color.White.copy(alpha = 0.8f), CircleShape)
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "${pagerState.currentPage + 1}/${state.product.images.size}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                // Dots Indicator
                Row(
                    Modifier.fillMaxWidth().padding(top = 16.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    repeat(state.product.images.size) { iteration ->
                        val color = if (pagerState.currentPage == iteration) GangaOrange else Color.LightGray
                        val width = if (pagerState.currentPage == iteration) 24.dp else 8.dp
                        Box(
                            modifier = Modifier
                                .padding(4.dp)
                                .clip(CircleShape)
                                .background(color)
                                .size(width, 8.dp)
                        )
                    }
                }

                // --- 3. Product Info ---
                Column(modifier = Modifier.padding(24.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Text(
                            text = state.product.title,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.weight(1f),
                            lineHeight = 30.sp
                        )
                        IconButton(onClick = { onAction(ProductDetailAction.OnToggleWishlist) }) {
                            Icon(
                                painter = painterResource(Res.drawable.ic_favorite),
                                contentDescription = "Wishlist",
                                tint = Color.Gray
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "₹${state.product.price}",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = GangaOrange
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        if (state.product.discountPrice > 0) {
                            Box(
                                modifier = Modifier
                                    .background(Color(0xFFFFEBEE), RoundedCornerShape(4.dp))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    "10% off", // Static for now, calculate later
                                    color = GangaOrange,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    // Old Price
                    Text(
                        text = "₹${state.product.price + 2000}", // Dummy calc for visual
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray,
                        textDecoration = TextDecoration.LineThrough
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Text("Description Product", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = state.product.description,
                        fontSize = 14.sp,
                        color = Color.Gray,
                        lineHeight = 22.sp
                    )
                }
            }
        } else {
            // Error State
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(state.errorMessage ?: "Product not found")
            }
        }
    }
}

@Composable
fun ProductBottomBar(onAction: (ProductDetailAction) -> Unit) {
    Surface(
        shadowElevation = 16.dp,
        color = Color.White
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .navigationBarsPadding(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Add to Cart (Outlined)
            OutlinedButton(
                onClick = { onAction(ProductDetailAction.OnAddToCart) },
                modifier = Modifier.weight(1f).height(56.dp),
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, GangaOrange)
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_bag),
                    contentDescription = null,
                    tint = GangaOrange,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Add to Cart", color = GangaOrange, fontWeight = FontWeight.Bold)
            }

            // Checkout (Filled)
            Button(
                onClick = { onAction(ProductDetailAction.OnCheckout) },
                modifier = Modifier.weight(1f).height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = GangaOrange)
            ) {
                Text("Checkout", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }
}