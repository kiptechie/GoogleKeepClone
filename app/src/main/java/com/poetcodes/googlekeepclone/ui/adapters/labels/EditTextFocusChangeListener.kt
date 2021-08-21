package com.poetcodes.googlekeepclone.ui.adapters.labels

import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import com.blankj.utilcode.util.KeyboardUtils
import com.poetcodes.googlekeepclone.R

class EditTextFocusChangeListener(
    private val viewHolder: LabelsAdapter.ViewHolder,
    private val labelsAdapter: LabelsAdapter
) : View.OnFocusChangeListener {

    private val deleteLabelIv: AppCompatImageView = viewHolder.deleteLabelIv
    private val saveLabelIv: AppCompatImageView = viewHolder.saveLabelIv
    private val listener = this.labelsAdapter.itemChangeListener

    override fun onFocusChange(p0: View?, p1: Boolean) {
        if (p1) {
            deleteLabelIv.setImageResource(R.drawable.ic_outline_delete_24)
            deleteLabelIv.setOnClickListener {
                val label = labelsAdapter.fetchItem(viewHolder.adapterPosition)
                listener?.onDeleteLabelClicked(label)
            }
            saveLabelIv.setImageResource(R.drawable.ic_baseline_check_24)
            saveLabelIv.setOnClickListener {
                viewHolder.editText.clearFocus()
                saveLabelIv.setImageResource(R.drawable.ic_outline_edit_24)
                KeyboardUtils.hideSoftInput(viewHolder.editText)
                listener?.onSaveLabelClicked()
            }
        } else {
            deleteLabelIv.setImageResource(R.drawable.ic_outline_label_24)
            deleteLabelIv.setOnClickListener {
                labelsAdapter.clearAllInputFocus()
            }
            saveLabelIv.setImageResource(R.drawable.ic_outline_edit_24)
            saveLabelIv.setOnClickListener {
                viewHolder.editText.requestFocus()
                KeyboardUtils.showSoftInput(viewHolder.editText)
            }
        }
    }

}