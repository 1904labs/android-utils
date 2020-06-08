package com.labs1904.test_utils.extensions

import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.whenever

fun Fragment.replaceNavControllerWithMock(mockNavController: NavController) = this.also {
    runOnMainThread {
        it.viewLifecycleOwnerLiveData.observeForever { lifecycleOwner ->
            if (lifecycleOwner != null) {
                whenever(mockNavController.getViewModelStoreOwner(any())).thenReturn(it)
                Navigation.setViewNavController(it.requireView(), mockNavController)
            }
        }
    }
}