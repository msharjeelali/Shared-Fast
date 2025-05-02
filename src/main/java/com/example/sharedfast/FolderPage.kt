package com.example.sharedfast

import Note
import NoteAdapter
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class FolderPage : AppCompatActivity() {

    val notes = mutableListOf<Note>()
    lateinit var noteRecyclerView: RecyclerView
    lateinit var noteAdapter: NoteAdapter

    val GALLERY_CONSTANT = 113
    private lateinit var imageUri: Uri

    @SuppressLint("IntentReset")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(R.layout.activity_folder_page)

        val folderName = intent.getStringExtra("folderName")
        findViewById<TextView>(R.id.header).text = folderName

        val importButton = findViewById<Button>(R.id.import_button)
        importButton.setOnClickListener {
            var captureIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            captureIntent.type = "image/*"
            startActivityForResult(captureIntent,GALLERY_CONSTANT)
        }

        noteRecyclerView = findViewById<RecyclerView>(R.id.notes_view)
        noteAdapter = NoteAdapter(notes)
        noteRecyclerView.layoutManager = LinearLayoutManager(this)
        noteRecyclerView.adapter = noteAdapter
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?){
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == GALLERY_CONSTANT && resultCode == RESULT_OK){
            val imageUri: Uri ?= data?.data
            imageUri.let {
                val bitMap = MediaStore.Images.Media.getBitmap(this.contentResolver, it)
                val current = LocalDateTime.now()
                val date = current.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                val time = current.format(DateTimeFormatter.ofPattern("HH:mm:ss"))
                noteAdapter.addItem(Note("Image", bitMap, date, time))
            }

        }
    }
}