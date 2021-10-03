package com.poetcodes.googlekeepclone.ui.adapters.notes

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.poetcodes.googlekeepclone.R
import com.poetcodes.googlekeepclone.repository.models.entities.Note
import com.poetcodes.googlekeepclone.utils.ConstantsUtil

class NoteAdapterClickListener(
    private val fragment: Fragment,
    private val fromArchives: Boolean
) : OnNoteClickListener {

    override fun onNoteClick(note: Note, position: Int) {
        val bundle = Bundle()
        bundle.putParcelable(ConstantsUtil.NOTE_EXTRA, note)
        bundle.putBoolean(ConstantsUtil.FROM_ARCHIVES, fromArchives)
        val id = if (fromArchives) {
            R.id.action_archiveFragment_to_viewEditNoteFragment
        } else {
            R.id.action_notesFragment_to_viewEditNoteFragment
        }
        Navigation.findNavController(fragment.requireView())
            .navigate(id, bundle)
    }

}