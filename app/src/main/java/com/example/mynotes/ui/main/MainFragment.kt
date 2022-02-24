package com.example.mynotes.ui.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mynotes.R
import com.example.mynotes.databinding.MainFragmentBinding
import com.example.mynotes.models.TaskList

// layout: main_activity.xml
// Binding Class: MainActivityBinding
// layout: main_fragment.xml
// Binding Class: MainFragmentBinding
class MainFragment() :
    Fragment(), ListSelectionRecyclerViewAdapter.ListSelectionRecyclerViewClickListener {

    private lateinit var binding: MainFragmentBinding
    var clickListener: MainFragmentInteractionListener? = null
    var holdClickListener: MainFragmentInteractionListener? = null

    interface MainFragmentInteractionListener {
        fun listItemTapped(list: TaskList)
        fun listItemHold(list: TaskList)
    }


    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MainFragmentBinding.inflate(inflater, container, false)
        // id: lists_recyclerview => listsRecyclerview
        binding.listsRecyclerview.layoutManager = LinearLayoutManager(requireContext())

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity(),
            MainViewModelFactory(android.preference.PreferenceManager.getDefaultSharedPreferences(requireActivity())))
            .get(MainViewModel::class.java)

        val recyclerViewAdapter = ListSelectionRecyclerViewAdapter(viewModel.lists, this,this)
        binding.listsRecyclerview.adapter = recyclerViewAdapter
        viewModel.onListAdded = {
            recyclerViewAdapter.listsUpdated()
        }
        viewModel.onListRemoved ={
            recyclerViewAdapter.listsRemove(viewModel.whereRemoved)

        }
    }
    override fun listItemClicked(list: TaskList) {
        clickListener?.listItemTapped(list)
    }

    override fun listItemHold(list: TaskList) {
        holdClickListener?.listItemHold(list)
    }


}