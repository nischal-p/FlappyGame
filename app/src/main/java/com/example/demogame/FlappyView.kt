package com.example.demogame

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Handler
import android.view.MotionEvent
import android.view.View
import java.util.*
import kotlin.random.Random
import java.util.Calendar

class Bird ( val wide: Float, val high: Float) {
    var x : Float = wide / 4
    var y : Float = high / 2
    var radius : Float = 40.00f
    var speed : Float = 0f
    var acceleration : Float = 0.8f
    var thurst : Float = -25f
    var birdColor : String = "#FFFFFF"

    fun fall() {
        this.speed += this.acceleration
        this.speed *= 0.93f
        this.y += this.speed

        if (this.y < 0) {
            this.y = 0f
        }

        if (this.y > high) {
            this.y = high
            this.acceleration = 0.00f
            this.speed = 0.00f
        }
    }

    fun fly() {
        this.speed += this.thurst
    }

    fun hitBottom(): Boolean {
        return this.y >= high
    }

    fun draw(paint : Paint, canvas : Canvas?) {
        paint.color = Color.parseColor(birdColor)
        canvas?.drawCircle(this.x, this.y, this.radius, paint)
    }

}

class Pipe (val wide: Float, val high : Float) {
    val random = Random
    val topAllowedOpeningY : Float = high / 8
    val bottomAllowedOpeningY : Float = 6 * high / 8
    val openingY = topAllowedOpeningY +
            random.nextFloat() * (bottomAllowedOpeningY - topAllowedOpeningY)
    val openingSize = 300f
    var x : Float = wide
    val topPipeLength : Float = openingY
    val pipeWidth = 100f
    val pipeSpeed = 5f

    fun draw(paint: Paint, canvas: Canvas?) {
        paint.color = Color.parseColor("#FFFFFF")
        canvas?.drawRect(x, 0f, x + pipeWidth, topPipeLength, paint)
        canvas?.drawRect(x, openingY + openingSize, x + pipeWidth, high, paint)
    }

    fun moveLeft() {
        this.x -= this.pipeSpeed
    }

    fun isOnScreen(): Boolean {
        return this.x + this.pipeWidth >= 0
    }

    fun hitBird(bird: Bird): Boolean {
        if (bird.x + bird.radius/2 > this.x
            && bird.x - bird.radius/2 < this.x + this.pipeWidth
            && (bird.y - bird.radius/2 < this.topPipeLength
                    || bird.y + bird.radius/2 > openingY + openingSize)) {
            return true
        }
        return false
    }
}

class FlappyView(context: Context?, w: Float, h: Float, val username : String?) : View(context) {
    var timer : Timer
    var timehandler : Handler
    val paint = Paint()
    var sharedPref : SharedPreferences =
        (context as Activity).getSharedPreferences("high_scores", Context.MODE_PRIVATE)
    var editor : SharedPreferences.Editor

    val wide = w
    val high = h

    var score = 0

    var collisionHappened = false

    val bird : Bird = Bird(wide, high)
    var pipes : MutableList<Pipe> = mutableListOf()

    init {
        editor = sharedPref.edit()
        timer = Timer()
        timehandler = Handler()
        val timetask = object : TimerTask() {
            override fun run() {
                timehandler.post(Runnable {
                    //this redraws the canvas everytime the timer goes off
                    invalidate()

                    if (collisionHappened) {
                        timer.cancel()
                        //save the score
                        saveScore()
                    }
                })
            }
        }
        startTimer(timetask)
        runThread()
    }

    private fun runThread() {
        object : Thread() {
            override fun run() {
               while (!collisionHappened) {
                   (context as Activity).runOnUiThread{
                       pipes.add(Pipe(wide, high))
                       score += 1
                   }
                   sleep(2800)
               }
            }
        }.start()
    }

    fun startTimer(task: TimerTask) {
        timer = Timer()
        timer.schedule(task, 1, 10)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        //background
        paint.color = Color.parseColor("#03A9F4")
        canvas?.drawRect(0f, 0f, wide, high, paint)

        pipes.retainAll { it.isOnScreen() }

        pipes.forEach { pipe ->
            pipe.draw(paint, canvas)
            pipe.moveLeft()
            if (pipe.hitBird(bird)) {
                collisionHappened = true
            }
        }

        bird.draw(paint, canvas)
        bird.fall()
        if (bird.hitBottom()) {
            collisionHappened = true
        }

        paint.color = Color.parseColor("#ff0000")
        paint.textSize = 80f
        if (!collisionHappened) {
            canvas?.drawText(score.toString(), wide/2, 3*high/8, paint)
        } else {
            canvas?.drawText("final score: ${score.toString()}", wide/3,
                3*high/8, paint)
        }
    }

    fun saveScore() {
        editor.putString(Calendar.getInstance().time.toString(), "$username|${score.toString()}")
        editor.apply()
    }

    override fun onTouchEvent(e: MotionEvent): Boolean {
        when (e.actionMasked) {
            //when user lifts their finger
            MotionEvent.ACTION_DOWN -> bird.fly()
        }
        return true
    }

}