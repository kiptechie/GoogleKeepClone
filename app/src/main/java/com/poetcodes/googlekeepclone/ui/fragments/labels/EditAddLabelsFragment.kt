package com.poetcodes.googlekeepclone.ui.fragments.labels

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.KeyboardUtils
import com.blankj.utilcode.util.ToastUtils
import com.poetcodes.googlekeepclone.R
import com.poetcodes.googlekeepclone.databinding.FragmentEditAddLabelsBinding
import com.poetcodes.googlekeepclone.repository.DataState
import com.poetcodes.googlekeepclone.repository.models.entities.Label
import com.poetcodes.googlekeepclone.repository.models.enums.Entity
import com.poetcodes.googlekeepclone.repository.models.enums.MyFragment
import com.poetcodes.googlekeepclone.ui.activities.MainActivity
import com.poetcodes.googlekeepclone.ui.adapters.LabelsAdapter
import com.poetcodes.googlekeepclone.ui.fragments.notes.NoteFetchListener
import com.poetcodes.googlekeepclone.ui.fragments.notes.OnBottomActionClickedListener
import com.poetcodes.googlekeepclone.ui.view_models.MainViewModel
import com.poetcodes.googlekeepclone.utils.HelpersUtil
import com.poetcodes.googlekeepclone.utils.MyDifferUtil

/**
 * A simple [Fragment] subclass.
 * Use the [EditAddLabelsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EditAddLabelsFragment : Fragment(), OnBottomActionClickedListener {

    private var _binding: FragmentEditAddLabelsBinding? = null
    private var _mainActivity: MainActivity? = null
    private var labelsAdapter: LabelsAdapter? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!
    private val mainActivity get() = _mainActivity!!

    private val mainViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditAddLabelsBinding.inflate(inflater, container, false)
        _mainActivity = requireActivity() as MainActivity
        mainActivity.showBottomBar(false)
        mainActivity.setOnBottomActionCLickedListener(this)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        KeyboardUtils.showSoftInput(binding.newNoteEd)
        setupRecycler()
        setUpObservers()
        mainActivity.loadEntity(Entity.LABEL)
        setClickListeners()
    }

    private fun setClickListeners() {
        binding.newNoteEd.onFocusChangeListener = OnNewNoteEdFocusChange(this)
        binding.saveNewNoteIv.setOnClickListener {
            validateEd()
        }
        binding.addClearIv.setOnClickListener {
            binding.newNoteEd.setText("")
        }
    }

    private fun validateEd() {
        val text: String = binding.newNoteEd.text.toString()
        if (TextUtils.isEmpty(text)) {
            ToastUtils.showShort("Label name cannot be empty.")
        } else {
            saveNewLabel(text)
        }
    }

    private fun saveNewLabel(text: String) {
        val label = Label(
            null,
            text
        )
        mainViewModel.addLabel(label)
        binding.newNoteEd.setText("")
    }

    private class OnNewNoteEdFocusChange(fragment: EditAddLabelsFragment) :
        View.OnFocusChangeListener {

        private val mFragment = fragment

        override fun onFocusChange(p0: View?, p1: Boolean) {
            if (p1) {
                mFragment.binding.saveNewNoteIv.visibility = View.VISIBLE
                mFragment.binding.addClearIv.setImageResource(R.drawable.ic_baseline_clear_24)
            } else {
                mFragment.binding.saveNewNoteIv.visibility = View.INVISIBLE
                mFragment.binding.addClearIv.setImageResource(R.drawable.ic_baseline_add_24)
            }
        }

    }

    private fun setUpObservers() {
        mainViewModel.labelDataState.observe(requireActivity(), { labelDataState ->
            when (labelDataState) {
                is DataState.Error -> {
                    showProgress(false)
                    if (_binding != null) {
                        HelpersUtil.showBottomSnack(binding.root, labelDataState.exception.message)
                    }
                }
                is DataState.Loading -> {
                    showProgress(true)
                }
                is DataState.Success -> {
                    showProgress(false)
                    labelsAdapter?.submitList(labelDataState.data.reversed())
                }
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

    private fun setupRecycler() {
        binding.labelsRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.labelsRecycler.setHasFixedSize(true)
        labelsAdapter = LabelsAdapter(MyDifferUtil.labelAsyncDifferConfig)
        binding.labelsRecycler.adapter = labelsAdapter
        labelsAdapter?.setEditTextChangeListener(EditTextChangeListener(this))
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            mainViewModel.cleanLabels()
        }, 1000)
    }

    fun onTextChanged(text: String, label: Label) {
        if (text == "") {
            ToastUtils.showShort("Please enter a label name")
        } else {
            label.name = text
            mainViewModel.updateLabel(label)
        }
    }

    fun deleteLabel(label: Label) {
        mainViewModel.deleteLabel(label)
    }

    fun cleanLabels() {
        mainViewModel.cleanLabels()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment EditAddLabelsFragment.
         */
        @JvmStatic
        fun newInstance() = EditAddLabelsFragment()
    }

    override fun onNewNoteClicked() {
        mainViewModel.fetchNote(
            HelpersUtil.newNote(),
            NoteFetchListener(this, MyFragment.EDIT_ADD_LABELS_FRAGMENT)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _mainActivity = null
    }
}