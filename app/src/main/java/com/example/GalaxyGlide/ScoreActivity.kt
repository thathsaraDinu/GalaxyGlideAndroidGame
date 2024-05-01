package com.example.GalaxyGlide

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ScoreActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.highscore_activity);
        val scoreVal : TextView = findViewById(R.id.highScores);
        val main : TextView = findViewById(R.id.mainMenu)

        // Retrieve the score data sent from SpaceShipView
        main.setOnClickListener{
            val intent2: Intent = Intent(this, MainActivity::class.java);
            startActivity(intent2);
        }
        val replay : TextView = findViewById(R.id.replay)

        // Retrieve the score data sent from SpaceShipView
        replay.setOnClickListener{
            val intent3: Intent = Intent(this, GameActivity::class.java);
            startActivity(intent3);
        }

        val intent1 = intent
        // Retrieve the score data sent from SpaceShipView
        val score = intent1.getIntExtra("score", 0)
        scoreVal.setText("Your Score: "+score);

    }
}