package com.poetcodes.googlekeepclone.ui.fragments.archives

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.KeyboardUtils
import com.poetcodes.googlekeepclone.databinding.FragmentArchiveBinding
import com.poetcodes.googlekeepclone.repository.DataState
import com.poetcodes.googlekeepclone.repository.models.entities.Note
import com.poetcodes.googlekeepclone.repository.models.enums.CurrentFragment
import com.poetcodes.googlekeepclone.repository.models.enums.Entity
import com.poetcodes.googlekeepclone.ui.activities.MainActivity
import com.poetcodes.googlekeepclone.ui.adapters.notes.NotesAdapter
import com.poetcodes.googlekeepclone.ui.adapters.notes.NoteAdapterClickListener
import com.poetcodes.googlekeepclone.ui.fragments.notes.NoteFetchListener
import com.poetcodes.googlekeepclone.ui.fragments.notes.OnBottomActionClickedListener
import com.poetcodes.googlekeepclone.ui.view_models.MainViewModel
import com.poetcodes.googlekeepclone.utils.ConstantsUtil
import com.poetcodes.googlekeepclone.utils.HelpersUtil
import com.poetcodes.googlekeepclone.utils.MyDifferUtil
import com.poetcodes.googlekeepclone.utils.comparators.NotesComparator
import io.paperdb.Paper

/**
 * A simple [Fragment] subclass.
 * Use the [ArchiveFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ArchiveFragment : Fragment(), OnBottomActionClickedListener {

    private var _binding: FragmentArchiveBinding? = null
    private var _mainActivity: MainActivity? = null

    // This property is only valid between onCreateView and onDestroyView.
    val binding get() = _binding!!
    private val mainActivity get() = _mainActivity!!

    private val mainViewModel: MainViewModel by activityViewModels()
    private var normalNotesAdapter: NotesAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentArchiveBinding.inflate(inflater, container, false)
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
        mainActivity.loadEntity(Entity.ARCHIVE)
    }

    private fun setupRecycler() {
        val isGrid = Paper.book().read(ConstantsUtil.IS_GRID_PAPER_BOOK, true)
        if (isGrid) {
            val gridLayoutManager = GridLayoutManager(requireContext(), 2)
            binding.archivedRv.layoutManager = gridLayoutManager
        } else {
            val linearLayoutManager = LinearLayoutManager(requireContext())
            binding.archivedRv.layoutManager = linearLayoutManager
        }
        normalNotesAdapter = NotesAdapter(MyDifferUtil.noteAsyncDifferConfig)
        binding.archivedRv.adapter = normalNotesAdapter
        normalNotesAdapter?.setOnNoteClickListener(NoteAdapterClickListener(this, true))
        mainActivity.setLayoutManagerListener(ArchiveFragmentLayoutManagerListener(this))
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
        mainViewModel.archivesDataState.observe(requireActivity(), { archivesDataState ->
            when (archivesDataState) {
                is DataState.Error -> {
                    showProgress(false)
                    if (_binding != null) {
                        HelpersUtil.showBottomSnack(
                            binding.root,
                            archivesDataState.exception.message
                        )
                    }
                }
                is DataState.Loading -> {
                    showProgress(true)
                    if (_binding != null) {
                        binding.noArchivesYet.visibility = View.GONE
                    }
                }
                is DataState.Success -> {
                    if (_binding != null) {
                        showProgress(false)
                        mainViewModel.executorsInstance().withMultipleThreads().submit {
                            val normalNotes: ArrayList<Note> = ArrayList()
                            for (archive in archivesDataState.data) {
                                normalNotes.add(archive.note)
                            }
                            val sortedArchives: List<Note> =
                                normalNotes.sortedWith(NotesComparator())
                            requireActivity().runOnUiThread {
                                normalNotesAdapter?.submitList(sortedArchives)
                                if (archivesDataState.data.isEmpty()) {
                                    binding.noArchivesYet.visibility = View.VISIBLE
                                } else {
                                    binding.noArchivesYet.visibility = View.GONE
                                }
                            }
                        }
                    }
                }
            }
        })
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment ArchiveFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() = ArchiveFragment()
    }

    override fun onNewNoteClicked() {
        mainViewModel.fetchNote(
            HelpersUtil.newNote(),
            NoteFetchListener(this, CurrentFragment.IS_ARCHIVE_FRAGMENT)
        )
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

}