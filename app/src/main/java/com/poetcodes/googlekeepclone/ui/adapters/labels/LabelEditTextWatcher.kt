package com.poetcodes.googlekeepclone.ui.adapters.labels

import android.text.Editable
import android.text.TextWatcher
import io.sentry.Sentry
import timber.log.Timber

class LabelEditTextWatcher(
    private val labelsAdapter: LabelsAdapter,
    private val position: Int
) :
    TextWatcher {

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        //Do nothing
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        //Do nothing
    }

    override fun afterTextChanged(p0: Editable?) {
        if (p0 != null && p0.isNotEmpty()) {
            try {
                val label = labelsAdapter.fetchItem(position)
                val newString = p0.toString()
                val oldString = label.name
                if (newString != oldString) {
                    labelsAdapter.performTextChange(newString, position)
                }
            } catch (e: Exception) {
                Timber.e(e)
                Sentry.captureException(e)
            }
        }
    }

}