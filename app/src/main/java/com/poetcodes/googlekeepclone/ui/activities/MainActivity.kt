package com.poetcodes.googlekeepclone.ui.activities

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.selection.SelectionTracker
import com.google.android.material.appbar.MaterialToolbar
import com.poetcodes.googlekeepclone.R
import com.poetcodes.googlekeepclone.databinding.ActivityMainBinding
import com.poetcodes.googlekeepclone.repository.models.entities.Note
import com.poetcodes.googlekeepclone.repository.models.enums.Entity
import com.poetcodes.googlekeepclone.ui.MainStateEvent
import com.poetcodes.googlekeepclone.ui.fragments.notes.OnBottomActionClickedListener
import com.poetcodes.googlekeepclone.ui.view_models.MainViewModel
import com.poetcodes.googlekeepclone.utils.ConstantsUtil
import dagger.hilt.android.AndroidEntryPoint
import io.paperdb.Paper


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModels()
    private var onBottomActionClickedListener: OnBottomActionClickedListener? = null
    private var materialToolbar: MaterialToolbar? = null
    private var layoutManagerChangeListener: LayoutManagerChangeListener? = null
    private var normalTracker: SelectionTracker<Long>? = null
    private var pinnedTracker: SelectionTracker<Long>? = null
    private var selectedNotes: List<Note>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        materialToolbar = binding.materialToolbar
        materialToolbar!!.inflateMenu(R.menu.main_menu)
        setSupportActionBar(materialToolbar)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        val topLevelDestinations: MutableSet<Int> = HashSet()
        topLevelDestinations.add(R.id.notesFragment)
        topLevelDestinations.add(R.id.remindersFragment)
        topLevelDestinations.add(R.id.archiveFragment)
        topLevelDestinations.add(R.id.deletedFragment)
        val appBarConfiguration = AppBarConfiguration(topLevelDestinations, binding.drawerLayout)
        binding.materialToolbar.setupWithNavController(navController, appBarConfiguration)
        binding.navView.setupWithNavController(navController)

        setClickListeners()

        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            invalidateOptionsMenu()
        }, 500)
        setSelectionToolbarClickListeners()
    }

    private fun setSelectionToolbarClickListeners() {
        binding.noteSelectionToolbarHolder.cancelSelection.setOnClickListener {
            // cancel all selections
            clearAllSelections()
        }
        binding.noteSelectionToolbarHolder.colorBtn.setOnClickListener {
            // set colors
        }
        binding.noteSelectionToolbarHolder.labelBtn.setOnClickListener {
            // add labels
        }
        binding.noteSelectionToolbarHolder.moreOptionsBtn.setOnClickListener {
            // show more options
        }
        binding.noteSelectionToolbarHolder.pinBtn.setOnClickListener {
            // pin notes
            if (selectedNotes != null) {
                mainViewModel.pinNotes(selectedNotes!!)
                clearAllSelections()
            }
        }
    }

    private fun clearAllSelections() {
        normalTracker?.clearSelection()
        pinnedTracker?.clearSelection()
    }

    fun getBinding(): ActivityMainBinding {
        return binding
    }

    fun setNormalSelectionTracker(normalTracker: SelectionTracker<Long>) {
        this.normalTracker = normalTracker
    }

    fun setSelectedNotes(notes: List<Note>) {
        selectedNotes = notes
    }

    fun setPinnedSelectionTracker(pinnedTracker: SelectionTracker<Long>) {
        this.pinnedTracker = pinnedTracker
    }

    fun updateSelectionCount(count: Int) {
        binding.noteSelectionToolbarHolder.selectionCountTv.text = count.toString()
    }

    fun showSelectionToolBar(show: Boolean) {
        if (show) {
            binding.noteSelectionToolbarHolder.root.visibility = View.VISIBLE
        } else {
            binding.noteSelectionToolbarHolder.root.visibility = View.GONE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        if (menu != null) {
            val menuItem: MenuItem? = menu.findItem(R.id.actionSwitchLayoutManager)
            changeMenuIcon(menuItem)
            return true
        }
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.actionSwitchLayoutManager -> {
                switchNotesLayoutManager()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun changeMenuIcon(menuItem: MenuItem?) {
        val isGrid = Paper.book().read(ConstantsUtil.IS_GRID_PAPER_BOOK, true)
        if (isGrid) {
            menuItem?.setIcon(R.drawable.ic_outline_view_agenda_24)
        } else {
            menuItem?.setIcon(R.drawable.ic_outline_dashboard_24)
        }

    }

    fun setLayoutManagerListener(layoutManagerChangeListener: LayoutManagerChangeListener?) {
        this.layoutManagerChangeListener = layoutManagerChangeListener
    }

    private fun switchNotesLayoutManager() {
        val isGrid = Paper.book().read(ConstantsUtil.IS_GRID_PAPER_BOOK, true)
        Paper.book().write(ConstantsUtil.IS_GRID_PAPER_BOOK, !isGrid)
        layoutManagerChangeListener?.onChange(!isGrid)
        invalidateOptionsMenu()
    }

    fun getToolbar(): MaterialToolbar? {
        return materialToolbar
    }

    private fun setClickListeners() {
        binding.newNoteFab.setOnClickListener {
            onBottomActionClickedListener?.onNewNoteClicked()
        }
    }

    fun loadEntity(entity: Entity) {
        runOnUiThread {
            when (entity) {
                Entity.ARCHIVE -> mainViewModel.setStateEvent(MainStateEvent.ArchiveEvents)
                Entity.DRAFT -> mainViewModel.setStateEvent(MainStateEvent.DraftEvents)
                Entity.LABEL -> mainViewModel.setStateEvent(MainStateEvent.LabelEvents)
                Entity.NOTE -> mainViewModel.setStateEvent(MainStateEvent.NoteEvents)
                Entity.TRASH -> mainViewModel.setStateEvent(MainStateEvent.TrashEvents)
            }
        }
    }

    fun showBottomBar(show: Boolean) {
        runOnUiThread {
            if (show) {
                binding.bottomAppBar.visibility = View.VISIBLE
                binding.newNoteFab.visibility = View.VISIBLE
            } else {
                binding.newNoteFab.visibility = View.GONE
                binding.bottomAppBar.visibility = View.GONE
            }
        }
    }

    fun setOnBottomActionCLickedListener(listener: OnBottomActionClickedListener) {
        onBottomActionClickedListener = listener
    }

}