package com.example.GalaxyGlide

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import kotlin.math.floor


class SpaceShipView(context: Context) : View(context) {
    private val ship = arrayOfNulls<Bitmap>(2)
    private var shipY = 0
    private var shipX: Int
    private var score = 0
    private var lifeCounter: Int
    private var highestScore: Int
    private val meteor = arrayOfNulls<Bitmap>(4)
    private var meteor1X :Int = 0
    private var meteor1Y = 100
    private var meteor1speed = 5
    private var meteor2X :Int = 0
    private var meteor2Y = 500
    private var meteor2speed = 10
    private var meteor3X :Int = 0
    private var meteor3Y = -100
    private var meteor3speed = 14
    private var meteor4X :Int = 0
    private var meteor4Y = 300
    private var meteor4speed = 18
    private var canvasWidth = 0
    private var canvasHeight = 0
    private var touch = false
    private var backgroundImage: Bitmap? = null
    private val scorePaint = Paint()
    private val highScorePaint = Paint()
    private val life = arrayOfNulls<Bitmap>(2)
    private var collision: Bitmap;
    private var initialMeteor = true;
    private var finished = true;
    private var elapsedTimeMillis = 0L
    private var currentTimeMillis = 0L
    private var deltaTimeMillis = 0L
    private var lastUpdateTimeMillis = System.currentTimeMillis()
    private val sharedPreferences: SharedPreferences

    init {
        ship[0] = BitmapFactory.decodeResource(resources, R.drawable.rocket)
        ship[0] = ship[0]?.let { Bitmap.createScaledBitmap(it, 120, 240, false) }

        scorePaint.color = Color.WHITE
        scorePaint.textSize = 50f
        scorePaint.setTypeface(Typeface.DEFAULT_BOLD)
        scorePaint.isAntiAlias = true

        highScorePaint.color = Color.CYAN
        highScorePaint.textSize = 30f
        highScorePaint.setTypeface(Typeface.DEFAULT_BOLD)
        highScorePaint.isAntiAlias = true

        life[0] = BitmapFactory.decodeResource(resources, R.drawable.heart)
        life[0] = life[0]?.let { Bitmap.createScaledBitmap(it, 50, 50, false) }
        life[1] = BitmapFactory.decodeResource(resources, R.drawable.heartempty)
        life[1] = life[1]?.let { Bitmap.createScaledBitmap(it, 50, 50, false) }

        meteor[0] = BitmapFactory.decodeResource(resources, R.drawable.asteroid)
        meteor[0] = meteor[0]?.let { Bitmap.createScaledBitmap(it, 100, 100, false) }
        meteor[1] = BitmapFactory.decodeResource(resources, R.drawable.asteroid1)
        meteor[1] = meteor[1]?.let { Bitmap.createScaledBitmap(it, 100, 100, false) }
        meteor[2] = BitmapFactory.decodeResource(resources, R.drawable.asteroid2)
        meteor[2] = meteor[2]?.let { Bitmap.createScaledBitmap(it, 100, 100, false) }
        meteor[3] = BitmapFactory.decodeResource(resources, R.drawable.asteroid3)
        meteor[3] = meteor[3]?.let { Bitmap.createScaledBitmap(it, 100, 100, false) }

        collision = BitmapFactory.decodeResource(resources, R.drawable.collision);
        collision = Bitmap.createScaledBitmap(collision, 120, 120, false);

        shipX = 450

        lifeCounter = 3

        sharedPreferences = context.getSharedPreferences("SpaceGamePrefs", Context.MODE_PRIVATE)
        // Retrieve the highest score from SharedPreferences
        highestScore = sharedPreferences.getInt("highestScore", 0)
        backgroundImage = BitmapFactory.decodeResource(resources, R.drawable.gamebackground2)

    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvasWidth = canvas.width
        canvasHeight = canvas.height

        backgroundImage =
            Bitmap.createScaledBitmap(backgroundImage!!, canvasWidth, canvasHeight, false)
        canvas.drawBitmap(backgroundImage!!, 0f, 0f, null)


        val minShipX = 20
        val maxShipX = canvasWidth
        shipY = canvasHeight - 260
        if (shipX < minShipX) {
            shipX = minShipX
        }
        if (shipX > maxShipX - 140) {
            shipX = maxShipX - 140
        }

        if(initialMeteor) {
            meteor1X = (floor(Math.random() * (maxShipX  )) ).toInt() - (meteor[0]!!.width)/2
            meteor2X = (floor(Math.random() * (maxShipX  )) ).toInt() - (meteor[0]!!.width)/2
            meteor3X = (floor(Math.random() * (maxShipX  )) ).toInt() - (meteor[0]!!.width)/2
            meteor4X = (floor(Math.random() * (maxShipX  )) ).toInt() - (meteor[0]!!.width)/2
            initialMeteor = false
        }
        meteor1Y +=  meteor1speed

        meteor2Y +=  meteor2speed

        meteor3Y +=  meteor3speed

        meteor4Y +=  meteor4speed

        currentTimeMillis = System.currentTimeMillis()
        deltaTimeMillis = currentTimeMillis - lastUpdateTimeMillis
        elapsedTimeMillis += deltaTimeMillis
        lastUpdateTimeMillis = currentTimeMillis

        // Increase meteor speeds every 3 seconds until meteor1speed reaches 20
        if (meteor3speed < 22 && elapsedTimeMillis >= 5000) {
            meteor1speed++
            meteor2speed++
            meteor3speed++
            meteor4speed++
            elapsedTimeMillis = 0L
        }

        if (hitMeteorChecker(shipX, shipY, meteor2X, meteor2Y)) {
            canvas.drawBitmap(collision, meteor2X.toFloat(), meteor2Y.toFloat(), null);

            lifeCounter--
            meteor2Y = -120
            if(lifeCounter > 0) {
                val toast = Toast.makeText(
                    context,
                    "Only $lifeCounter live(s) remaining", Toast.LENGTH_SHORT
                )
                toast.setGravity(Gravity.TOP or Gravity.CENTER_HORIZONTAL, 0, 100)
                toast.show()
            }

        }
        if (hitMeteorChecker(shipX, shipY, meteor1X, meteor1Y)) {
            canvas.drawBitmap(collision, meteor1X.toFloat(), meteor1Y.toFloat(), null);

            lifeCounter--
            meteor1Y = -120
            if(lifeCounter > 0) {
                val toast = Toast.makeText(
                    context,
                    "Only $lifeCounter live(s) remaining", Toast.LENGTH_SHORT
                )
                toast.setGravity(Gravity.TOP or Gravity.CENTER_HORIZONTAL, 0, 100)
                toast.show()
            }
        }
        if (hitMeteorChecker(shipX, shipY, meteor3X, meteor3Y)) {
            canvas.drawBitmap(collision, meteor3X.toFloat(), meteor3Y.toFloat(), null);

            lifeCounter--
            meteor3Y = -120
            if(lifeCounter > 0) {
                val toast = Toast.makeText(
                    context,
                    "Only $lifeCounter live(s) remaining", Toast.LENGTH_SHORT
                )
                toast.setGravity(Gravity.TOP or Gravity.CENTER_HORIZONTAL, 0, 100)
                toast.show()
            }
        }
        if (hitMeteorChecker(shipX, shipY, meteor4X, meteor4Y)) {
            canvas.drawBitmap(collision, meteor4X.toFloat(), meteor4Y.toFloat(), null);

            lifeCounter--
            meteor4Y = -120
            if(lifeCounter > 0) {
                val toast = Toast.makeText(
                    context,
                    "Only $lifeCounter live(s) remaining", Toast.LENGTH_SHORT
                )
                toast.setGravity(Gravity.TOP or Gravity.CENTER_HORIZONTAL, 0, 100)
                toast.show()
            }
        }

        if (lifeCounter == 0 && finished) {
            val toast = Toast.makeText(
                context,
                "Game Over", Toast.LENGTH_SHORT
            )
            toast.setGravity(Gravity.TOP or Gravity.CENTER_HORIZONTAL, 0, 100)
            toast.show()
            val intent = Intent(context, ScoreActivity::class.java)
            intent.putExtra("score", score)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
            (context as Activity).finish()
            finished = false;
        }
        if (meteor1Y > canvasHeight) {
            meteor1Y = -120
            meteor1X = (floor(Math.random() * (maxShipX  )) ).toInt() - (meteor[0]!!.width)/2
            score += 5
        }
        if (meteor2Y > canvasHeight) {
            meteor2Y = -120
            meteor2X = (floor(Math.random() * (maxShipX  )) ).toInt() - (meteor[0]!!.width)/2
            score += 10
        }
        if (meteor3Y > canvasHeight) {
            meteor3Y = -120
            meteor3X = (floor(Math.random() * (maxShipX  )) ).toInt() - (meteor[0]!!.width)/2
            score += 25
        }
        if (meteor4Y > canvasHeight) {
            meteor4Y = -120
            meteor4X = (floor(Math.random() * (maxShipX  )) ).toInt() - (meteor[0]!!.width)/2
            score += 20
        }
        saveHighestScore(score)

        canvas.drawBitmap(meteor[0]!!, meteor1X.toFloat(), meteor1Y.toFloat(), null)
        canvas.drawBitmap(meteor[1]!!, meteor2X.toFloat(), meteor2Y.toFloat(), null)
        canvas.drawBitmap(meteor[2]!!, meteor3X.toFloat(), meteor3Y.toFloat(), null)
        canvas.drawBitmap(meteor[3]!!, meteor4X.toFloat(), meteor4Y.toFloat(), null)

        for (i in 0..2) {
            val x = (canvasWidth - 3 * (50 * 1.5) + life[0]!!.width * 1.5 * i).toInt()
            val y = 30
            if (i < lifeCounter) {
                canvas.drawBitmap(life[0]!!, x.toFloat(), y.toFloat(), null)
            } else {
                canvas.drawBitmap(life[1]!!, x.toFloat(), y.toFloat(), null)
            }
        }
        canvas.drawBitmap(ship[0]!!, shipX.toFloat(), shipY.toFloat(), null)
        canvas.drawText("High Scores : $highestScore", 20f, 50f, highScorePaint)

        canvas.drawText("Score : $score", 20f, 110f, scorePaint)

    }

    private fun saveHighestScore(score: Int) {
        // Update the highest score if the current score is higher
        if (score > highestScore) {
            highestScore = score
            // Save the highest score to SharedPreferences
            val editor = sharedPreferences.edit()
            editor.putInt("highestScore", highestScore)
            editor.apply()
        }
    }

    fun hitMeteorChecker(shipX: Int, shipY: Int, meteorX: Int, meteorY: Int): Boolean {
        val shipWidth = 4*(ship[0]!!.width )/5
        val shipHeight = 3*(ship[0]!!.height)/4
        val meteorWidth = 4*(meteor[0]!!.width)/5
        val meteorHeight = 4*(meteor[0]!!.height)/5

        // Check if the ship and meteor intersect by comparing their bounding boxes
        return if (shipX < meteorX + meteorWidth && shipX + shipWidth > meteorX && shipY < meteorY + meteorHeight && shipY + shipHeight > meteorY
        ) {
            // Collision detected
            true
        } else {
            // No collision
            false
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                val x = event.x
                val y = event.y
                // When the user initially touches the screen
                if (isInsideDraggableArea(x, y)) {
                    touch = true
                }
            }

            MotionEvent.ACTION_MOVE -> if (touch) {
                val touchX = event.x
                shipX = touchX.toInt()
            }

            MotionEvent.ACTION_UP -> touch = false
        }
        // Redraw the view to reflect the changes
        invalidate()
        // Return true to indicate that the touch event was handled
        return true
    }

    private fun isInsideDraggableArea(x: Float, y: Float): Boolean {
        return if (y > shipY - 10) {
            x < shipX + 130 && x > shipX -10
        } else {
            false
        }
    }
}
