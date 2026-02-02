package com.shuham.ganga.presentation.dashboard.tabs.profile

data class ProfileState(
    val userName: String = "",
    val userEmail: String = "user@example.com", // Placeholder until backend user API
    val isLoading: Boolean = false,
    val appVersion: String = "App Version 2.4.0", // Added version
    val isVendor: Boolean = false // To toggle the "Switch to Vendor" banner state if needed
)