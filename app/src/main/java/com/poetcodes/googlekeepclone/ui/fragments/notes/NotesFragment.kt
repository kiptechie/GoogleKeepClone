package com.poetcodes.googlekeepclone.ui.fragments.notes

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.KeyboardUtils
import com.google.android.material.snackbar.Snackbar
import com.poetcodes.googlekeepclone.R
import com.poetcodes.googlekeepclone.databinding.FragmentNotesBinding
import com.poetcodes.googlekeepclone.repository.DataState
import com.poetcodes.googlekeepclone.repository.models.entities.Archive
import com.poetcodes.googlekeepclone.repository.models.entities.Note
import com.poetcodes.googlekeepclone.repository.models.enums.CurrentFragment
import com.poetcodes.googlekeepclone.repository.models.enums.Entity
import com.poetcodes.googlekeepclone.ui.activities.MainActivity
import com.poetcodes.googlekeepclone.ui.adapters.notes.*
import com.poetcodes.googlekeepclone.ui.view_models.MainViewModel
import com.poetcodes.googlekeepclone.utils.ConstantsUtil
import com.poetcodes.googlekeepclone.utils.HelpersUtil
import com.poetcodes.googlekeepclone.utils.MyDifferUtil
import com.poetcodes.googlekeepclone.utils.comparators.NotesComparator
import io.paperdb.Paper


/**
 * A simple [Fragment] subclass.
 * Use the [NotesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NotesFragment : Fragment(), OnNoteClickListener, OnBottomActionClickedListener {

    private var _binding: FragmentNotesBinding? = null
    private var _mainActivity: MainActivity? = null

    // This property is only valid between onCreateView and onDestroyView.
    val binding get() = _binding!!
    private val mainActivity get() = _mainActivity!!

    private val mainViewModel: MainViewModel by activityViewModels()
    private var normalNotesAdapter: NotesAdapter? = null
    private var pinnedNotesAdapter: NotesAdapter? = null

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

    fun onNoteSwipe(note: Note) {
        val archive = Archive(
            null,
            note
        )
        mainViewModel.addArchive(archive)
        val snackbar: Snackbar = Snackbar.make(
            binding.root, "Note archived!",
            Snackbar.LENGTH_LONG
        )
        snackbar.setAction("Undo") { mainViewModel.unarchive(note) }
        snackbar.show()
    }

    private fun setupRecycler() {
        val isGrid = Paper.book().read(ConstantsUtil.IS_GRID_PAPER_BOOK, true)
        if (isGrid) {
            val pinnedGridLayoutManager = GridLayoutManager(requireContext(), 2)
            val normalGridLayoutManager = GridLayoutManager(requireContext(), 2)
            binding.pinnedNotesRecycler.layoutManager = pinnedGridLayoutManager
            binding.notesRecycler.layoutManager = normalGridLayoutManager
        } else {
            val pinnedLinearLayoutManager = LinearLayoutManager(requireContext())
            val normalLinearLayoutManager = LinearLayoutManager(requireContext())
            binding.pinnedNotesRecycler.layoutManager = pinnedLinearLayoutManager
            binding.notesRecycler.layoutManager = normalLinearLayoutManager
        }
        normalNotesAdapter = NotesAdapter(MyDifferUtil.noteAsyncDifferConfig)
        pinnedNotesAdapter = NotesAdapter(MyDifferUtil.noteAsyncDifferConfig)
        binding.notesRecycler.adapter = normalNotesAdapter
        binding.pinnedNotesRecycler.adapter = pinnedNotesAdapter
        val normalItemTouchHelper = ItemTouchHelper(SwipeToArchiveCallback(normalNotesAdapter))
        val pinnedItemTouchHelper = ItemTouchHelper(SwipeToArchiveCallback(pinnedNotesAdapter))
        normalItemTouchHelper.attachToRecyclerView(binding.notesRecycler)
        pinnedItemTouchHelper.attachToRecyclerView(binding.pinnedNotesRecycler)
        val normalTracker = SelectionTracker.Builder(
            "normalSelection",
            binding.notesRecycler,
            MyNotesItemKeyProvider(binding.notesRecycler),
            MyNotesDetailLookup(binding.notesRecycler),
            StorageStrategy.createLongStorage()
        ).withSelectionPredicate(
            SelectionPredicates.createSelectAnything()
        ).build()
        mainActivity.setNormalSelectionTracker(normalTracker)
        normalNotesAdapter?.setItemSelectionTracker(normalTracker)
        val pinnedTracker = SelectionTracker.Builder(
            "pinnedSelection",
            binding.pinnedNotesRecycler,
            MyNotesItemKeyProvider(binding.pinnedNotesRecycler),
            MyNotesDetailLookup(binding.pinnedNotesRecycler),
            StorageStrategy.createLongStorage()
        ).withSelectionPredicate(
            SelectionPredicates.createSelectAnything()
        ).build()
        mainActivity.setPinnedSelectionTracker(pinnedTracker)
        pinnedNotesAdapter?.setItemSelectionTracker(pinnedTracker)
        itemSelectionTrackerObservers(normalTracker, pinnedTracker)
        pinnedNotesAdapter?.setOnNoteClickListener(NoteAdapterClickListener(this, false))
        pinnedNotesAdapter?.setOnNoteSwipeListener(NoteAdapterSwipeListener(this))
        normalNotesAdapter?.setOnNoteClickListener(NoteAdapterClickListener(this, false))
        normalNotesAdapter?.setOnNoteSwipeListener(NoteAdapterSwipeListener(this))
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            mainViewModel.cleanNotes()
        }, 1000)
        mainActivity.setLayoutManagerListener(NotesFragmentLayoutManagerListener(this))
    }

    private fun itemSelectionTrackerObservers(
        normalTracker: SelectionTracker<Long>,
        pinnedTracker: SelectionTracker<Long>
    ) {
        normalTracker.addObserver(
            object : SelectionTracker.SelectionObserver<Long>() {
                override fun onSelectionChanged() {
                    super.onSelectionChanged()
                    val itemsSelected = normalTracker.selection.size()
                    mainActivity.showSelectionToolBar(itemsSelected > 0)
                    mainActivity.updateSelectionCount(itemsSelected)
                    mainActivity.setSelectedNotes(normalNotesAdapter!!.getSelectedNotes())
                }
            })
        pinnedTracker.addObserver(
            object : SelectionTracker.SelectionObserver<Long>() {
                override fun onSelectionChanged() {
                    super.onSelectionChanged()
                    val itemsSelected = pinnedTracker.selection.size()
                    mainActivity.showSelectionToolBar(itemsSelected > 0)
                    mainActivity.updateSelectionCount(itemsSelected)
                    mainActivity.setSelectedNotes(normalNotesAdapter!!.getSelectedNotes())
                }
            })

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
                    if (_binding != null) {
                        showProgress(false)
                        mainViewModel.executorsInstance().withMultipleThreads().submit {
                            val pinnedNotes: ArrayList<Note> = ArrayList()
                            val normalNotes: ArrayList<Note> = ArrayList()
                            for (note in notesDataState.data) {
                                if (note.isPinned) {
                                    pinnedNotes.add(note)
                                } else {
                                    normalNotes.add(note)
                                }
                            }
                            val sortedPinnedNotes: List<Note> =
                                pinnedNotes.sortedWith(NotesComparator())
                            val sortedNormalNotes: List<Note> =
                                normalNotes.sortedWith(NotesComparator())
                            requireActivity().runOnUiThread {
                                if (sortedPinnedNotes.isEmpty()) {
                                    binding.pinnedSubheadTv.visibility = View.GONE
                                    binding.pinnedNotesRecycler.visibility = View.GONE
                                    binding.subheadTv.visibility = View.GONE
                                } else {
                                    binding.pinnedSubheadTv.visibility = View.VISIBLE
                                    binding.pinnedNotesRecycler.visibility = View.VISIBLE
                                    binding.subheadTv.visibility = View.VISIBLE
                                }
                                pinnedNotesAdapter?.submitList(sortedPinnedNotes)
                                normalNotesAdapter?.submitList(sortedNormalNotes)
                                if (notesDataState.data.isEmpty()) {
                                    binding.noNotesTv.visibility = View.VISIBLE
                                } else {
                                    binding.noNotesTv.visibility = View.GONE
                                }
                            }
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

    override fun onNewNoteClicked() {
        mainViewModel.fetchNote(
            HelpersUtil.newNote(),
            NoteFetchListener(this, CurrentFragment.IS_NOTES_FRAGMENT)
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

    override fun onDestroyView() {
        super.onDestroyView()
        mainActivity.setLayoutManagerListener(null)
        _binding = null
        normalNotesAdapter = null
        _mainActivity = null
    }

}