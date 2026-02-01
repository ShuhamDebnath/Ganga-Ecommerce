package com.shuham.ganga.di

import com.russhwolf.settings.Settings
import com.shuham.ganga.data.local.TokenManager
import com.shuham.ganga.data.repository.AuthRepositoryImpl
import com.shuham.ganga.data.repository.ProductRepositoryImpl
import com.shuham.ganga.domain.repository.AuthRepository
import com.shuham.ganga.domain.repository.ProductRepository
import com.shuham.ganga.presentation.auth.login.LoginViewModel
import com.shuham.ganga.presentation.auth.onboarding.OnboardingViewModel
import com.shuham.ganga.presentation.auth.signup.SignUpViewModel
import com.shuham.ganga.presentation.auth.splash.SplashViewModel
import com.shuham.ganga.presentation.dashboard.tabs.home.HomeViewModel
import com.shuham.ganga.presentation.dashboard.tabs.search.SearchViewModel
import com.shuham.ganga.presentation.detail.ProductDetailViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val appModule = module {
    //Token Manager
    single<Settings> { Settings() } // <-- FIX: Define Settings provider
    single { TokenManager(get()) }  // <-- FIX: Inject Settings into TokenManager

    //  Repository
    singleOf(::AuthRepositoryImpl).bind<AuthRepository>()
    singleOf(::ProductRepositoryImpl).bind<ProductRepository>()

    // ViewModels
    viewModelOf(::LoginViewModel)
    viewModelOf(::SignUpViewModel)
    viewModelOf(::OnboardingViewModel)
    viewModelOf(::SplashViewModel)
    viewModelOf(::HomeViewModel)
    viewModelOf(::SearchViewModel)
    viewModelOf(::ProductDetailViewModel)
}

