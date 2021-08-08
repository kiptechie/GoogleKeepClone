package com.poetcodes.googlekeepclone.ui.activities

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.appbar.MaterialToolbar
import com.poetcodes.googlekeepclone.databinding.ActivityMainBinding
import com.poetcodes.googlekeepclone.repository.models.enums.Entity
import com.poetcodes.googlekeepclone.ui.MainStateEvent
import com.poetcodes.googlekeepclone.ui.view_models.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModels()
    private var onBottomActionClickedListener: OnBottomActionClickedListener? = null
    private var materialToolbar: MaterialToolbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val navHostFragment =
            supportFragmentManager.findFragmentById(com.poetcodes.googlekeepclone.R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        val topLevelDestinations: MutableSet<Int> = HashSet()
        topLevelDestinations.add(com.poetcodes.googlekeepclone.R.id.notesFragment)
        topLevelDestinations.add(com.poetcodes.googlekeepclone.R.id.remindersFragment)
        topLevelDestinations.add(com.poetcodes.googlekeepclone.R.id.editAddLabelsFragment)
        topLevelDestinations.add(com.poetcodes.googlekeepclone.R.id.archiveFragment)
        topLevelDestinations.add(com.poetcodes.googlekeepclone.R.id.deletedFragment)
        val appBarConfiguration = AppBarConfiguration(topLevelDestinations, binding.drawerLayout)
        binding.materialToolbar.setupWithNavController(navController, appBarConfiguration)
        binding.navView.setupWithNavController(navController)
        materialToolbar = binding.materialToolbar

        setClickListeners()
    }

    fun getToolbar() : MaterialToolbar? {
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

    fun setOnBottomActionCLickedListener (listener: OnBottomActionClickedListener) {
        onBottomActionClickedListener = listener
    }

    interface OnBottomActionClickedListener {
        fun onNewNoteClicked()
        fun onToDoClicked()
        fun onDrawingCanvasClicked()
        fun onMicClicked()
        fun onImageIconClicked()
    }

}