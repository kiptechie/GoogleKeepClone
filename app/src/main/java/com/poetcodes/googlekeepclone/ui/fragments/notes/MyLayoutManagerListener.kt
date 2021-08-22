package com.poetcodes.googlekeepclone.ui.fragments.notes

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.poetcodes.googlekeepclone.ui.activities.LayoutManagerChangeListener

class MyLayoutManagerListener(private val notesFragment: NotesFragment) :
    LayoutManagerChangeListener {
    override fun onChange(isGrid: Boolean) {
        if (isGrid) {
            val gridLayoutManager = GridLayoutManager(notesFragment.requireContext(), 2)
            notesFragment.binding.notesRecycler.layoutManager = gridLayoutManager
        } else {
            val linearLayoutManager = LinearLayoutManager(notesFragment.requireContext())
            notesFragment.binding.notesRecycler.layoutManager = linearLayoutManager
        }
    }
}