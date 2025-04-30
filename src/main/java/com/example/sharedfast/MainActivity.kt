package com.example.sharedfast

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(R.layout.activity_splash_screen)

        val myButton: Button = findViewById(R.id.intentButton)
        myButton.setOnClickListener({
            startActivity(Intent(this, HomePage::class.java))
        })

    }
}
