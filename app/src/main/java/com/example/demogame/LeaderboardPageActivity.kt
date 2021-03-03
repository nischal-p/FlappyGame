package com.example.demogame

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_leaderboard_page.*
import androidx.recyclerview.widget.LinearLayoutManager

class LeaderboardPageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leaderboard_page)

        // creates a vertical linear layout manager
        leaderboard_recycler_view.layoutManager = LinearLayoutManager(this)

        //adapter with click listener
        leaderboard_recycler_view.adapter = LeaderboardAdapter(this)
    }
}