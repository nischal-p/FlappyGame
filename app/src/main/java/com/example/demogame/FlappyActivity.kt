package com.example.demogame

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_flappy.*

class FlappyActivity : AppCompatActivity() {
    var g : FlappyView? = null
    var username : String? = "default name"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.username = intent.getStringExtra("player_name").toString()
        println("name made it safely here: $username")
        newGame()
    }

    fun newGame() {
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val h = displayMetrics.heightPixels
        val w = displayMetrics.widthPixels
        g = FlappyView(this, w.toFloat(), h.toFloat(), this.username)
        setContentView(g)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu._menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            R.id.new_game -> {
               newGame()
                return true
            }
            R.id.diff_player -> {
                val i = Intent(this, MainActivity::class.java)
                this.startActivity(i)
                return true
            }
            R.id.leaderboard_menu -> {
                val i = Intent(this, LeaderboardPageActivity::class.java)
                this.startActivity(i)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}