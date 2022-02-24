package com.example.mynotes.ui.detail

import android.content.ContentValues
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.example.mynotes.MainActivity
import com.example.mynotes.R
import com.example.mynotes.models.TaskList
import com.example.mynotes.ui.main.MainViewModel

class NoteDetailFragment : Fragment() {

    companion object {
        fun newInstance() = NoteDetailFragment()
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        val note: TaskList? = arguments?.getParcelable(MainActivity.INTENT_LIST_KEY)

        note?.let{
            viewModel.list = note
            requireActivity().title = note.name
            val text: EditText = requireActivity().findViewById(R.id.editTextDetail)
            val sharedPreferences = viewModel.sharedPreferences
            val contented = sharedPreferences.getString(viewModel.list.name,"Not found")
            Log.d(ContentValues.TAG, note.tasks)
            text.setText(contented)
        }
    }
}

