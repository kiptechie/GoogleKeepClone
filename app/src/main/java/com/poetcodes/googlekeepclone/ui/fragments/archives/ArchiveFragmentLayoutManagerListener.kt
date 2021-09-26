package com.poetcodes.googlekeepclone.ui.fragments.archives

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.poetcodes.googlekeepclone.ui.activities.LayoutManagerChangeListener

class ArchiveFragmentLayoutManagerListener(private val archiveFragment: ArchiveFragment) :
    LayoutManagerChangeListener {
    override fun onChange(isGrid: Boolean) {
        if (isGrid) {
            val gridLayoutManager = GridLayoutManager(archiveFragment.requireContext(), 2)
            archiveFragment.binding.archivedRv.layoutManager = gridLayoutManager
        } else {
            val linearLayoutManager = LinearLayoutManager(archiveFragment.requireContext())
            archiveFragment.binding.archivedRv.layoutManager = linearLayoutManager
        }
    }
}