package com.labs1904.ui.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * This utility function cuts down on some of the boilerplate code around creating a
 * {@link androidx.lifecycle.ViewModelProvider.Factory ViewModelProvider.Factory}
 */
fun viewModelFactory(factory: () -> ViewModel): ViewModelProvider.Factory =
    object : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return factory() as T
        }
    }