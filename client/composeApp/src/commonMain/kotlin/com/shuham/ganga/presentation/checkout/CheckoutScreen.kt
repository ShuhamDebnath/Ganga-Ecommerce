package com.shuham.ganga.presentation.checkout

import androidx.compose.foundation.background
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
import com.shuham.ganga.presentation.theme.GangaOrange
import ganga.composeapp.generated.resources.Res
import ganga.composeapp.generated.resources.ic_arrow_back
import ganga.composeapp.generated.resources.ic_bag
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CheckoutScreenRoot(
    viewModel: CheckoutViewModel = koinViewModel(),
    onNavigateBack: () -> Unit,
    onOrderSuccess: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(state.isOrderPlaced) {
        if (state.isOrderPlaced) {
            onOrderSuccess()
        }
    }

    CheckoutScreen(
        state = state,
        onAction = { action ->
            when (action) {
                CheckoutAction.OnBackClick -> onNavigateBack()
                else -> viewModel.onAction(action)
            }
        }
    )
}

@Composable
fun CheckoutScreen(
    state: CheckoutState,
    onAction: (CheckoutAction) -> Unit
) {
    Scaffold(
        containerColor = Color(0xFFF9F9F9),
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(16.dp)
                    .statusBarsPadding(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_arrow_back),
                    contentDescription = "Back",
                    modifier = Modifier.clickable { onAction(CheckoutAction.OnBackClick) }
                )
                Text("Checkout", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Icon(
                    painter = painterResource(Res.drawable.ic_bag),
                    contentDescription = "Help",
                    modifier = Modifier.size(24.dp),
                    tint = Color.Gray
                )
            }
        },
        bottomBar = {
            Surface(shadowElevation = 16.dp, color = Color.White) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                        .navigationBarsPadding(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("Total Price", color = Color.Gray, fontSize = 12.sp)
                        Text(
                            "₹${state.totalAmount}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = Color.Black
                        )
                    }
                    Button(
                        onClick = { onAction(CheckoutAction.OnPlaceOrderClick) },
                        colors = ButtonDefaults.buttonColors(containerColor = GangaOrange),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.height(48.dp).width(160.dp),
                        enabled = !state.isLoading
                    ) {
                        if (state.isLoading) {
                            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                        } else {
                            Text("Place Order", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text("Shipping Address", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Spacer(modifier = Modifier.height(8.dp))

                // Address Card
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(modifier = Modifier.padding(16.dp)) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(Color(0xFFFFF0EB), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(Res.drawable.ic_bag), // Use location icon if avail
                                contentDescription = null,
                                tint = GangaOrange,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                                Text("Home", fontWeight = FontWeight.Bold)
                                Text("Edit", color = GangaOrange, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("John Doe", fontSize = 14.sp)
                            Text("+91 9876543210", fontSize = 14.sp, color = Color.Gray)
                            Text("123 Market Road, Indiranagar, Bangalore", fontSize = 14.sp, color = Color.Gray)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
                Text("Order Items", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Dynamic Order Items
            items(state.items) { item ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AsyncImage(
                            model = item.imageUrl,
                            contentDescription = null,
                            modifier = Modifier
                                .size(60.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.LightGray),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = item.title,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                "Qty: ${item.quantity} • ₹${item.price}",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        Text("₹${item.price * item.quantity}", fontWeight = FontWeight.Bold)
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
                Text("Payment Method", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Spacer(modifier = Modifier.height(8.dp))

                // Payment Methods
                PaymentOption(
                    method = PaymentMethod.UPI,
                    isSelected = state.selectedPaymentMethod == PaymentMethod.UPI,
                    onSelect = { onAction(CheckoutAction.OnPaymentMethodSelect(it)) }
                )
                Spacer(modifier = Modifier.height(8.dp))
                PaymentOption(
                    method = PaymentMethod.CARD,
                    isSelected = state.selectedPaymentMethod == PaymentMethod.CARD,
                    onSelect = { onAction(CheckoutAction.OnPaymentMethodSelect(it)) }
                )
                Spacer(modifier = Modifier.height(8.dp))
                PaymentOption(
                    method = PaymentMethod.COD,
                    isSelected = state.selectedPaymentMethod == PaymentMethod.COD,
                    onSelect = { onAction(CheckoutAction.OnPaymentMethodSelect(it)) }
                )

                Spacer(modifier = Modifier.height(24.dp))
                Text("Payment Summary", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Spacer(modifier = Modifier.height(8.dp))

                // Summary Card
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        SummaryRow("Subtotal", "₹${state.subtotal}")
                        SummaryRow("Shipping Fee", "₹${state.shippingFee}")
                        SummaryRow("Tax (GST)", "₹${state.tax}")
                        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Total Amount", fontWeight = FontWeight.Bold)
                            Text("₹${state.totalAmount}", fontWeight = FontWeight.Bold, color = GangaOrange)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PaymentOption(
    method: PaymentMethod,
    isSelected: Boolean,
    onSelect: (PaymentMethod) -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = if (isSelected) Color(0xFFFFF0EB) else Color.White),
        shape = RoundedCornerShape(12.dp),
        border = if (isSelected) androidx.compose.foundation.BorderStroke(1.dp, GangaOrange) else null,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelect(method) }
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_bag), // Placeholder payment icon
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = Color.Gray
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(method.title, fontWeight = FontWeight.Medium, modifier = Modifier.weight(1f))
            RadioButton(
                selected = isSelected,
                onClick = { onSelect(method) },
                colors = RadioButtonDefaults.colors(selectedColor = GangaOrange, unselectedColor = Color.LightGray)
            )
        }
    }
}

@Composable
fun SummaryRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, color = Color.Gray, fontSize = 14.sp)
        Text(value, fontWeight = FontWeight.Medium, fontSize = 14.sp)
    }
}