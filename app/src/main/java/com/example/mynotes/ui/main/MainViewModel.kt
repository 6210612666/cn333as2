package com.example.mynotes.ui.main

import android.content.ContentValues
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.mynotes.models.TaskList

class MainViewModel(val sharedPreferences: SharedPreferences) : ViewModel() {

    lateinit var list: TaskList

    lateinit var onListAdded: () -> Unit
    lateinit var onListRemoved: () -> Unit
    var whereRemoved: Int = -1

    val lists: MutableList<TaskList> by lazy {
        retrieveLists()
    }

    // key: list-name, value: hashset (list of items)
    private fun retrieveLists(): MutableList<TaskList> {

        val sharedPreferencesContents = sharedPreferences.all
        val noteLists = ArrayList<TaskList>()

        for (noteList in sharedPreferencesContents) {

            val note = TaskList(noteList.key, noteList.value as String)
            noteLists.add(note)
        }

        return noteLists
    }

    fun createList(list: TaskList) {
        val editor = sharedPreferences.edit()
        val text: String = list.tasks
        editor.putString(list.name, text)
        editor.apply()
        lists.add(list)
        onListAdded.invoke()

    }

    fun saveList(list: TaskList) {
        val editor = sharedPreferences.edit()
        val text: String = list.tasks
        editor.putString(list.name, text)
        editor.apply()
        Log.d(ContentValues.TAG, list.tasks)

    }

    fun updateList(list: TaskList) {
        val editor = sharedPreferences.edit()
        val text: String = list.tasks
        editor.putString(list.name, text)
        editor.apply()
        Log.d(ContentValues.TAG, list.tasks)
        refreshLists()
    }
    fun removeList(list: TaskList){
        val index = lists.indexOf(list)
        whereRemoved = index
        lists.remove(list)
        onListRemoved.invoke()

        val editor = sharedPreferences.edit()
        editor.remove(list.name)
        editor.apply()
    }

    fun findList(key: String): Boolean{
        return sharedPreferences.contains(key)
    }

    private fun refreshLists() {
        lists.clear()
        lists.addAll(retrieveLists())
    }

}