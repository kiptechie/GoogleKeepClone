package com.poetcodes.googlekeepclone.ui.adapters

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.KeyboardUtils
import com.poetcodes.googlekeepclone.R
import com.poetcodes.googlekeepclone.repository.models.entities.Label
import timber.log.Timber

class LabelsAdapter(config: AsyncDifferConfig<Label>) :
    ListAdapter<Label, LabelsAdapter.ViewHolder>(config) {

    private var editTextChangeListener: EditTextChangeListener? = null
    private var editTextsList: ArrayList<EditText> = ArrayList()

    class ViewHolder(view: View, private val labelsAdapter: LabelsAdapter) :
        RecyclerView.ViewHolder(view) {

        val deleteLabelIv: AppCompatImageView = view.findViewById(R.id.delete_label_iv)
        val saveLabelIv: AppCompatImageView = view.findViewById(R.id.save_edited_note_iv)
        val editText: EditText = view.findViewById(R.id.edit_note_ed)

        fun onBind(label: Label) {
            editText.addTextChangedListener(MyTextWatcher(labelsAdapter, adapterPosition))
            editText.onFocusChangeListener = MyFocusChangeListener(
                this,
                labelsAdapter,
            )
            editText.setText(label.name)
            labelsAdapter.editTextsList.add(editText)
        }

    }

    private class MyFocusChangeListener(
        private val viewHolder: ViewHolder,
        private val labelsAdapter: LabelsAdapter
    ) : View.OnFocusChangeListener {

        private val deleteLabelIv: AppCompatImageView = viewHolder.deleteLabelIv
        private val saveLabelIv: AppCompatImageView = viewHolder.saveLabelIv
        private val listener = this.labelsAdapter.editTextChangeListener

        override fun onFocusChange(p0: View?, p1: Boolean) {
            if (p1) {
                deleteLabelIv.setImageResource(R.drawable.ic_outline_delete_24)
                deleteLabelIv.setOnClickListener {
                    val label = labelsAdapter.getItem(viewHolder.adapterPosition)
                    listener?.onDeleteLabelClicked(label)
                }
                saveLabelIv.setImageResource(R.drawable.ic_baseline_check_24)
                saveLabelIv.setOnClickListener {
                    viewHolder.editText.clearFocus()
                    saveLabelIv.setImageResource(R.drawable.ic_outline_edit_24)
                    KeyboardUtils.hideSoftInput(viewHolder.editText)
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

    fun clearAllInputFocus() {
        for (editText in editTextsList) {
            try {
                editText.clearFocus()
            } catch (ignore: Exception) {

            }
        }
        editTextChangeListener?.onClearAllInputFocus()
    }

    private class MyTextWatcher(
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
                    val label = labelsAdapter.getItem(position)
                    val newString = p0.toString()
                    val oldString = label.name
                    if (newString != oldString) {
                        labelsAdapter.performTextChange(newString, position)
                    }
                } catch (e: Exception) {
                    Timber.e(e)
                }
            }
        }

    }

    private fun performTextChange(text: String, position: Int) {
        try {
            val label = getItem(position)
            editTextChangeListener?.onTextChanged(text, label)
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    fun setEditTextChangeListener(editTextChangeListener: EditTextChangeListener) {
        this.editTextChangeListener = editTextChangeListener
    }

    interface EditTextChangeListener {
        fun onTextChanged(text: String, label: Label)
        fun onSaveLabelClicked()
        fun onDeleteLabelClicked(label: Label)
        fun onClearAllInputFocus()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_label, parent, false)
        return ViewHolder(view, this)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val label = getItem(position)
        if (label.name != "") {
            holder.onBind(label)
        }
    }
}