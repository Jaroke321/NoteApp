package com.example.noteapp.screen

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noteapp.data.NotesDataSource
import com.example.noteapp.model.Note
import com.example.noteapp.repository.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(InternalCoroutinesApi::class)
@HiltViewModel
class NoteViewModel @Inject constructor(private val repository: NoteRepository): ViewModel() {

    //private var noteList = mutableStateListOf<Note>()
    private val _noteList = MutableStateFlow<List<Note>>(emptyList())
    val noteList = _noteList.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getAllNotes().distinctUntilChanged()
                .collect { listOfNodes ->

                    if (listOfNodes.isNullOrEmpty()) {

                    } else {
                        _noteList.value = listOfNodes
                    }

            }
        }
    }

    fun addNote(note: Note) = viewModelScope.launch { repository.addNote(note) }

    fun updateNote(note: Note) = viewModelScope.launch { repository.updateNote(note) }

    fun removeNote(note: Note) = viewModelScope.launch { repository.deleteNote(note) }

}