package com.shuham.ganga.presentation.dashboard.tabs.profile


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shuham.ganga.presentation.theme.GangaOrange
import ganga.composeapp.generated.resources.Res
import ganga.composeapp.generated.resources.ic_arrow_forward
import ganga.composeapp.generated.resources.ic_bag
import ganga.composeapp.generated.resources.ic_edit
import ganga.composeapp.generated.resources.ic_favorite
import ganga.composeapp.generated.resources.ic_help
import ganga.composeapp.generated.resources.ic_location_on
import ganga.composeapp.generated.resources.ic_lock
import ganga.composeapp.generated.resources.ic_logout
import ganga.composeapp.generated.resources.ic_notifications
import ganga.composeapp.generated.resources.ic_person
import ganga.composeapp.generated.resources.ic_settings
import ganga.composeapp.generated.resources.ic_store
import ganga.composeapp.generated.resources.ic_wallet
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ProfileScreenRoot(
    viewModel: ProfileViewModel = koinViewModel(),
    onNavigateToAuth: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    ProfileScreen(
        state = state,
        onAction = { action ->
            if (action is ProfileAction.OnLogoutClick) {
                viewModel.onAction(action)
                onNavigateToAuth()
            } else {
                viewModel.onAction(action)
            }
        }
    )
}

@Composable
fun ProfileScreen(
    state: ProfileState,
    onAction: (ProfileAction) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF9F9F9))
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp)
            .statusBarsPadding()
    ) {
        // --- Header ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Profile",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Row {
                IconButton(onClick = { /* Notification Action */ }) {
                    BadgedBox(badge = { Badge { Text("1") } }) {
                        Icon(
                            painter = painterResource(Res.drawable.ic_notifications),
                            contentDescription = "Notifications",
                            tint = Color.Black
                        )
                    }
                }
                IconButton(onClick = { /* Settings Action */ }) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_settings),
                        contentDescription = "Settings",
                        tint = Color.Black
                    )
                }
            }
        }

        // --- User Profile Card ---
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(contentAlignment = Alignment.BottomEnd) {
                // Avatar
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFFFA07A)), // Salmon/Placeholder color
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_person),
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(40.dp)
                    )
                }
                // Edit Icon
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(GangaOrange)
                        .clickable { onAction(ProfileAction.OnEditProfileClick) },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_edit),
                        contentDescription = "Edit Profile",
                        tint = Color.White,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(state.userName, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                Text(state.userEmail, color = Color.Gray, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(4.dp))
                // Gold Member Badge
                Surface(
                    color = Color(0xFFFFF3E0), // Light Gold
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = "Gold Member",
                        fontSize = 10.sp,
                        color = Color(0xFFF57C00), // Dark Gold
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- Switch to Vendor Banner ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFFFFF0EB),
                            Color(0xFFFFCCBC)
                        ) // Light Orange Gradient
                    )
                )
                .clickable { onAction(ProfileAction.OnSwitchToVendorClick) }
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Switch to Vendor",
                        color = GangaOrange,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Manage your store, products,\nand earnings directly.",
                        color = GangaOrange.copy(alpha = 0.8f),
                        fontSize = 12.sp,
                        lineHeight = 16.sp
                    )
                }
                // Vendor Icon Circle
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(GangaOrange, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_store),
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- Account Section ---
        Text("Account", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        Spacer(modifier = Modifier.height(12.dp))

        Card(
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column {
                ProfileOptionItem(
                    icon = Res.drawable.ic_bag,
                    title = "My Orders",
                    subtitle = "Check order status",
                    onClick = { onAction(ProfileAction.OnOrdersClick) }
                )
                HorizontalDivider(
                    color = Color(0xFFF0F0F0),
                    thickness = 1.dp,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                ProfileOptionItem(
                    icon = Res.drawable.ic_favorite,
                    title = "Wishlist",
                    subtitle = "Your favorite items",
                    onClick = { onAction(ProfileAction.OnWishlistClick) }
                )
                HorizontalDivider(
                    color = Color(0xFFF0F0F0),
                    thickness = 1.dp,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                ProfileOptionItem(
                    icon = Res.drawable.ic_location_on,
                    title = "Addresses",
                    subtitle = "Manage delivery locations",
                    onClick = { onAction(ProfileAction.OnAddressClick) }
                )
                HorizontalDivider(
                    color = Color(0xFFF0F0F0),
                    thickness = 1.dp,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                ProfileOptionItem(
                    icon = Res.drawable.ic_wallet,
                    title = "Payment Methods",
                    subtitle = "Cards & UPI",
                    onClick = { onAction(ProfileAction.OnPaymentMethodsClick) }
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- General Section ---
        Text("General", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        Spacer(modifier = Modifier.height(12.dp))

        Card(
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column {
                ProfileOptionItem(
                    icon = Res.drawable.ic_help,
                    title = "Help Center",
                    onClick = { onAction(ProfileAction.OnHelpClick) }
                )
                HorizontalDivider(
                    color = Color(0xFFF0F0F0),
                    thickness = 1.dp,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                ProfileOptionItem(
                    icon = Res.drawable.ic_lock,
                    title = "Privacy Policy",
                    onClick = { onAction(ProfileAction.OnPrivacyPolicyClick) }
                )
                HorizontalDivider(
                    color = Color(0xFFF0F0F0),
                    thickness = 1.dp,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                // Logout Item
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onAction(ProfileAction.OnLogoutClick) }
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(Color(0xFFFFEBEE), CircleShape), // Light Red
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(Res.drawable.ic_logout),
                            contentDescription = null,
                            tint = Color.Red,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = "Log Out",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Red
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Version Footer
        Text(
            text = state.appVersion,
            fontSize = 12.sp,
            color = Color.Gray,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(80.dp)) // Extra space for bottom nav
    }
}

@Composable
fun ProfileOptionItem(
    icon: DrawableResource,
    title: String,
    subtitle: String? = null,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(Color(0xFFF5F9FF), CircleShape), // Light Blue tint background for icons
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(icon),
                contentDescription = null,
                tint = Color(0xFF3D8FEF), // Icon Blue Color
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black
            )
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }

        Icon(
            painter = painterResource(Res.drawable.ic_arrow_forward),
            contentDescription = null,
            tint = Color.LightGray,
            modifier = Modifier.size(16.dp)
        )
    }
}