package com.poetcodes.googlekeepclone.ui.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.KeyboardUtils
import com.poetcodes.googlekeepclone.R
import com.poetcodes.googlekeepclone.databinding.FragmentNotesBinding
import com.poetcodes.googlekeepclone.repository.DataState
import com.poetcodes.googlekeepclone.repository.models.NoteEssentials
import com.poetcodes.googlekeepclone.repository.models.entities.Note
import com.poetcodes.googlekeepclone.repository.models.enums.Entity
import com.poetcodes.googlekeepclone.ui.activities.MainActivity
import com.poetcodes.googlekeepclone.ui.adapters.NotesAdapter
import com.poetcodes.googlekeepclone.ui.adapters.interfaces.OnNoteClickListener
import com.poetcodes.googlekeepclone.ui.view_models.MainViewModel
import com.poetcodes.googlekeepclone.utils.ConstantsUtil
import com.poetcodes.googlekeepclone.utils.HelpersUtil
import com.poetcodes.googlekeepclone.utils.MyDifferUtil
import com.poetcodes.googlekeepclone.utils.NoteEntityUtil
import com.poetcodes.googlekeepclone.utils.comparators.NotesComparator
import java.util.*


/**
 * A simple [Fragment] subclass.
 * Use the [NotesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NotesFragment : Fragment(), OnNoteClickListener, MainActivity.OnBottomActionClickedListener {

    private var _binding: FragmentNotesBinding? = null
    private var _mainActivity: MainActivity? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!
    private val mainActivity get() = _mainActivity!!

    private val mainViewModel: MainViewModel by activityViewModels()
    private var notesAdapter: NotesAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotesBinding.inflate(inflater, container, false)
        _mainActivity = requireActivity() as MainActivity
        mainActivity.showBottomBar(true)
        mainActivity.setOnBottomActionCLickedListener(this)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        KeyboardUtils.hideSoftInput(requireView())
        setupRecycler()
        setUpObservers()
        mainActivity.loadEntity(Entity.NOTE)
    }

    private fun setupRecycler() {
        binding.notesRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.notesRecycler.setHasFixedSize(true)
        notesAdapter = NotesAdapter(MyDifferUtil.noteAsyncDifferConfig)
        binding.notesRecycler.adapter = notesAdapter
        notesAdapter?.setOnNoteClickListener(NoteAdapterClickListener(this))
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            mainViewModel.cleanNotes()
        }, 1000)
    }

    private class NoteAdapterClickListener(fragment: Fragment) : OnNoteClickListener {

        private val notesFragment = fragment

        override fun onNoteClick(note: Note, position: Int) {
            val bundle = Bundle()
            bundle.putParcelable(ConstantsUtil.NOTE_EXTRA, note)
            Navigation.findNavController(notesFragment.requireView())
                .navigate(R.id.action_notesFragment_to_viewEditNoteFragment, bundle)
        }

    }

    private fun showProgress(show: Boolean) {
        if (_binding != null) {
            if (show) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        }
    }

    private fun setUpObservers() {
        mainViewModel.notesDataState.observe(requireActivity(), { notesDataState ->
            when (notesDataState) {
                is DataState.Error -> {
                    showProgress(false)
                    if (_binding != null) {
                        HelpersUtil.showBottomSnack(binding.root, notesDataState.exception.message)
                    }
                }
                is DataState.Loading -> {
                    showProgress(true)
                    if (_binding != null) {
                        binding.noNotesTv.visibility = View.GONE
                    }
                }
                is DataState.Success -> {
                    showProgress(false)
                    val sortedList: List<Note> = notesDataState.data.sortedWith(NotesComparator())
                    notesAdapter?.submitList(sortedList)
                    if (_binding != null) {
                        if (notesDataState.data.isEmpty()) {
                            binding.noNotesTv.visibility = View.VISIBLE
                        } else {
                            binding.noNotesTv.visibility = View.GONE
                        }
                    }
                }
            }
        })
    }

    override fun onNoteClick(note: Note, position: Int) {
        val bundle = Bundle()
        bundle.putParcelable(ConstantsUtil.NOTE_EXTRA, note)
        Navigation.findNavController(requireView())
            .navigate(R.id.action_notesFragment_to_viewEditNoteFragment, bundle)
    }

    class NoteFetchListener(fragment: Fragment) : MainViewModel.NoteFetchListener {

        private val notesFragment = fragment

        override fun onNoteFetch(note: Note) {
            notesFragment.requireActivity().runOnUiThread {
                val bundle = Bundle()
                bundle.putParcelable(ConstantsUtil.NOTE_EXTRA, note)
                Navigation.findNavController(notesFragment.requireView())
                    .navigate(R.id.action_notesFragment_to_viewEditNoteFragment, bundle)
            }
        }

    }

    override fun onNewNoteClicked() {
        val title = ""
        val description = ""
        val currentTime = System.currentTimeMillis().toString()
        val createdAt = currentTime
        val updatedAt = currentTime
        val id = UUID.randomUUID().toString()
        val noteEssentials = NoteEssentials(
            id,
            title,
            description,
            createdAt,
            updatedAt
        )
        val noteEntityUtil = NoteEntityUtil.Builder()
            .withNoteEssentials(noteEssentials)
            .build()
        mainViewModel.fetchNote(noteEntityUtil.note, NoteFetchListener(this))
    }

    override fun onToDoClicked() {
        //TODO("Not yet implemented")
    }

    override fun onDrawingCanvasClicked() {
        //TODO("Not yet implemented")
    }

    override fun onMicClicked() {
        //TODO("Not yet implemented")
    }

    override fun onImageIconClicked() {
        //TODO("Not yet implemented")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        notesAdapter = null
        _mainActivity = null
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment NotesFragment.
         */
        @JvmStatic
        fun newInstance() = NotesFragment()
    }

}