package com.poetcodes.googlekeepclone.ui.fragments.labels

import android.app.AlertDialog
import android.content.DialogInterface
import com.blankj.utilcode.util.KeyboardUtils
import com.blankj.utilcode.util.ToastUtils
import com.poetcodes.googlekeepclone.repository.models.entities.Label
import com.poetcodes.googlekeepclone.ui.adapters.labels.LabelItemChangeListener

class LabelChangeListener(private val editAddLabelsFragment: EditAddLabelsFragment) :
    LabelItemChangeListener {

    override fun onTextChanged(text: String, label: Label) {
        editAddLabelsFragment.onTextChanged(text, label)
    }

    override fun onSaveLabelClicked() {
        editAddLabelsFragment.cleanLabels()
    }

    override fun onDeleteLabelClicked(label: Label) {
        val alertDialog: AlertDialog = AlertDialog.Builder(editAddLabelsFragment.requireContext())
            .setTitle("Delete label?")
            .setMessage("We'll delete this label and remove it from all of your notes. Your notes won't be deleted.")
            .setNegativeButton("Cancel", NegativeDialogClickListener())
            .setPositiveButton("Delete", PositiveDialogClickListener(label, editAddLabelsFragment))
            .create()
        alertDialog.show()
    }

    private class PositiveDialogClickListener(
        private val label: Label,
        private val editAddLabelsFragment: EditAddLabelsFragment
    ) : DialogInterface.OnClickListener {

        override fun onClick(p0: DialogInterface?, p1: Int) {
            editAddLabelsFragment.deleteLabel(label)
            ToastUtils.showShort("\"${label.name}\" Deleted!")
        }
    }

    private class NegativeDialogClickListener : DialogInterface.OnClickListener {
        override fun onClick(p0: DialogInterface?, p1: Int) {
            p0?.dismiss()
        }
    }

    override fun onClearAllInputFocus() {
        KeyboardUtils.hideSoftInput(editAddLabelsFragment.requireView())
    }
}