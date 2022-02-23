package com.example.mynotes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mynotes.ui.detail.NoteDetailFragment

class NoteDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.note_detail_activity_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, NoteDetailFragment.newInstance())
                .commitNow()
        }
    }
}