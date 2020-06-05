package com.labs1904.test_utils

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity

class TestActivity : AppCompatActivity() {

    companion object {
        fun createIntent(targetContext: Context) : Intent = Intent(targetContext, TestActivity::class.java)
    }
}
