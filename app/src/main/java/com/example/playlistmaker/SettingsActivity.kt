package com.example.playlistmaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val buttonReturn = findViewById<ImageView>(R.id.return_from_settings)
        buttonReturn.setOnClickListener {
            val returnIntent = Intent(this, MainActivity::class.java)
            returnIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
            startActivity(returnIntent)
            this.finish()
        }
    }
}