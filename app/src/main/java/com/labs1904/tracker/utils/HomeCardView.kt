package com.labs1904.tracker.utils

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.google.android.material.card.MaterialCardView
import com.labs1904.tracker.R
import kotlinx.android.synthetic.main.home_card.view.*

class HomeCardView(context: Context, attrs: AttributeSet) : MaterialCardView(context, attrs) {

	private var title: String? = ""
	set(value) {
		field = value

		home_card_title.text = value
	}

	var value: String? = ""
	set(value) {
		field = value

		home_card_value.text = value
	}

	init {
	    View.inflate(context, R.layout.home_card, this)

		cardElevation = context.resources.getDimension(R.dimen.home_card_elevation)

		val typedArray = context.obtainStyledAttributes(attrs, R.styleable.HomeCardView, 0, 0)
		typedArray.getString(R.styleable.HomeCardView_title)?.let { title = it }
		typedArray.getString(R.styleable.HomeCardView_value)?.let { value = it }
		typedArray.recycle()
	}

}
