package com.labs1904.ui.views

import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment

open class BaseFragment : Fragment() {

    protected val activity: AppCompatActivity?
        get() = (getActivity() as? AppCompatActivity)

    protected val supportActionBar: ActionBar?
        get() = activity?.supportActionBar

    protected fun setUpToolbar(toolbar: Toolbar, initialTitle: String? = null, shouldShowTitle: Boolean = true) {
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