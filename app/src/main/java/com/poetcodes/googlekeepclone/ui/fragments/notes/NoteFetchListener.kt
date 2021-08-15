package com.poetcodes.googlekeepclone.ui.fragments.notes

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.poetcodes.googlekeepclone.R
import com.poetcodes.googlekeepclone.repository.models.entities.Note
import com.poetcodes.googlekeepclone.repository.models.enums.MyFragment
import com.poetcodes.googlekeepclone.ui.view_models.MainViewModel
import com.poetcodes.googlekeepclone.utils.ConstantsUtil

class NoteFetchListener(fragment: Fragment, myFragment: MyFragment) :
    MainViewModel.NoteFetchListener {

    private val mFragment = fragment
    private val _myFragment = myFragment

    override fun onNoteFetch(note: Note) {
        mFragment.requireActivity().runOnUiThread {
            val bundle = Bundle()
            bundle.putParcelable(ConstantsUtil.NOTE_EXTRA, note)
            when (_myFragment) {
                MyFragment.NOTES_FRAGMENT -> {
                    Navigation.findNavController(mFragment.requireView())
                        .navigate(R.id.action_notesFragment_to_viewEditNoteFragment, bundle)
                }
                MyFragment.EDIT_ADD_LABELS_FRAGMENT -> {
                    Navigation.findNavController(mFragment.requireView())
                        .navigate(R.id.action_editAddLabelsFragment_to_viewEditNoteFragment, bundle)
                }
                MyFragment.REMINDERS_FRAGMENT -> {
                    Navigation.findNavController(mFragment.requireView())
                        .navigate(R.id.action_remindersFragment_to_viewEditNoteFragment, bundle)
                }
            }
        }
    }

}