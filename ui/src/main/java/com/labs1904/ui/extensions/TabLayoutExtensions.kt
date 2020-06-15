package com.labs1904.ui.extensions

import com.google.android.material.tabs.TabLayout

/**
 * Easily add a tab selected listener to a TabLayout without all of the boilerplate code.
 *
 * @param listener A function that has contains a Tab as a parameter and performs any logic you would
 * like to run once a Tab is selected.
 */
fun TabLayout.addOnTabSelectedListener(listener: (TabLayout.Tab?) -> Unit) {
    addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
        override fun onTabReselected(tab: TabLayout.Tab?) = Unit
        override fun onTabUnselected(tab: TabLayout.Tab?) = Unit
        override fun onTabSelected(tab: TabLayout.Tab?) = listener(tab)
    })
}