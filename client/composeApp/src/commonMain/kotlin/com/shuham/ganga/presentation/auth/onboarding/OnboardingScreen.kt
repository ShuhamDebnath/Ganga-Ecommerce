package com.shuham.ganga.presentation.auth.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shuham.ganga.presentation.auth.login.LoginAction
import com.shuham.ganga.presentation.components.GangaButton
import com.shuham.ganga.presentation.theme.GangaOrange
import ganga.composeapp.generated.resources.Res
import ganga.composeapp.generated.resources.ic_arrow_back
import ganga.composeapp.generated.resources.ic_arrow_forward
import ganga.composeapp.generated.resources.ic_delivery_truck
import ganga.composeapp.generated.resources.ic_logo_bag
import ganga.composeapp.generated.resources.ic_security_check
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import kotlin.math.round

// --- ROOT COMPOSABLE (Logic) ---
@Composable
fun OnboardingScreenRoot(
    viewModel: OnboardingViewModel = koinViewModel(), // Inject ViewModel
    onFinished: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    OnboardingScreen(
        state = state,
        onAction = { action ->
            when (action) {
                OnboardingAction.OnSkipClick -> onFinished()
                OnboardingAction.OnGetStartedClick -> onFinished()
                else -> viewModel.onAction(action)
            }
        }
    )
}

// --- UI COMPOSABLE (Stateless) ---
@Composable
fun OnboardingScreen(
    state: OnboardingState,
    onAction: (OnboardingAction) -> Unit
) {
    val pages = listOf(
        OnboardingPage(
            title = "Fast Delivery",
            description = "Get your order delivered to your doorstep quickly with our super fast delivery partners.",
            //image = Res.drawable.img_onboarding_delivery,
            image = Res.drawable.ic_logo_bag,
            icon = Res.drawable.ic_delivery_truck
        ),
        OnboardingPage(
            title = "Wide Variety",
            description = "Explore thousands of products from multiple trusted vendors in one single place.",
            //image = Res.drawable.img_onboarding_variety,
            image = Res.drawable.ic_logo_bag,
            icon = Res.drawable.ic_logo_bag
        ),
        OnboardingPage(
            title = "Secure Payment",
            description = "Enjoy a safe and seamless checkout experience with our multi-layer security.",
            //image = Res.drawable.img_onboarding_secure,
            image = Res.drawable.ic_logo_bag,
            icon = Res.drawable.ic_security_check
        )
    )

    val pagerState = rememberPagerState(pageCount = { pages.size })

    // Sync ViewModel State -> Pager UI
    LaunchedEffect(state.currentPage) {
        if (pagerState.currentPage != state.currentPage) {
            pagerState.animateScrollToPage(state.currentPage)
        }
    }

    // Sync Pager UI -> ViewModel State (Swipe detection)
    LaunchedEffect(pagerState.currentPage) {
        onAction(OnboardingAction.OnPageChange(pagerState.currentPage))
    }

    Scaffold(
        containerColor = Color.White
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Pager Content
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.weight(1f)
            ) { pageIndex ->
                val page = pages[pageIndex]
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 40.dp)
                ) {
                    // Illustration Circle Background
                    Box(
                        modifier = Modifier
                            .size(280.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFF0F9F9)),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(page.image), // DIRECT USE (Correct)
                            contentDescription = null,
                            modifier = Modifier.size(200.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(48.dp))

                    // Small Icon Circle
                    Surface(
                        shape = CircleShape,
                        color = Color(0xFFFFF5F2),
                        modifier = Modifier.size(48.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                painter = painterResource(page.icon), // DIRECT USE (Correct)
                                contentDescription = null,
                                tint = GangaOrange,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = page.title,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = page.description,
                        fontSize = 16.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Center,
                        lineHeight = 24.sp
                    )
                }
            }

            // Bottom Navigation Area
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 48.dp, start = 24.dp, end = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Page Indicator
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    repeat(pages.size) { index ->
                        val isSelected = state.currentPage == index
                        Box(
                            modifier = Modifier
                                .height(6.dp)
                                .width(if (isSelected) 24.dp else 6.dp)
                                .clip(CircleShape)
                                .background(if (isSelected) GangaOrange else Color.LightGray)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(40.dp))

                // Get Started / Next Button
                GangaButton(
                    text = if (state.isLastPage) "Get Started" else "Next",
                    onClick = {
                        if (state.isLastPage) {
                            onAction(OnboardingAction.OnGetStartedClick)
                        } else {
                            onAction(OnboardingAction.OnNextClick)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Small Branding footer
                Text(
                    text = "GANGA",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.LightGray,
                    letterSpacing = 2.sp
                )
            }
        }
    }
}


@Preview
@Composable
fun OnboardingScreenPrev() {
    OnboardingScreen(state = OnboardingState(), {})
}