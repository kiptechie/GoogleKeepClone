package com.poetcodes.googlekeepclone.ui.fragments.labels

import com.poetcodes.googlekeepclone.repository.models.entities.Label
import com.poetcodes.googlekeepclone.ui.adapters.LabelsAdapter

class EditTextChangeListener(fragment: EditAddLabelsFragment): LabelsAdapter.EditTextChangeListener {

    private val mFragment = fragment

    override fun onTextChanged(text: String, label: Label) {
        mFragment.onTextChanged(text, label)
    }
}