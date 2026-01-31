package com.shuham.ganga.presentation.auth.signup


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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shuham.ganga.presentation.auth.login.LoginAction
import com.shuham.ganga.presentation.components.GangaButton
import com.shuham.ganga.presentation.components.GangaSocialButton
import com.shuham.ganga.presentation.components.GangaTextField
import com.shuham.ganga.presentation.theme.GangaOrange
import com.shuham.ganga.presentation.theme.GangaOrangeLight
import ganga.composeapp.generated.resources.Res
import ganga.composeapp.generated.resources.ic_arrow_back
import ganga.composeapp.generated.resources.ic_google
import ganga.composeapp.generated.resources.ic_lock
import ganga.composeapp.generated.resources.ic_logo_bag
import ganga.composeapp.generated.resources.ic_mail
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel


@Composable
fun SignUpScreenRoot(
    viewModel: SignUpViewModel = koinViewModel(),
    onNavigateToLogin: () -> Unit,
    onSignUpSuccess: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(state.successMessage) {
        if (state.successMessage != null) {
            onSignUpSuccess()
        }
    }

    SignUpScreen(
        state = state,
        onAction = { action ->
            if (action is SignUpAction.NavigateToLogin) {
                onNavigateToLogin()
            } else {
                viewModel.onAction(action)
            }
        }
    )
}

@Composable
fun SignUpScreen(
    state: SignUpState,
    onAction: (SignUpAction) -> Unit
) {
    Scaffold(
        containerColor = Color.White
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            // Back Button
            Box(modifier = Modifier.fillMaxWidth()) {
                IconButton(
                    onClick = { onAction(SignUpAction.NavigateToLogin) },
                    modifier = Modifier.size(40.dp).background(Color(0xFFF5F5F5), RoundedCornerShape(12.dp))
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_arrow_back),
                        contentDescription = "Back",
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Logo Header
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(GangaOrangeLight),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_logo_bag),
                    contentDescription = null,
                    tint = GangaOrange,
                    modifier = Modifier.size(40.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Create Account",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF1A1A1A)
            )

            Text(
                text = "Join Ganga and start your shopping journey.",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Name Field
            GangaTextField(
                value = state.name,
                onValueChange = { onAction(SignUpAction.OnNameChange(it)) },
                placeholder = "Enter your full name",
                leadingIcon = Res.drawable.ic_mail // Replace with ic_person when available
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Email Field
            GangaTextField(
                value = state.email,
                onValueChange = { onAction(SignUpAction.OnEmailChange(it)) },
                placeholder = "Enter your email",
                leadingIcon = Res.drawable.ic_mail
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Password Field
            GangaTextField(
                value = state.password,
                onValueChange = { onAction(SignUpAction.OnPasswordChange(it)) },
                placeholder = "Create a password",
                leadingIcon = Res.drawable.ic_lock,
                isPassword = true,
                isPasswordVisible = state.isPasswordVisible,
                onToggleVisibility = { onAction(SignUpAction.OnTogglePasswordVisibility) }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Vendor Toggle (Unique to Sign Up, so not shared)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("I am a Vendor", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyLarge)
                    Text("Create a seller account", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                }
                Switch(
                    checked = state.isVendor,
                    onCheckedChange = { onAction(SignUpAction.OnToggleVendor(it)) },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = GangaOrange,
                        uncheckedTrackColor = Color(0xFFE0E0E0)
                    )
                )
            }

            // Animated Store Name Field
            if (state.isVendor) {
                Spacer(modifier = Modifier.height(16.dp))
                GangaTextField(
                    value = state.storeName,
                    onValueChange = { onAction(SignUpAction.OnStoreNameChange(it)) },
                    placeholder = "Your Business Name",
                    leadingIcon = Res.drawable.ic_logo_bag
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Terms Checkbox
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = state.agreedToTerms,
                    onCheckedChange = { onAction(SignUpAction.OnToggleTerms) },
                    colors = CheckboxDefaults.colors(checkedColor = GangaOrange)
                )
                Text(
                    text = buildAnnotatedString {
                        append("By signing up, you agree to our ")
                        withStyle(style = SpanStyle(color = GangaOrange)) { append("Terms of Service") }
                        append(" and ")
                        withStyle(style = SpanStyle(color = GangaOrange)) { append("Privacy Policy") }
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    lineHeight = 16.sp
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Sign Up Button
            GangaButton(
                text = "Sign Up",
                onClick = { onAction(SignUpAction.OnSignUpClick) },
                isLoading = state.isLoading,
                enabled = state.agreedToTerms
            )

            Spacer(modifier = Modifier.height(24.dp))

            Spacer(modifier = Modifier.height(24.dp))

            // Social Divider
            Row(verticalAlignment = Alignment.CenterVertically) {
                HorizontalDivider(modifier = Modifier.weight(1f))
                Text(" Or continue with ", modifier = Modifier.padding(horizontal = 8.dp), style = MaterialTheme.typography.labelMedium)
                HorizontalDivider(modifier = Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Google Button
            GangaSocialButton(
                text = "Continue with Google",
                onClick= { onAction(SignUpAction.OnGoogleSignInClick) },
                icon= Res.drawable.ic_google,
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Footer
            Text(
                text = buildAnnotatedString {
                    append("Already have an account? ")
                    withStyle(style = SpanStyle(color = GangaOrange, fontWeight = FontWeight.Bold)) {
                        append("Log In")
                    }
                },
                modifier = Modifier.clickable { onAction(SignUpAction.NavigateToLogin) },
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Preview
@Composable
fun SignUpScreenPreview() {
    SignUpScreen(
        state = SignUpState(),
        onAction = {}
    )
}