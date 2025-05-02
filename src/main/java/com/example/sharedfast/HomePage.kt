package com.example.sharedfast

import FolderAdapter
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HomePage : AppCompatActivity() {

    val foldersName: MutableList<String> = ArrayList()
    lateinit var folderRecyclerView: RecyclerView
    lateinit var folderAdapter: FolderAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(R.layout.activity_home_page)

        val addNewFolderButton:Button = findViewById(R.id.add_folder_button)
        addNewFolderButton.setOnClickListener{

            val editText = EditText(this)
            editText.hint = "Folder Name"

            AlertDialog.Builder(this)
                .setTitle("New Folder")
                .setMessage("Enter folder name")
                .setView(editText)
                .setCancelable(false)
                .setPositiveButton("Create") { dialog, which ->
                    val folderName = editText.text.toString().trim()
                    folderAdapter.addItem(folderName)

                }
                .setNegativeButton("Cancel") { dialog, which ->
                    dialog.cancel()
                }
                .show()

        }

        folderRecyclerView = findViewById<RecyclerView>(R.id.folders_view)
        foldersName.add("First")
        foldersName.add("Second")
        foldersName.add("Third")
        foldersName.add("Forth")
        foldersName.add("Five")
        folderRecyclerView.layoutManager = GridLayoutManager(this, 2)
        folderAdapter = FolderAdapter(foldersName) { folderName ->
            startActivity(Intent(this, FolderPage::class.java).putExtra("folderName", folderName))
        }

        folderRecyclerView.adapter = folderAdapter
    }
}