package com.example.GalaxyGlide


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import java.util.Timer
import java.util.TimerTask

class GameActivity : AppCompatActivity() {
    private var gameView: SpaceShipView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        gameView = SpaceShipView(this)
        setContentView(gameView)
        val timer = Timer()

        timer.schedule(object : TimerTask() {
            override fun run() {
                runOnUiThread { gameView!!.invalidate() }
            }
        }, 0, Interval)
    }

    companion object {
        private const val Interval: Long = 30
    }
}

