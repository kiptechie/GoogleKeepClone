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
import com.poetcodes.googlekeepclone.R
import com.poetcodes.googlekeepclone.repository.models.entities.Label
import timber.log.Timber

class LabelsAdapter(config: AsyncDifferConfig<Label>) :
    ListAdapter<Label, LabelsAdapter.ViewHolder>(config) {

    private var editTextChangeListener: EditTextChangeListener? = null

    class ViewHolder(view: View, adapter: LabelsAdapter) : RecyclerView.ViewHolder(view) {

        val deleteLabelIv: AppCompatImageView = view.findViewById(R.id.delete_label_iv)
        val saveLabelIv: AppCompatImageView = view.findViewById(R.id.save_edited_note_iv)
        private val editText: EditText = view.findViewById(R.id.edit_note_ed)
        private val labelsAdapter = adapter

        init {
            editText.addTextChangedListener(MyTextWatcher(labelsAdapter, adapterPosition))
            editText.onFocusChangeListener = MyFocusChangeListener(this)
        }

        fun onBind(label: Label) {
            editText.setText(label.name)
        }

    }

    class MyFocusChangeListener(viewHolder: ViewHolder) : View.OnFocusChangeListener {

        private val deleteLabelIv: AppCompatImageView = viewHolder.deleteLabelIv
        private val saveLabelIv: AppCompatImageView = viewHolder.saveLabelIv

        override fun onFocusChange(p0: View?, p1: Boolean) {
            if (p1) {
                deleteLabelIv.setImageResource(R.drawable.ic_outline_delete_24)
                saveLabelIv.setImageResource(R.drawable.ic_baseline_check_24)
            } else {
                deleteLabelIv.setImageResource(R.drawable.ic_outline_label_24)
                saveLabelIv.setImageResource(R.drawable.ic_outline_edit_24)
            }
        }

    }

    class MyTextWatcher(adapter: LabelsAdapter, position: Int) :
        TextWatcher {

        private val mPosition = position
        private val mAdapter = adapter

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            //TODO("Not yet implemented")
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            //TODO("Not yet implemented")
        }

        override fun afterTextChanged(p0: Editable?) {
            if (p0 != null && p0.isNotEmpty()) {
                mAdapter.performTextChange(p0.toString(), mPosition)
            }
        }

    }

    fun performTextChange(text: String, position: Int) {
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