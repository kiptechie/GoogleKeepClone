package com.poetcodes.googlekeepclone.ui.fragments.notes

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.blankj.utilcode.util.KeyboardUtils
import com.poetcodes.googlekeepclone.R
import com.poetcodes.googlekeepclone.databinding.FragmentViewEditNoteBinding
import com.poetcodes.googlekeepclone.repository.models.NoteEssentials
import com.poetcodes.googlekeepclone.repository.models.entities.Note
import com.poetcodes.googlekeepclone.ui.activities.MainActivity
import com.poetcodes.googlekeepclone.ui.view_models.MainViewModel
import com.poetcodes.googlekeepclone.utils.ConstantsUtil
import com.poetcodes.googlekeepclone.utils.HelpersUtil
import com.poetcodes.googlekeepclone.utils.NoteEntityUtil
import timber.log.Timber

/**
 * A simple [Fragment] subclass.
 * Use the [ViewEditNoteFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ViewEditNoteFragment : Fragment() {

    private var _binding: FragmentViewEditNoteBinding? = null
    private var _mainActivity: MainActivity? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!
    private val mainActivity get() = _mainActivity!!

    private val mainViewModel: MainViewModel by activityViewModels()
    private var note: Note? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
        inflater.inflate(R.menu.edit_note_menu, menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        try {
            // if note pinned set filled icon
            val pinNoteItem: MenuItem? = menu.findItem(R.id.actionPin)
            if (note?.isPinned!!) {
                pinNoteItem?.setIcon(R.drawable.ic_baseline_push_pin_filled_24)
            } else {
                pinNoteItem?.setIcon(R.drawable.ic_outline_push_pin_24)
            }
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.actionPin -> {
                pinNote()
                true
            }
            R.id.actionReminder -> {
                initReminder()
                true
            }
            R.id.actionArchive -> {
                archiveNote()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun archiveNote() {
        //TODO("Not yet implemented")
    }

    private fun initReminder() {
        //TODO("Not yet implemented")
    }

    private fun pinNote() {
        mainViewModel.pinNote(note)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentViewEditNoteBinding.inflate(inflater, container, false)
        _mainActivity = requireActivity() as MainActivity
        mainActivity.showBottomBar(false)
        val bundle = arguments
        if (bundle != null) {
            note = bundle.getParcelable(ConstantsUtil.NOTE_EXTRA)
            if (HelpersUtil.isNewNote(note)) {
                mainActivity.getToolbar()?.title = "New Note"
            } else {
                mainActivity.getToolbar()?.title = "Edit Note"
            }
            requireActivity().invalidateOptionsMenu()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initEditTexts()
        initObservers()
    }

    private fun initObservers() {
        if (note != null) {
            note!!.id.let {
                mainViewModel.liveNote(it).observe(requireActivity(), { liveNote ->
                    note = liveNote
                    try {
                        requireActivity().invalidateOptionsMenu()
                    } catch (ignore: Exception) {

                    }
                })
            }
        }
    }

    private fun initEditTexts() {
        if (note != null) {
            binding.titleEd.setText(note!!.title)
            binding.contentEd.setText(note!!.description)
        }
        if (HelpersUtil.isNewNote(note)) {
            KeyboardUtils.showSoftInput(binding.contentEd)
        }
        setUpTextWatchers()
    }

    private fun setUpTextWatchers() {
        binding.titleEd.addTextChangedListener(TitleWatcher(this))
        binding.contentEd.addTextChangedListener(ContentWatcher(this))
    }

    private class TitleWatcher(fragment: ViewEditNoteFragment?) : TextWatcher {

        private val currentFragment = fragment

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun afterTextChanged(p0: Editable?) {
            if (p0 != null) {
                if (p0.isNotEmpty()) {
                    val oldNote: Note? = currentFragment?.note
                    if (oldNote != null) {
                        val title: String = if (oldNote.title != p0.toString()) {
                            p0.toString()
                        } else {
                            oldNote.title
                        }
                        val noteEssentials = NoteEssentials(
                            oldNote.id,
                            title,
                            oldNote.description,
                            oldNote.createdAt,
                            System.currentTimeMillis().toString(),
                            oldNote.isPinned
                        )
                        val noteEntityUtil = NoteEntityUtil.Builder()
                            .withNoteEssentials(noteEssentials)
                            .build()
                        currentFragment?.mainViewModel?.updateNote(note = noteEntityUtil.note)
                    }
                }
            }
        }

    }

    private class ContentWatcher(fragment: ViewEditNoteFragment?) : TextWatcher {

        private val currentFragment = fragment

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun afterTextChanged(p0: Editable?) {
            if (p0 != null) {
                if (p0.isNotEmpty()) {
                    val oldNote: Note? = currentFragment?.note
                    if (oldNote != null) {
                        val description: String = if (oldNote.title != p0.toString()) {
                            p0.toString()
                        } else {
                            oldNote.title
                        }
                        val noteEssentials = NoteEssentials(
                            oldNote.id,
                            oldNote.title,
                            description,
                            oldNote.createdAt,
                            System.currentTimeMillis().toString(),
                            oldNote.isPinned
                        )
                        val noteEntityUtil = NoteEntityUtil.Builder()
                            .withNoteEssentials(noteEssentials)
                            .build()
                        currentFragment?.mainViewModel?.updateNote(note = noteEntityUtil.note)
                    }
                }
            }
        }

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment ViewEditNoteFragment.
         */
        @JvmStatic
        fun newInstance() = ViewEditNoteFragment()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.titleEd.removeTextChangedListener(TitleWatcher(null))
        binding.contentEd.removeTextChangedListener(ContentWatcher(null))
        _binding = null
        _mainActivity = null
    }
}