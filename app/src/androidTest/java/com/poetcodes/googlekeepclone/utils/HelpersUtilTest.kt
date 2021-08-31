package com.poetcodes.googlekeepclone.utils

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.poetcodes.googlekeepclone.ui.activities.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HelpersUtilTest {

    @Rule
    @JvmField
    val mainActivity = ActivityTestRule(MainActivity::class.java)

    @Test
    fun snackBarWorks() {
        val snackBarMessage = "This is a test."
        HelpersUtil.showBottomSnack(mainActivity.activity.getBinding().root, snackBarMessage)
        onView(withId(com.google.android.material.R.id.snackbar_text))
            .check(matches(withText(snackBarMessage)))
    }
}