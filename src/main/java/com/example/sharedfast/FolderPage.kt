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
import android.widget.ImageView
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
import android.widget.Toast

class FolderPage : AppCompatActivity() {

    val notes = mutableListOf<Note>()
    lateinit var noteRecyclerView: RecyclerView
    lateinit var noteAdapter: NoteAdapter

    val GALLERY_CONSTANT = 113
    val CAMERA_CONSTANT = 114
    private lateinit var imageUri: Uri

    @SuppressLint("IntentReset", "QueryPermissionsNeeded")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(R.layout.activity_folder_page)

        val folderName = intent.getStringExtra("folderName")
        findViewById<TextView>(R.id.header).text = folderName

        val importButton = findViewById<Button>(R.id.import_button)
        importButton.setOnClickListener {
            var importIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            importIntent.type = "image/*"
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

        noteRecyclerView = findViewById<RecyclerView>(R.id.notes_view)
        noteAdapter = NoteAdapter(notes)
        noteRecyclerView.layoutManager = LinearLayoutManager(this)
        noteRecyclerView.adapter = noteAdapter
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?){
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == RESULT_OK){
            val current = LocalDateTime.now()
            val date = current.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            val time = current.format(DateTimeFormatter.ofPattern("HH:mm:ss"))

            if(requestCode == GALLERY_CONSTANT) {
                val imageUri: Uri? = data?.data
                imageUri?.let {
                    val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, it)
                    noteAdapter.addItem(Note("Imported Image", bitmap, date, time))
                }
            }
            else if(requestCode == CAMERA_CONSTANT){
                val extras = data?.extras
                val imageBitmap = extras?.get("data") as? android.graphics.Bitmap
                imageBitmap?.let {
                    noteAdapter.addItem(Note("Captured Image", imageBitmap, date, time))
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
}