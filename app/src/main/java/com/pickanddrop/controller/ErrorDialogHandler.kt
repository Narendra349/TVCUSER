package com.pickanddrop.controller

import android.support.v4.app.FragmentActivity
import android.support.v7.app.AppCompatActivity
import com.pickanddrop.R
import com.pickanddrop.dialog.ErrorDialogFragment


import java.lang.ref.WeakReference

/**
 * A convenience class to handle displaying error dialogs.
 */
class ErrorDialogHandler(activity: FragmentActivity) {

    private val activityRef: WeakReference<FragmentActivity> = WeakReference(activity)

    fun show(errorMessage: String) {
        val activity = activityRef.get() ?: return

        ErrorDialogFragment.newInstance(activity.getString(R.string.validationErrors), errorMessage)
            .show(activity.supportFragmentManager, "error")
    }
}
