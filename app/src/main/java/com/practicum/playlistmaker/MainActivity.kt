package com.practicum.playlistmaker

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val buttonSearch = findViewById<Button>(R.id.search)
        val buttonClickListener: View.OnClickListener = object : View.OnClickListener {
            override fun onClick(v: View?) {
                Toast.makeText(this@MainActivity, "Жмякс!", Toast.LENGTH_SHORT).show()
            }
        }
        buttonSearch.setOnClickListener(buttonClickListener)

        val buttonMediaLibrary = findViewById<Button>(R.id.media_library)
        buttonMediaLibrary.setOnClickListener {
            Toast.makeText(this@MainActivity, "Упс! И снова ничего", Toast.LENGTH_SHORT).show()
        }
        val buttonSetting = findViewById<Button>(R.id.settings)
        buttonSetting.setOnClickListener {
            Toast.makeText(this@MainActivity, "Наташа! Мы фсе сломали!", Toast.LENGTH_SHORT).show()
        }
    }
}