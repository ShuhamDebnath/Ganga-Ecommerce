package com.shuham.ganga.di

import com.shuham.ganga.data.repository.AuthRepositoryImpl
import com.shuham.ganga.domain.repository.AuthRepository
import com.shuham.ganga.presentation.auth.login.LoginViewModel
import com.shuham.ganga.presentation.auth.onboarding.OnboardingViewModel
import com.shuham.ganga.presentation.auth.signup.SignUpViewModel
import com.shuham.ganga.presentation.auth.splash.SplashViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val appModule = module {
    // 1. Repository
    singleOf(::AuthRepositoryImpl).bind<AuthRepository>()

    // ViewModels
    viewModelOf(::LoginViewModel)
    viewModelOf(::SignUpViewModel)
    viewModelOf(::OnboardingViewModel)
    viewModelOf(::SplashViewModel)
}

