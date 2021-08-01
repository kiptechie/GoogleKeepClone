package com.poetcodes.googlekeepclone.ui.activities

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.poetcodes.googlekeepclone.databinding.ActivityMainBinding
import com.poetcodes.googlekeepclone.repository.DataState
import com.poetcodes.googlekeepclone.ui.MainStateEvent
import com.poetcodes.googlekeepclone.ui.view_models.MainViewModel
import dagger.hilt.android.AndroidEntryPoint




@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModels()

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

        subscribeObservers()
        mainViewModel.setStateEvent(MainStateEvent.NoteEvents)
        mainViewModel.setStateEvent(MainStateEvent.LabelEvents)
    }

    private fun subscribeObservers() {
        mainViewModel.labelDataState.observe(this, { labelDataState ->
            when (labelDataState) {
                is DataState.Error -> {
                    showToast("Error loading Labels!")
                }
                is DataState.Loading -> {
                    // do nothing
                }
                is DataState.Success -> {
                    val labels = labelDataState.data
                    if (labels.isNotEmpty()) {
                        //
                    }
                }
            }
        })
    }

    private fun showToast(message: String?) {
        if (message == null) {
            return
        }
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

}