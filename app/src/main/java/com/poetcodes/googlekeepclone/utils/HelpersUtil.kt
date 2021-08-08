package com.poetcodes.googlekeepclone.utils

import android.os.Handler
import android.os.Looper
import android.view.View
import com.blankj.utilcode.util.SnackbarUtils
import com.poetcodes.googlekeepclone.utils.mappers.DateMapper

class HelpersUtil {

    companion object {
        @JvmStatic
        val dateMapper = DateMapper()

        @JvmStatic
        fun showBottomSnack(view: View, message: String?) {
            if (message != null) {
                SnackbarUtils.with(view)
                    .setMessage(message)
                    .setAction("Dismiss") {
                        SnackbarUtils.dismiss()
                    }
                    .setDuration(SnackbarUtils.LENGTH_INDEFINITE)
                    .show()
                val handler = Handler(Looper.getMainLooper())
                handler.postDelayed(
                    { SnackbarUtils.dismiss() },
                    2500
                )
            }
        }
    }

}