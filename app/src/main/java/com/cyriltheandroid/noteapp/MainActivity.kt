package com.cyriltheandroid.noteapp

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cyriltheandroid.noteapp.model.NoteModel
import com.cyriltheandroid.noteapp.viewmodel.NoteViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity(), NoteAdapter.NoteListener {

    lateinit var addNewNoteButton: FloatingActionButton
    lateinit var noteRecyclerView: RecyclerView
    var notes = mutableListOf<NoteModel>()
    lateinit var noteViewModel: NoteViewModel

    private val createNoteActivityResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            val title = data?.getStringExtra(NOTE_TITLE)
            val desc = data?.getStringExtra(NOTE_DESC)

            val noteModel = NoteModel(title!!, desc!!)
            notes.add(0, noteModel)
            noteRecyclerView.adapter?.notifyItemChanged(0)
            noteViewModel.saveNotes(notes)
        }
    }

    private val noteDetailsActivityResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            val title = data?.getStringExtra(NOTE_TITLE)
            val desc = data?.getStringExtra(NOTE_DESC)
            val position = data?.getIntExtra(NOTE_POSITION, -1)

            Log.d("Position", "Position $position, $title, $desc");
            if (position != -1) {
                notes[position!!].title = title!!
                notes[position].desc = desc!!
                noteRecyclerView.adapter?.notifyItemChanged(position)
                noteViewModel.saveNotes(notes)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        noteViewModel = ViewModelProvider(this)[NoteViewModel::class.java]
        noteViewModel.setSharedPreferences(this)
        noteViewModel.notesLiveData.observe(this) {
            notes.addAll(it)
            initNoteRecyclerView()
        }
        addNewNoteButton = findViewById(R.id.add_new_note_button)
        addNewNoteButton.setOnClickListener {
            val intent = Intent(this, CreateNoteActivity::class.java)
            createNoteActivityResult.launch(intent)
        }
    }

    private fun initNoteRecyclerView() {
        noteRecyclerView = findViewById(R.id.note_recycler_view)
        val adapter = NoteAdapter(notes, this)
        val layoutManager = LinearLayoutManager(this)

        noteRecyclerView.adapter = adapter
        noteRecyclerView.layoutManager = layoutManager
    }

    private fun showDeleteNoteAlertDialog(note: NoteModel, position: Int) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Suppression de la note ${note.title}")
            .setMessage("Êtes-vous certain de vouloir supprimer la note ?")
            .setIcon(android.R.drawable.ic_menu_delete)
            .setPositiveButton("Supprimer") { dialog, _ ->
                dialog.dismiss()
                deleteNote(position)
                displayToast("La note ${note.title} a bien été supprimée.")
            }
            .setNegativeButton("Annuler", null)
        val alertDialog = builder.create()
        alertDialog.show()
    }

    private fun deleteNote(position: Int) {
        notes.removeAt(position)
        noteRecyclerView.adapter?.notifyItemRemoved(position)
        noteViewModel.saveNotes(notes)
    }

    private fun displayToast(message: String) {
        runOnUiThread {
            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        }
    }

    override fun onItemClicked(position: Int) {
        val note = notes[position]
        val intent = Intent(this, NoteDetailsActivity::class.java)
        intent.putExtra(NOTE_TITLE, note.title)
        intent.putExtra(NOTE_DESC, note.desc)
        intent.putExtra(NOTE_POSITION, position)
        noteDetailsActivityResult.launch(intent)
    }

    override fun onDeleteNoteClicked(position: Int) {
        showDeleteNoteAlertDialog(notes[position], position)
    }
}