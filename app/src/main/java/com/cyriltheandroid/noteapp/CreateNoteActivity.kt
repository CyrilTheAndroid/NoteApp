package com.cyriltheandroid.noteapp

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText

class CreateNoteActivity : AppCompatActivity() {

    lateinit var noteTitleEditText: EditText
    lateinit var noteDescEditText: EditText
    lateinit var createNoteButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_note)

        noteTitleEditText = findViewById(R.id.note_title_edit_text)
        noteDescEditText = findViewById(R.id.note_desc_edit_text)
        createNoteButton = findViewById(R.id.create_note_button)

        createNoteButton.setOnClickListener {
            val title = noteTitleEditText.text.toString()
            val desc = noteDescEditText.text.toString()
            Log.d("CreateNoteActivity", "Titre: $title, desc: $desc")

            val intent = Intent()
            intent.putExtra(NOTE_TITLE, title)
            intent.putExtra(NOTE_DESC, desc)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }
}