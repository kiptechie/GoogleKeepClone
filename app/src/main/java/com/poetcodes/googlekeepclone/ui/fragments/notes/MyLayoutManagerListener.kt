package com.poetcodes.googlekeepclone.ui.fragments.notes

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.poetcodes.googlekeepclone.ui.activities.LayoutManagerChangeListener

class MyLayoutManagerListener(private val notesFragment: NotesFragment) :
    LayoutManagerChangeListener {
    override fun onChange(isGrid: Boolean) {
        if (isGrid) {
            val pinnedGridLayoutManager = GridLayoutManager(notesFragment.requireContext(), 2)
            val normalGridLayoutManager = GridLayoutManager(notesFragment.requireContext(), 2)
            notesFragment.binding.pinnedNotesRecycler.layoutManager = pinnedGridLayoutManager
            notesFragment.binding.notesRecycler.layoutManager = normalGridLayoutManager
        } else {
            val pinnedLinearLayoutManager = LinearLayoutManager(notesFragment.requireContext())
            val normalLinearLayoutManager = LinearLayoutManager(notesFragment.requireContext())
            notesFragment.binding.pinnedNotesRecycler.layoutManager = pinnedLinearLayoutManager
            notesFragment.binding.notesRecycler.layoutManager = normalLinearLayoutManager
        }
    }
}