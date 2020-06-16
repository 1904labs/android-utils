package com.labs1904.ui.views

import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment

/**
 * BaseFragment is an extension of Fragment with a few helpers tacked on. This class makes setting up a toolbar,
 * getting a reference to an AppCompatActivity, and getting a reference to the ActionBar a bit easier.
 */
open class BaseFragment : Fragment() {

    protected val activity: AppCompatActivity?
        get() = (getActivity() as? AppCompatActivity)

    protected val supportActionBar: ActionBar?
        get() = activity?.supportActionBar

    protected fun setUpToolbar(
        toolbar: Toolbar,
        initialTitle: String? = null,
        shouldShowTitle: Boolean = true
    ) {
        activity?.apply {
            setSupportActionBar(toolbar)

            supportActionBar?.apply {
                setDisplayHomeAsUpEnabled(true)
                setDisplayShowTitleEnabled(shouldShowTitle)
                initialTitle?.let { this.title = it }
            }
        }
    }
}