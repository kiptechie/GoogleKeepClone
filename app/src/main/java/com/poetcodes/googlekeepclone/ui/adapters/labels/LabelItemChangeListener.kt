package com.poetcodes.googlekeepclone.ui.adapters.labels

import com.poetcodes.googlekeepclone.repository.models.entities.Label

interface LabelItemChangeListener {
    fun onTextChanged(text: String, label: Label)
    fun onSaveLabelClicked()
    fun onDeleteLabelClicked(label: Label)
    fun onClearAllInputFocus()
}