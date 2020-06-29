package com.labs1904.ui.views

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.Gravity
import android.widget.CompoundButton
import androidx.core.widget.CompoundButtonCompat
import com.labs1904.ui.R

/**
 * This is a custom toggle button that allows you to have a checked and an unchecked state with a
 * icon in the center. Android, as of the time of creating this implementation,
 * does not have a built-in way of having an icon without text centered within a compound button.
 */
class IconToggleButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : CompoundButton(context, attrs, defStyleAttr) {

    init {
        val attributes =
            context.obtainStyledAttributes(attrs, R.styleable.IconToggleButton, defStyleAttr, 0)
        buttonDrawable = attributes.getDrawable(R.styleable.IconToggleButton_drawableCenter)
        gravity = Gravity.CENTER
        attributes.recycle()
    }

    override fun onDraw(canvas: Canvas?) {
        val buttonDrawable = CompoundButtonCompat.getButtonDrawable(this)
        if (buttonDrawable != null && canvas != null) {
            buttonDrawable.state = drawableState

            val verticalGravity = gravity and Gravity.VERTICAL_GRAVITY_MASK

            val height = buttonDrawable.intrinsicHeight
            val buttonWidth = buttonDrawable.intrinsicWidth
            val buttonLeft = (width - buttonWidth) / 2

            val y = when (verticalGravity) {
                Gravity.BOTTOM -> getHeight() - height
                Gravity.CENTER_VERTICAL -> (getHeight() - height) / 2
                else -> 0
            }

            buttonDrawable.setBounds(buttonLeft, y, buttonLeft + buttonWidth, y + height)
            buttonDrawable.draw(canvas)
        }
    }
}