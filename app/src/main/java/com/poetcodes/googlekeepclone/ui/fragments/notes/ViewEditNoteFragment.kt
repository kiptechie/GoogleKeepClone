package com.poetcodes.googlekeepclone.ui.fragments.notes

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.blankj.utilcode.util.KeyboardUtils
import com.blankj.utilcode.util.ToastUtils
import com.poetcodes.googlekeepclone.R
import com.poetcodes.googlekeepclone.databinding.FragmentViewEditNoteBinding
import com.poetcodes.googlekeepclone.repository.models.NoteEssentials
import com.poetcodes.googlekeepclone.repository.models.entities.Archive
import com.poetcodes.googlekeepclone.repository.models.entities.Note
import com.poetcodes.googlekeepclone.ui.activities.MainActivity
import com.poetcodes.googlekeepclone.ui.fragments.archives.ArchiveAdded
import com.poetcodes.googlekeepclone.ui.view_models.MainViewModel
import com.poetcodes.googlekeepclone.utils.ConstantsUtil
import com.poetcodes.googlekeepclone.utils.HelpersUtil
import com.poetcodes.googlekeepclone.utils.NoteEntityUtil
import io.sentry.Sentry
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
    private lateinit var note: Note
    private var isFromArchive: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        val bundle = arguments
        if (bundle != null) {
            isFromArchive = bundle.getBoolean(ConstantsUtil.FROM_ARCHIVES, false)
            note = bundle.getParcelable(ConstantsUtil.NOTE_EXTRA)!!
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
        inflater.inflate(R.menu.edit_note_menu, menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        val archiveNoteItem: MenuItem? = menu.findItem(R.id.actionArchive)
        if (isFromArchive) {
            archiveNoteItem?.setIcon(R.drawable.ic_outline_unarchive_24)
        } else {
            archiveNoteItem?.setIcon(R.drawable.ic_outline_archive_24)
        }
        try {
            // if note pinned set filled icon
            val pinNoteItem: MenuItem? = menu.findItem(R.id.actionPin)
            if (note.isPinned) {
                pinNoteItem?.setIcon(R.drawable.ic_baseline_push_pin_filled_24)
            } else {
                pinNoteItem?.setIcon(R.drawable.ic_outline_push_pin_24)
            }
        } catch (e: Exception) {
            Timber.e(e)
            Sentry.captureException(e)
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
        if (isFromArchive) {
            mainViewModel.unarchive(note)
            var msg = "Note"
            if (note.title != "") {
                msg = note.title
            }
            ToastUtils.showShort("$msg Unarchived!")
            requireActivity().onBackPressed()
        } else {
            val archive = Archive(
                null,
                note = this.note
            )
            mainViewModel.addArchive(archive, ArchiveAdded(this))
        }
    }

    private fun initReminder() {
        if (isFromArchive) {
            ToastUtils.showShort("Unarchive note first!")
        } else {
            //TODO("Not yet implemented")
        }
    }

    private fun pinNote() {
        if (isFromArchive) {
            ToastUtils.showShort("Unarchive note first!")
        } else {
            mainViewModel.pinNote(note)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentViewEditNoteBinding.inflate(inflater, container, false)
        _mainActivity = requireActivity() as MainActivity
        mainActivity.showBottomBar(false)
        if (HelpersUtil.isNewNote(note)) {
            mainActivity.getToolbar()?.title = "New Note"
        } else {
            mainActivity.getToolbar()?.title = "Edit Note"
        }
        requireActivity().invalidateOptionsMenu()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initEditTexts()
        initObservers()
    }

    private fun initObservers() {
        note.id.let {
            mainViewModel.liveNote(it).observe(requireActivity(), { liveNote ->
                try {
                    note = liveNote
                    requireActivity().invalidateOptionsMenu()
                } catch (e: Exception) {
                    Timber.e(e)
                    Sentry.captureException(e)
                }
            })
        }
    }

    private fun initEditTexts() {
        binding.titleEd.setText(note.title)
        binding.contentEd.setText(note.description)
        if (HelpersUtil.isNewNote(note)) {
            KeyboardUtils.showSoftInput(binding.contentEd)
        }
        setUpTextWatchers()
    }

    private fun setUpTextWatchers() {
        binding.titleEd.addTextChangedListener(TitleWatcher(this))
        binding.contentEd.addTextChangedListener(ContentWatcher(this))
    }

    private class TitleWatcher(private val currentFragment: ViewEditNoteFragment) : TextWatcher {

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun afterTextChanged(p0: Editable?) {
            if (p0 != null) {
                if (p0.isNotEmpty()) {
                    val oldNote: Note = currentFragment.note
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
                    if (currentFragment.isFromArchive) {
                        currentFragment.mainViewModel.updateArchive(note = noteEntityUtil.note)
                    } else {
                        currentFragment.mainViewModel.updateNote(note = noteEntityUtil.note)
                    }
                }
            }
        }

    }

    private class ContentWatcher(private val currentFragment: ViewEditNoteFragment) : TextWatcher {


        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun afterTextChanged(p0: Editable?) {
            if (p0 != null) {
                if (p0.isNotEmpty()) {
                    val oldNote: Note = currentFragment.note
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
                    if (currentFragment.isFromArchive) {
                        currentFragment.mainViewModel.updateArchive(note = noteEntityUtil.note)
                    } else {
                        currentFragment.mainViewModel.updateNote(note = noteEntityUtil.note)
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
        binding.titleEd.removeTextChangedListener(TitleWatcher(this))
        binding.contentEd.removeTextChangedListener(ContentWatcher(this))
        _binding = null
        _mainActivity = null
    }
}