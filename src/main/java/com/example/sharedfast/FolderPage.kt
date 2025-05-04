package com.example.sharedfast

import Note
import NoteAdapter
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import android.Manifest
import android.content.ContentValues
import android.graphics.Bitmap
import android.os.Environment
import android.widget.Toast
import com.google.gson.Gson
import androidx.core.content.edit
import androidx.core.net.toUri

class FolderPage : AppCompatActivity() {

    lateinit var folderName: String
    val notes = mutableListOf<Note>()
    lateinit var noteRecyclerView: RecyclerView
    lateinit var noteAdapter: NoteAdapter

    val GALLERY_CONSTANT = 113
    val CAMERA_CONSTANT = 114

    @SuppressLint("IntentReset", "QueryPermissionsNeeded")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(R.layout.activity_folder_page)

        folderName = intent.getStringExtra("folderName") ?: "Default"
        findViewById<TextView>(R.id.header).text = folderName

        loadSavedNotes(folderName)

        val importButton = findViewById<Button>(R.id.import_button)
        importButton.setOnClickListener {
            var importIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            importIntent.type = "image/*"
            importIntent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            startActivityForResult(importIntent,GALLERY_CONSTANT)
        }

        val captureButton = findViewById<Button>(R.id.capture_button)
        captureButton.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), CAMERA_CONSTANT)
            } else {
                val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                if (takePictureIntent.resolveActivity(packageManager) != null) {
                    startActivityForResult(takePictureIntent, CAMERA_CONSTANT)
                }
            }
        }

        val shareButton = findViewById<Button>(R.id.share_button)
            shareButton.setOnClickListener {
                val imageUris = ArrayList<Uri>()
                val adapterNotes = noteAdapter.getItems()
                for (note in adapterNotes) {
                    try {
                        val uri = Uri.parse(note.source)
                        contentResolver.openInputStream(uri)?.close()
                        imageUris.add(uri)
                    } catch (e: Exception) {

                    }
                }

                if (imageUris.isNotEmpty()) {
                    val shareIntent = Intent().apply {
                        action = Intent.ACTION_SEND_MULTIPLE
                        type = "image/*"
                        putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUris)
                        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    }
                    startActivity(Intent.createChooser(shareIntent, "Share images via"))
                }
            }


        noteRecyclerView = findViewById<RecyclerView>(R.id.notes_view)
        noteAdapter = NoteAdapter(notes)
        noteRecyclerView.layoutManager = LinearLayoutManager(this)
        noteRecyclerView.adapter = noteAdapter
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            val current = LocalDateTime.now()
            val date = current.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            val time = current.format(DateTimeFormatter.ofPattern("HH:mm:ss"))

            if (requestCode == GALLERY_CONSTANT) {
                val imageUri: Uri? = data?.data
                imageUri?.let { uri ->
                        contentResolver.openInputStream(uri)?.use {
                            val note = Note("Imported Image", uri.toString(), date, time)
                            saveNoteInPreferences(note, folderName)
                            noteAdapter.addItem(note)
                        }
                }
            } else if (requestCode == CAMERA_CONSTANT) {
                val extras = data?.extras
                val imageBitmap = extras?.get("data") as? android.graphics.Bitmap
                imageBitmap?.let {
                    try {
                        val savedImageURL = MediaStore.Images.Media.insertImage(
                            contentResolver,
                            it,
                            "SharedFast_" + System.currentTimeMillis(),
                            "Captured via Camera"
                        )
                        val note = Note("Captured Image", savedImageURL ?: "", date, time)
                        saveNoteInPreferences(note, folderName)
                        noteAdapter.addItem(note)
                        Toast.makeText(this, "Image captured successfully", Toast.LENGTH_SHORT).show()
                    } catch (e: Exception) {
                        Toast.makeText(this, "Failed to save captured image", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_CONSTANT) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                if (takePictureIntent.resolveActivity(packageManager) != null) {
                    startActivityForResult(takePictureIntent, CAMERA_CONSTANT)
                }
            } else {
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @SuppressLint("MutatingSharedPrefs")
    private fun saveNoteInPreferences(note: Note, folderName: String) {
        val sharedPreferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE)
        sharedPreferences.edit() {

            val gson = Gson()
            val noteJson = gson.toJson(note)

            val notesJson = sharedPreferences.getStringSet(folderName, mutableSetOf()) ?: mutableSetOf()
            notesJson.add(noteJson)

            putStringSet(folderName, notesJson)
        }
    }

    private fun loadSavedNotes(folderName: String) {
        val sharedPreferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE)
        val notesJson = sharedPreferences.getStringSet(folderName, mutableSetOf()) ?: mutableSetOf()

        val gson = Gson()

        notesJson.forEach { noteJson ->
            val note = gson.fromJson(noteJson, Note::class.java)
            notes.add(note)
        }
    }

}