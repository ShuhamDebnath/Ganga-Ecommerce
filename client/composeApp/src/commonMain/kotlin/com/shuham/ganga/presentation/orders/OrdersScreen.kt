package com.shuham.ganga.presentation.orders

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shuham.ganga.data.remote.model.OrderDataDto
import com.shuham.ganga.presentation.theme.GangaOrange
import ganga.composeapp.generated.resources.Res
import ganga.composeapp.generated.resources.ic_arrow_back
import ganga.composeapp.generated.resources.ic_bag
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun OrdersScreenRoot(
    viewModel: OrdersViewModel = koinViewModel(),
    onNavigateBack: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    OrdersScreen(
        state = state,
        onAction = { action ->
            if (action is OrdersAction.OnBackClick) onNavigateBack()
            else viewModel.onAction(action)
        }
    )
}
@Composable
fun OrdersScreen(
    state: OrdersState,
    onAction: (OrdersAction) -> Unit
) {
    Scaffold(
        containerColor = Color(0xFFF9F9F9),
        topBar = {
            Row(
                modifier = Modifier.fillMaxWidth().background(Color.White).padding(16.dp).statusBarsPadding(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_arrow_back),
                    contentDescription = "Back",
                    modifier = Modifier.clickable { onAction(OrdersAction.OnBackClick) }
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text("My Orders", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }
    ) { padding ->
        if (state.isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator(color = GangaOrange) }
        } else if (state.orders.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text("No orders found", color = Color.Gray) }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(state.orders) { order ->
                    OrderCard(order, onCancel = { onAction(OrdersAction.OnCancelClick(order.id)) })
                }
            }
        }
    }
}

@Composable
fun OrderCard(order: OrderDataDto, onCancel: () -> Unit) {
    Card(colors = CardDefaults.cardColors(containerColor = Color.White), shape = RoundedCornerShape(12.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Order #${order.id.takeLast(6).uppercase()}", fontWeight = FontWeight.Bold)
                Text(
                    text = order.status,
                    color = when(order.status) {
                        "Cancelled" -> Color.Red
                        "Delivered" -> Color(0xFF4CAF50)
                        else -> Color(0xFFFFA000)
                    },
                    fontWeight = FontWeight.Medium, fontSize = 12.sp
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(40.dp).background(Color(0xFFF5F5F5), CircleShape), contentAlignment = Alignment.Center) {
                    Icon(painterResource(Res.drawable.ic_bag), null, tint = Color.Gray, modifier = Modifier.size(20.dp))
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text("Total Amount", fontSize = 12.sp, color = Color.Gray)
                    Text("₹${order.total_price}", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }
            if (order.status != "Cancelled" && order.status != "Delivered") {
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedButton(
                    onClick = onCancel,
                    modifier = Modifier.fillMaxWidth().height(36.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color.Red)
                ) {
                    Text("Cancel Order", fontSize = 12.sp)
                }
            }
        }
    }
}