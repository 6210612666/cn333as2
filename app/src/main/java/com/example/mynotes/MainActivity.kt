package com.example.mynotes

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import com.example.mynotes.databinding.MainActivityBinding
import com.example.mynotes.models.TaskList
import com.example.mynotes.ui.main.MainFragment
import com.example.mynotes.ui.main.MainViewModel
import com.example.mynotes.ui.main.MainViewModelFactory
import android.content.Intent
import android.graphics.Insets.add
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.commit
import com.example.mynotes.ui.detail.NoteDetailFragment


class MainActivity : AppCompatActivity(), MainFragment.MainFragmentInteractionListener {
    private lateinit var binding: MainActivityBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this,
            MainViewModelFactory(android.preference.PreferenceManager.getDefaultSharedPreferences(this))
        )
            .get(MainViewModel::class.java)
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            val mainFragment = MainFragment.newInstance()
            mainFragment.clickListener = this
            mainFragment.holdClickListener = this
            val fragmentContainerViewId: Int = if (binding.mainFragmentContainer == null) {
                R.id.container }
            else {
                R.id.main_fragment_container
            }

            supportFragmentManager.commit {
                setReorderingAllowed(true)
                add(fragmentContainerViewId, mainFragment)
            }
        }

        binding.listAddButton.setOnClickListener {
            showCreateListDialog()
        }
    }

    private fun showCreateListDialog() {
        val dialogTitle = getString(R.string.name_of_list)
        val positiveButtonTitle = getString(R.string.create_list)

        val builder = AlertDialog.Builder(this)
        val listTitleEditText = EditText(this)
        listTitleEditText.inputType = InputType.TYPE_CLASS_TEXT

        builder.setTitle(dialogTitle)
        builder.setView(listTitleEditText)

        builder.setPositiveButton(positiveButtonTitle) { dialog, _ ->
            dialog.dismiss()
            if (viewModel.findList(listTitleEditText.text.toString())){
                Toast.makeText(this,"The note name has been taken",Toast.LENGTH_LONG).show()
            }else {
                val noteList = TaskList(listTitleEditText.text.toString(), "")
                viewModel.createList(noteList)
                showNote(noteList)
            }
        }
        builder.create().show()
    }


    private fun showNote(list: TaskList) {
        if (binding.mainFragmentContainer == null) {
            val noteIntent = Intent(this, NoteDetailActivity::class.java)
            noteIntent.putExtra(INTENT_LIST_KEY, list)
            startActivity(noteIntent)
        }else{
            val bundle = bundleOf(INTENT_LIST_KEY to list)

            supportFragmentManager.commit {
                setReorderingAllowed(true)
                replace(R.id.subnote_fragment, NoteDetailFragment::class.java,bundle,null)
            }
        }
    }

    private fun showRemoveDialog(list: TaskList) {
        val dialogTitle = "You removing note ${list.name}!"
        val positiveButtonTitle = "Yes"
        val negativeButtonTitle = "No"

        val builder = AlertDialog.Builder(this)

        builder.setTitle(dialogTitle)

        builder.setPositiveButton(positiveButtonTitle) { dialog, _ ->
            dialog.dismiss()
            viewModel.removeList(list)

            viewModel = ViewModelProvider(this,
                MainViewModelFactory(android.preference.PreferenceManager.getDefaultSharedPreferences(this))
            )
                .get(MainViewModel::class.java)
            binding = MainActivityBinding.inflate(layoutInflater)
            setContentView(binding.root)

            val mainFragment = MainFragment.newInstance()
            mainFragment.clickListener = this
            mainFragment.holdClickListener = this
            val fragmentContainerViewId: Int = if (binding.mainFragmentContainer == null) {
                R.id.container }
            else {
                R.id.main_fragment_container
            }

            supportFragmentManager.commit {
                setReorderingAllowed(true)
                add(fragmentContainerViewId, mainFragment)
            }


            binding.listAddButton.setOnClickListener {
                showCreateListDialog()
            }

        }
        builder.setNegativeButton(negativeButtonTitle) { dialog, _ ->
            dialog.dismiss()
        }

        builder.create().show()
    }

    companion object {
        const val INTENT_LIST_KEY = "list"
        const val LIST_DETAIL_REQUEST_CODE = 123
    }

    override fun listItemTapped(list: TaskList) {
        showNote(list)
    }

    override fun listItemHold(list: TaskList) {
        showRemoveDialog(list)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == LIST_DETAIL_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            data?.let {
                viewModel.updateList(data.getParcelableExtra(INTENT_LIST_KEY)!!)
            }
        }
    }


    override fun onBackPressed() {
        val listNoteFragment = supportFragmentManager.findFragmentById(R.id.subnote_fragment)
        if (listNoteFragment == null) {
            super.onBackPressed()
//
        } else {
            title = resources.getString(R.string.app_name)
            val editNoteText: EditText = findViewById(R.id.editTextDetail)
            viewModel.saveList(TaskList(viewModel.list.name,editNoteText.text.toString()))
            editNoteText.setText("")
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                remove(listNoteFragment)
            }
            binding.listAddButton.setOnClickListener {
                showCreateListDialog()
            }
        }
    }
}