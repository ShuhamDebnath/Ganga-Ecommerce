package com.shuham.ganga.presentation.auth.login

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import com.shuham.ganga.presentation.components.GangaButton
import com.shuham.ganga.presentation.components.GangaSocialButton
import com.shuham.ganga.presentation.components.GangaTextField
import com.shuham.ganga.presentation.theme.GangaOrange
import com.shuham.ganga.presentation.theme.GangaOrangeLight
import ganga.composeapp.generated.resources.Res
import ganga.composeapp.generated.resources.ic_google
import ganga.composeapp.generated.resources.ic_lock
import ganga.composeapp.generated.resources.ic_bag
import ganga.composeapp.generated.resources.ic_mail
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun LoginScreenRoot(
    viewModel: LoginViewModel = koinViewModel(),
    onLoginSuccess: () -> Unit,
    onNavigateToSignUp: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    // Handle Success Navigation
    LaunchedEffect(state.successMessage) {
        if (state.successMessage != null) {
            onLoginSuccess()
        }
    }



    LoginScreen(
        state = state,
        onAction = { action ->
            // 2. Intercept navigation actions here
            when (action) {
                is LoginAction.OnSignUpClick -> onNavigateToSignUp()
                else -> viewModel.onAction(action)
            }
        }
    )
}

@Composable
fun LoginScreen(
    state: LoginState,
    onAction: (LoginAction) -> Unit
) {
    Scaffold(containerColor = Color.White) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            // Logo
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(GangaOrangeLight),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_bag),
                    contentDescription = null,
                    tint = GangaOrange,
                    modifier = Modifier.size(40.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Welcome Back!",
                style = MaterialTheme.typography.headlineLarge,
                color = GangaOrange,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Sign in to continue to Ganga",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Email
            GangaTextField(
                value = state.email,
                onValueChange = { onAction(LoginAction.OnEmailChange(it)) },
                placeholder = "Enter your email",
                leadingIcon = Res.drawable.ic_mail
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Password
            GangaTextField(
                value = state.password,
                onValueChange = { onAction(LoginAction.OnPasswordChange(it)) },
                placeholder = "Enter your password",
                leadingIcon = Res.drawable.ic_lock,
                isPassword = true,
                isPasswordVisible = state.isPasswordVisible,
                onToggleVisibility = { onAction(LoginAction.OnTogglePasswordVisibility) }
            )

            Text(
                text = "Forgot Password?",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
                    .clickable { onAction(LoginAction.OnForgotPasswordClick) },
                textAlign = TextAlign.End,
                color = GangaOrange,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.labelMedium
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Login Button
            GangaButton(
                text = "Login",
                onClick = { onAction(LoginAction.OnLoginClick) },
                isLoading = state.isLoading,
                enabled = !state.isLoading
            )

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
                onClick= { onAction(LoginAction.OnGoogleSignInClick) },
                icon= Res.drawable.ic_google,
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = buildAnnotatedString {
                    append("Don't have an account? ")
                    withStyle(style = SpanStyle(color = GangaOrange, fontWeight = FontWeight.Bold)) {
                        append("Sign up")
                    }
                },
                modifier = Modifier.clickable { onAction(LoginAction.OnSignUpClick) },
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Preview
@Composable
fun LoginScreenPrev() {
    LoginScreen(
        state = LoginState(),
        onAction = {}
    )
}

