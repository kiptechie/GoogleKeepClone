package com.poetcodes.googlekeepclone.ui.fragments.labels

import android.view.View
import com.poetcodes.googlekeepclone.R

class OnNewNoteEdFocusChange(private val editAddLabelsFragment: EditAddLabelsFragment) :
    View.OnFocusChangeListener {

    override fun onFocusChange(p0: View?, p1: Boolean) {
        if (p1) {
            editAddLabelsFragment.binding.saveNewNoteIv.visibility = View.VISIBLE
            editAddLabelsFragment.binding.addClearIv.setImageResource(R.drawable.ic_baseline_clear_24)
        } else {
            editAddLabelsFragment.binding.saveNewNoteIv.visibility = View.INVISIBLE
            editAddLabelsFragment.binding.addClearIv.setImageResource(R.drawable.ic_baseline_add_24)
        }
    }

}