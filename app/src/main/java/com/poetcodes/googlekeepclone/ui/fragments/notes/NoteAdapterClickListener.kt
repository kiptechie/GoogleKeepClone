package com.poetcodes.googlekeepclone.ui.fragments.notes

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.poetcodes.googlekeepclone.R
import com.poetcodes.googlekeepclone.repository.models.entities.Note
import com.poetcodes.googlekeepclone.ui.adapters.interfaces.OnNoteClickListener
import com.poetcodes.googlekeepclone.utils.ConstantsUtil

class NoteAdapterClickListener(fragment: Fragment) : OnNoteClickListener {

    private val notesFragment = fragment

    override fun onNoteClick(note: Note, position: Int) {
        val bundle = Bundle()
        bundle.putParcelable(ConstantsUtil.NOTE_EXTRA, note)
        Navigation.findNavController(notesFragment.requireView())
            .navigate(R.id.action_notesFragment_to_viewEditNoteFragment, bundle)
    }

}