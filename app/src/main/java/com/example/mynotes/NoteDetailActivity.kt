package com.example.mynotes

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import com.example.mynotes.databinding.NoteDetailActivityActivityBinding
import com.example.mynotes.models.TaskList
import com.example.mynotes.ui.detail.NoteDetailFragment
import com.example.mynotes.ui.main.MainViewModel
import com.example.mynotes.ui.main.MainViewModelFactory

class NoteDetailActivity : AppCompatActivity() {
    private lateinit var binding: NoteDetailActivityActivityBinding
    private lateinit var viewModel: MainViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this,
            MainViewModelFactory(PreferenceManager.getDefaultSharedPreferences(this))
        )
            .get(MainViewModel::class.java)
        binding = NoteDetailActivityActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.list = intent.getParcelableExtra(MainActivity.INTENT_LIST_KEY)!!
        title = viewModel.list.name

        val sharedPreferences : SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val editNoteText: EditText = findViewById(R.id.editTextTextMultiLine)
        val contented = sharedPreferences.getString(title as String,"Not found")
        if (contented != null) {
            Log.d(ContentValues.TAG, contented)
            editNoteText.setText(contented)
        }else{
            Log.d(ContentValues.TAG, "BROKE")
        }


        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()

                .replace(R.id.note_fragment_container, NoteDetailFragment.newInstance())
                .commitNow()
        }
    }
    override fun onBackPressed() {
        val editNoteText: EditText = findViewById(R.id.editTextTextMultiLine)

        viewModel.saveList(TaskList(viewModel.list.name,editNoteText.text.toString()))

        val bundle = Bundle()
        bundle.putParcelable(MainActivity.INTENT_LIST_KEY,viewModel.list)
        val intent = Intent()
        intent.putExtras(bundle)
        setResult(Activity.RESULT_OK, intent)

        super.onBackPressed()

    }
}