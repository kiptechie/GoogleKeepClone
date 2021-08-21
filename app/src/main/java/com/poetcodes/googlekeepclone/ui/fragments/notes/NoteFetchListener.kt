package com.poetcodes.googlekeepclone.ui.fragments.notes

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.poetcodes.googlekeepclone.R
import com.poetcodes.googlekeepclone.repository.models.entities.Note
import com.poetcodes.googlekeepclone.repository.models.enums.CurrentFragment
import com.poetcodes.googlekeepclone.ui.view_models.MainViewModel
import com.poetcodes.googlekeepclone.utils.ConstantsUtil

class NoteFetchListener(
    private val fragment: Fragment,
    private val currentFragment: CurrentFragment
) :
    MainViewModel.NoteFetchListener {

    override fun onNoteFetch(note: Note) {
        fragment.requireActivity().runOnUiThread {
            val resId: Int = when (currentFragment) {
                CurrentFragment.IS_NOTES_FRAGMENT -> {
                    R.id.action_notesFragment_to_viewEditNoteFragment
                }
                CurrentFragment.IS_EDIT_ADD_LABELS_FRAGMENT -> {
                    R.id.action_editAddLabelsFragment_to_viewEditNoteFragment
                }
                CurrentFragment.IS_REMINDERS_FRAGMENT -> {
                    R.id.action_remindersFragment_to_viewEditNoteFragment
                }
            }
            val bundle = Bundle()
            bundle.putParcelable(ConstantsUtil.NOTE_EXTRA, note)
            Navigation.findNavController(fragment.requireView())
                .navigate(resId, bundle)
        }
    }

}