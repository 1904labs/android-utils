package com.labs1904.core.navigation

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.Navigator

/**
 * There is currently a bug within the AndroidX Navigation library that can cause the application to crash.
 * The crash occurs when a user simultaneously clicks two views that initiate a navigation action. To prevent the crash
 * from happening, we have created this wrapper function around <a href="https://developer.android.com/reference/androidx/navigation/NavController#navigate(int,%20android.os.Bundle)">NavController.navigate()</a>.
 *
 * This wrapper function checks whether another action has already been initiated before calling navigate().
 *
 * @param resId an {@link NavDestination#getAction(int) action} id or a destination id to navigate to
 * @param args arguments to pass to the destination
 * @param navOptions special options for this navigation operation
 * @param navExtras extras to pass to the Navigator
 */
fun NavController.navigateSafe(
	@IdRes resId: Int,
	args: Bundle? = null,
	navOptions: NavOptions? = null,
	navExtras: Navigator.Extras? = null
) {
	val action = currentDestination?.getAction(resId) ?: graph.getAction(resId)

	if (action != null && currentDestination?.id != action.destinationId) {
		navigate(resId, args, navOptions, navExtras)
	}
}
