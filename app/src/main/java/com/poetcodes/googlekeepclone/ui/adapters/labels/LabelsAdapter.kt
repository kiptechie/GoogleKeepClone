package com.poetcodes.googlekeepclone.ui.adapters.labels

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.poetcodes.googlekeepclone.R
import com.poetcodes.googlekeepclone.repository.models.entities.Label
import timber.log.Timber

class LabelsAdapter(config: AsyncDifferConfig<Label>) :
    ListAdapter<Label, LabelsAdapter.ViewHolder>(config) {

    var itemChangeListener: LabelItemChangeListener? = null
    private var editTextsList: ArrayList<EditText> = ArrayList()

    class ViewHolder(view: View, private val labelsAdapter: LabelsAdapter) :
        RecyclerView.ViewHolder(view) {

        val deleteLabelIv: AppCompatImageView = view.findViewById(R.id.delete_label_iv)
        val saveLabelIv: AppCompatImageView = view.findViewById(R.id.save_edited_note_iv)
        val editText: EditText = view.findViewById(R.id.edit_note_ed)

        fun onBind(label: Label) {
            editText.addTextChangedListener(LabelEditTextWatcher(labelsAdapter, adapterPosition))
            editText.onFocusChangeListener = EditTextFocusChangeListener(
                this,
                labelsAdapter,
            )
            editText.setText(label.name)
            labelsAdapter.editTextsList.add(editText)
        }

    }

    fun fetchItem(position: Int): Label {
        return getItem(position)
    }

    fun clearAllInputFocus() {
        for (editText in editTextsList) {
            try {
                editText.clearFocus()
            } catch (ignore: Exception) {

            }
        }
        itemChangeListener?.onClearAllInputFocus()
    }

    fun performTextChange(text: String, position: Int) {
        try {
            val label = getItem(position)
            itemChangeListener?.onTextChanged(text, label)
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    fun setEditTextChangeListener(labelItemChangeListener: LabelItemChangeListener) {
        this.itemChangeListener = labelItemChangeListener
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