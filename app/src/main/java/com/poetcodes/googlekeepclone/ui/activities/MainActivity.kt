package com.poetcodes.googlekeepclone.ui.activities

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.poetcodes.googlekeepclone.databinding.ActivityMainBinding
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

        subscribeObservers()
        mainViewModel.setStateEvent(MainStateEvent.NoteEvents)
        mainViewModel.setStateEvent(MainStateEvent.LabelEvents)
    }

    private fun subscribeObservers() {
        // TODO("Not yet implemented")
    }

}