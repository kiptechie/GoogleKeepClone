package com.poetcodes.googlekeepclone.ui.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.blankj.utilcode.util.KeyboardUtils
import com.poetcodes.googlekeepclone.databinding.FragmentViewEditNoteBinding
import com.poetcodes.googlekeepclone.repository.models.NoteEssentials
import com.poetcodes.googlekeepclone.repository.models.entities.Note
import com.poetcodes.googlekeepclone.ui.activities.MainActivity
import com.poetcodes.googlekeepclone.ui.view_models.MainViewModel
import com.poetcodes.googlekeepclone.utils.ConstantsUtil
import com.poetcodes.googlekeepclone.utils.NoteEntityUtil

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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentViewEditNoteBinding.inflate(inflater, container, false)
        _mainActivity = requireActivity() as MainActivity
        mainActivity.showBottomBar(false)
        val bundle = arguments
        if (bundle != null) {
            mainActivity.getToolbar()?.title = "Edit Note"
            note = bundle.getParcelable(ConstantsUtil.NOTE_EXTRA)
        } else {
            mainActivity.getToolbar()?.title = "New Note"
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
            note!!.id?.let {
                mainViewModel.liveNote(it).observe(requireActivity(), { liveNote ->
                    note = liveNote
                })
            }
        }
    }

    private fun initEditTexts() {
        if (note != null) {
            binding.titleEd.setText(note!!.title)
            binding.contentEd.setText(note!!.description)
        }
        KeyboardUtils.showSoftInput(binding.contentEd)
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
                            title,
                            oldNote.description,
                            oldNote.createdAt,
                            System.currentTimeMillis().toString()
                        )
                        val noteEntityUtil = NoteEntityUtil.Builder()
                            .withNoteEssentials(noteEssentials)
                            .build()
                        currentFragment?.mainViewModel?.updateNote(note = noteEntityUtil.note)
                    } else {
                        val title: String = p0.toString()
                        val description = ""
                        val currentTime = System.currentTimeMillis().toString()
                        val createdAt = currentTime
                        val updatedAt = currentTime
                        val noteEssentials = NoteEssentials(
                            title,
                            description,
                            createdAt,
                            updatedAt
                        )
                        val noteEntityUtil = NoteEntityUtil.Builder()
                            .withNoteEssentials(noteEssentials)
                            .build()
                        currentFragment?.mainViewModel?.addNote(note = noteEntityUtil.note)
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
                            oldNote.title,
                            description,
                            oldNote.createdAt,
                            System.currentTimeMillis().toString()
                        )
                        val noteEntityUtil = NoteEntityUtil.Builder()
                            .withNoteEssentials(noteEssentials)
                            .build()
                        currentFragment?.mainViewModel?.updateNote(note = noteEntityUtil.note)
                    } else {
                        val title = ""
                        val description = p0.toString()
                        val currentTime = System.currentTimeMillis().toString()
                        val createdAt = currentTime
                        val updatedAt = currentTime
                        val noteEssentials = NoteEssentials(
                            title,
                            description,
                            createdAt,
                            updatedAt
                        )
                        val noteEntityUtil = NoteEntityUtil.Builder()
                            .withNoteEssentials(noteEssentials)
                            .build()
                        currentFragment?.mainViewModel?.addNote(note = noteEntityUtil.note)
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