package com.example.GalaxyGlide

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val newGame: TextView = findViewById(R.id.newGame)
        val highScore: TextView = findViewById(R.id.highScores)
        val exitBtn: TextView = findViewById(R.id.exit)

        highScore.setOnClickListener {
            val sharedPreferences: SharedPreferences = getSharedPreferences("SpaceGamePrefs", MODE_PRIVATE)

            // Retrieve the highest score from SharedPreferences
            val highestScore = sharedPreferences.getInt("highestScore", 0)

            highScore.text = "Highest Score: $highestScore"
        }

        exitBtn.setOnClickListener{
            finishAffinity()
        }
        newGame.setOnClickListener {
            val intent: Intent = Intent(this, GameActivity::class.java)
            startActivity(intent)
        }

    }
}
