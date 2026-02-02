package com.shuham.ganga.presentation.dashboard.tabs.cart

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.shuham.ganga.data.local.entity.CartEntity
import com.shuham.ganga.presentation.theme.GangaOrange
import ganga.composeapp.generated.resources.Res
import ganga.composeapp.generated.resources.ic_add
import ganga.composeapp.generated.resources.ic_arrow_back
import ganga.composeapp.generated.resources.ic_bag
import ganga.composeapp.generated.resources.ic_favorite
import ganga.composeapp.generated.resources.ic_notifications
import ganga.composeapp.generated.resources.ic_remove
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel


// --- ROOT COMPOSABLE (Logic) ---
@Composable
fun CartScreenRoot(
    viewModel: CartViewModel = koinViewModel(),
    onNavigateBack: () -> Unit = {},
    onNavigateToCheckout: () -> Unit = {}
) {
    val state by viewModel.state.collectAsState()

    CartScreen(
        state = state,
        onAction = { action ->
            when (action) {
                is CartAction.OnBackClick -> onNavigateBack()
                is CartAction.OnCheckoutClick -> onNavigateToCheckout()
                else -> viewModel.onAction(action)
            }
        }
    )
}

// --- UI COMPOSABLE (Stateless) ---

@Composable
fun CartScreen(
    state: CartState,
    onAction: (CartAction) -> Unit
) {
    Scaffold(
        containerColor = Color(0xFFF9F9F9),
        topBar = {
            CartTopBar(onBackClick = { onAction(CartAction.OnBackClick) })
        },
        bottomBar = {
            if (state.cartItems.isNotEmpty()) {
                CartBottomBar(
                    totalPrice = state.totalPrice,
                    onCheckout = { onAction(CartAction.OnCheckoutClick) }
                )
            }
        }
    ) { padding ->
        if (state.cartItems.isEmpty()) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("Your Cart is Empty", color = Color.Gray)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 16.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                state.groupedItems.forEach { (vendorId, items) ->
                    item {
                        VendorHeader(vendorName = "Vendor $vendorId")
                    }
                    items(items) { item ->
                        CartItemRow(
                            item = item,
                            onQuantityChange = { newQty ->
                                onAction(CartAction.OnUpdateQuantity(item.productId, newQty))
                            }
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun CartItemRow(
    item: CartEntity,
    onQuantityChange: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(12.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Image
        AsyncImage(
            model = item.imageUrl,
            contentDescription = null,
            modifier = Modifier
                .size(80.dp)
                .background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp))
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(12.dp))

        // Details
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.title,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "₹${item.price}",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = Color.Black
            )
        }

        // Actions
        Column(horizontalAlignment = Alignment.End) {
            Icon(
                painter = painterResource(Res.drawable.ic_favorite),
                contentDescription = "Wishlist",
                tint = Color.Gray,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            // Stepper
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.border(1.dp, Color(0xFFEEEEEE), RoundedCornerShape(8.dp)).padding(4.dp)
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_remove),
                    contentDescription = "Decrease",
                    modifier = Modifier.size(16.dp).clickable { onQuantityChange(item.quantity - 1) },
                    tint = Color.Gray
                )
                Text(
                    text = "${item.quantity}",
                    modifier = Modifier.padding(horizontal = 12.dp),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Icon(
                    painter = painterResource(Res.drawable.ic_add),
                    contentDescription = "Increase",
                    modifier = Modifier.size(16.dp).clickable { onQuantityChange(item.quantity + 1) },
                    tint = GangaOrange
                )
            }
        }
    }
}

@Composable
fun CartBottomBar(
    totalPrice: Double,
    onCheckout: () -> Unit
) {
    Surface(
        shadowElevation = 16.dp,
        color = Color.White,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .navigationBarsPadding(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text("Total", color = Color.Gray, fontSize = 12.sp)
                Text("₹$totalPrice", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.Black)
            }
            Button(
                onClick = onCheckout,
                colors = ButtonDefaults.buttonColors(containerColor = GangaOrange),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.height(48.dp)
            ) {
                Text("Checkout", fontWeight = FontWeight.Bold)
            }
        }
    }
}

// ... TopBar and VendorHeader helpers remain similar ...
@Composable
fun CartTopBar(onBackClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().background(Color.White).padding(16.dp).statusBarsPadding(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Icon(painterResource(Res.drawable.ic_arrow_back), "Back", modifier = Modifier.clickable { onBackClick() })
        Text("My Cart", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Icon(painterResource(Res.drawable.ic_notifications), "Notifications")
    }
}

@Composable
fun VendorHeader(vendorName: String) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 12.dp)) {
        Icon(painterResource(Res.drawable.ic_bag), null, tint = GangaOrange, modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text("Sold by ", color = Color.Gray, fontSize = 12.sp)
        Text(vendorName, fontWeight = FontWeight.Bold, fontSize = 12.sp)
    }
}