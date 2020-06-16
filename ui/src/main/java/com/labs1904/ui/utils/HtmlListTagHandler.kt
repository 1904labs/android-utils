package com.labs1904.ui.utils

import android.text.Editable
import android.text.Html
import android.text.Spanned
import android.text.style.BulletSpan
import org.xml.sax.XMLReader

/**
 * This is a custom HTML tag handler that properly numbers ordered lists and maintains proper indentations.
 * This tag handler can be passed in when converting HTML into a spannable.
 *
 * See {@link android.text.Html#fromHtml(String, int, ImageGetter, TagHandler) fromHtml(String, int, ImageGetter TagHandler)}
 *
 * @param bulletGapWidth The distance, in pixels, between the bullet point and the paragraph.
 */
class HtmlListTagHandler(private val bulletGapWidth: Int) : Html.TagHandler {

    private var olStartIndex: Int = 0
    private var ulStartIndex: Int = 0

    override fun handleTag(
        opening: Boolean,
        tag: String?,
        output: Editable?,
        xmlReader: XMLReader?
    ) {
        output?.let {
            when {
                tag.equals(OL, true) && opening -> {
                    olStartIndex = it.length
                }
                tag.equals(OL, true) -> {
                    it.getSpans(olStartIndex, output.length, BulletSpan::class.java)
                        .forEachIndexed { index, spanToRemove ->
                            val insertString = "${index + 1}. "
                            val start = output.getSpanStart(spanToRemove)

                            output.removeSpan(spanToRemove)
                            output.insert(start, insertString)
                        }
                }
                tag.equals(UL, true) -> {
                    ulStartIndex = it.length
                }
                else -> {
                    it.getSpans(ulStartIndex, output.length, BulletSpan::class.java)
                        .forEach { spanToRemove ->
                            val start = output.getSpanStart(spanToRemove)
                            val end = output.getSpanEnd(spanToRemove)

                            output.removeSpan(spanToRemove)
                            output.setSpan(
                                BulletSpan(bulletGapWidth),
                                start,
                                end,
                                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                            )
                        }
                }
            }
        }
    }

    companion object {
        private const val OL = "OL"
        private const val UL = "UL"
    }
}