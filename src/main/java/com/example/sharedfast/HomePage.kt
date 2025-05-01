package com.example.sharedfast

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.window.Dialog
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.marginLeft

class HomePage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(R.layout.activity_home_page)

        val addNewFolderButton:Button = findViewById(R.id.addFolderButton)
        addNewFolderButton.setOnClickListener{

            val editText: EditText = EditText(this)
            editText.hint = "Folder Name"

            AlertDialog.Builder(this)
                .setTitle("New Folder")
                .setMessage("Enter folder name")
                .setView(editText)
                .setCancelable(false)
                .setPositiveButton("Create") { dialog, which ->
                    val folderName = editText.text.toString().trim()
                    Toast.makeText(this, "Folder name: $folderName", Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("Cancel") { dialog, which ->
                    dialog.cancel()
                }
                .show()

        }

    }
}