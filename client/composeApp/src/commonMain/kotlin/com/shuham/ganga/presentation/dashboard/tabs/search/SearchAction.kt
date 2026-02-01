package com.shuham.ganga.presentation.dashboard.tabs.search

sealed interface SearchAction {
    data class OnQueryChange(val query: String) : SearchAction
    data class OnSearchClick(val query: String) : SearchAction
    data object OnBackClick : SearchAction
}