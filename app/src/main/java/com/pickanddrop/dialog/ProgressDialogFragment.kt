package com.pickanddrop.dilog

import android.app.Dialog
import android.app.ProgressDialog
import android.os.Bundle
import android.support.v4.app.DialogFragment

class ProgressDialogFragment : DialogFragment() {

    private val message: String?
        get() = arguments?.getString("message")

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = ProgressDialog(activity)
        dialog.setMessage(message)
        return dialog
    }

    companion object {
        fun newInstance(message: String): ProgressDialogFragment {
            val fragment = ProgressDialogFragment()

            val args = Bundle()
            args.putString("message", message)

            fragment.arguments = args

            return fragment
        }
    }
}
