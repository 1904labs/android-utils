package com.labs1904.ui.extensions

import com.google.android.material.tabs.TabLayout

fun TabLayout.addOnTabSelectedListener(listener: (TabLayout.Tab?) -> Unit) {
    addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
        override fun onTabReselected(tab: TabLayout.Tab?) = Unit
        override fun onTabUnselected(tab: TabLayout.Tab?) = Unit
        override fun onTabSelected(tab: TabLayout.Tab?) = listener(tab)
    })
}