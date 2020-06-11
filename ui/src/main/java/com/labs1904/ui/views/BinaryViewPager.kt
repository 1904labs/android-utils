package com.labs1904.ui.views

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.tabs.TabLayout
import com.labs1904.ui.R

/**
 * This is a custom ViewPager that lazily loads fragments and does not support swiping. Currently,
 * it only supports a max of two Fragments.
 */
class BinaryViewPager(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs),
    TabLayout.OnTabSelectedListener {

    private var adapter: BinaryViewPagerAdapter? = null
    private var fragmentManager: FragmentManager? = null
    private var tabLayout: TabLayout? = null

    fun setUp(
        adapter: BinaryViewPagerAdapter,
        fragmentManager: FragmentManager,
        tabLayout: TabLayout
    ) {
        this.adapter = adapter
        this.fragmentManager = fragmentManager
        this.tabLayout = tabLayout

        tabLayout.addTab(tabLayout.newTab().also { it.text = adapter.getPageTitle(0) })
        tabLayout.addTab(tabLayout.newTab().also { it.text = adapter.getPageTitle(1) })

        tabLayout.addOnTabSelectedListener(this)

        if (fragmentManager.fragments.isNullOrEmpty()) {
            safeLet(
                findOrCreateFragment(1),
                findOrCreateFragment(0)
            ) { fragmentToHide, fragmentToShow ->
                executeTransaction(fragmentToHide, fragmentToShow, adapter.getPageTitle(0))
            }
        }
    }

    override fun onSaveInstanceState(): Parcelable? {
        return SavedState(super.onSaveInstanceState()).also {
            it.selectedItem = tabLayout?.selectedTabPosition ?: -1
        }
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        if (state !is SavedState) {
            super.onRestoreInstanceState(state)
        } else {
            super.onRestoreInstanceState(state.superState)
            if (state.selectedItem >= 0) tabLayout?.getTabAt(state.selectedItem)?.select()
        }
    }

    override fun onTabSelected(tab: TabLayout.Tab) {
        val hideFragment =
            (tab.position == 0) then findOrCreateFragment(1) ?: findOrCreateFragment(0)
        val showFragment =
            (tab.position == 0) then findOrCreateFragment(0) ?: findOrCreateFragment(1)

        val exitSlide =
            (tab.position == 0) then R.anim.exit_right_view_pager ?: R.anim.exit_left_view_pager
        val enterSlide =
            (tab.position == 0) then R.anim.enter_left_view_pager ?: R.anim.enter_right_view_pager

        safeLet(hideFragment, showFragment, adapter) { fragmentToHide, fragmentToShow, adapter ->
            executeTransaction(
                fragmentToHide,
                fragmentToShow,
                adapter.getPageTitle(tab.position),
                enterSlide,
                exitSlide
            )
        }
    }

    override fun onTabReselected(tab: TabLayout.Tab) {}

    override fun onTabUnselected(tab: TabLayout.Tab) {}

    private fun executeTransaction(
        fragmentToHide: Fragment,
        fragmentToShow: Fragment,
        pageTitle: String,
        enterAnimation: Int? = null,
        exitAnimation: Int? = null
    ) {
        fragmentManager?.beginTransaction()
            ?.also {
                if (enterAnimation != null && exitAnimation != null) it.setCustomAnimations(
                    enterAnimation,
                    exitAnimation
                )
            }
            ?.also { if (!fragmentToShow.isAdded) it.add(id, fragmentToShow, pageTitle) }
            ?.hide(fragmentToHide)
            ?.show(fragmentToShow)
            ?.commit()
    }

    private fun findOrCreateFragment(position: Int): Fragment? =
        fragmentManager?.findFragmentByTag(
            adapter?.getPageTitle(position)
        ) ?: adapter?.getItem(
            position
        )

    private infix fun <T> Boolean.then(value: T): T? = if (this) value else null

    private fun <T1 : Any, T2 : Any, R : Any> safeLet(p1: T1?, p2: T2?, block: (T1, T2) -> R?): R? {
        return if (p1 != null && p2 != null) block(p1, p2) else null
    }

    private fun <T1 : Any, T2 : Any, T3 : Any, R : Any> safeLet(
        p1: T1?,
        p2: T2?,
        p3: T3?,
        block: (T1, T2, T3) -> R?
    ): R? {
        return if (p1 != null && p2 != null && p3 != null) block(p1, p2, p3) else null
    }
}

interface BinaryViewPagerAdapter {
    fun getItem(position: Int): Fragment
    fun getPageTitle(position: Int): String
}

internal class SavedState : View.BaseSavedState {

    var selectedItem: Int = -1

    constructor(superState: Parcelable?) : super(superState)

    private constructor (input: Parcel) : super(input) {
        selectedItem = input.readInt()
    }

    override fun writeToParcel(out: Parcel?, flags: Int) {
        super.writeToParcel(out, flags)
        out?.writeInt(selectedItem)
    }

    companion object {
        @Suppress("unused")
        @JvmField
        val CREATOR: Parcelable.Creator<SavedState> = object : Parcelable.Creator<SavedState> {
            override fun createFromParcel(source: Parcel): SavedState = SavedState(source)
            override fun newArray(size: Int): Array<SavedState?> = arrayOfNulls(size)
        }
    }
}