package com.cyriltheandroid.noteapp

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class NoteDetailsActivity : AppCompatActivity() {

    lateinit var noteTitleEditText: EditText
    lateinit var noteDescEditText: EditText
    lateinit var updateNoteButton: Button

    var noteTitle: String? = null
    var noteDesc: String? = null
    var notePosition: Int? = null
    var isUpdated: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_details)

        initViews()
        fetchData()
        updateNoteButton.setOnClickListener {
            noteTitle = noteTitleEditText.text.toString()
            noteDesc = noteDescEditText.text.toString()
            isUpdated = true
            Toast.makeText(this, getString(R.string.note_updated), Toast.LENGTH_LONG).show()
        }
    }

    override fun onBackPressed() {
        if (isUpdated) {
            val intent = Intent()
            intent.putExtra(NOTE_TITLE, noteTitle)
            intent.putExtra(NOTE_DESC, noteDesc)
            intent.putExtra(NOTE_POSITION, notePosition)
            setResult(Activity.RESULT_OK, intent)
        }
        super.onBackPressed()
    }

    private fun fetchData() {
        if (intent.hasExtra(NOTE_TITLE)) {
            noteTitle = intent.getStringExtra(NOTE_TITLE)
            noteDesc = intent.getStringExtra(NOTE_DESC)
            notePosition = intent.getIntExtra(NOTE_POSITION, -1)
            noteTitleEditText.setText(noteTitle)
            noteDescEditText.setText(noteDesc)
        }
    }

    private fun initViews() {
        noteTitleEditText = findViewById(R.id.note_title_edit_text)
        noteDescEditText = findViewById(R.id.note_desc_edit_text)
        updateNoteButton = findViewById(R.id.update_note_button)
    }
}