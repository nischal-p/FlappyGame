package com.example.demogame

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.leaderboard_item.view.*

class LeaderboardAdapter(var activity: Activity) : RecyclerView.Adapter<LeaderboardViewHolder>() {
    var leaderboardList : List<LeaderboardItem>

    init {
        val sharedPref : SharedPreferences =
            activity.getSharedPreferences("high_scores", Context.MODE_PRIVATE)
        var allScores : MutableMap<String, String> = sharedPref.all as MutableMap<String, String>
        leaderboardList = allScores.map { (date, info) ->
            LeaderboardItem(info.split("|")[0], info.split("|")[1].toInt())
        }
        leaderboardList.sortedByDescending { it.score }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeaderboardViewHolder =
        LeaderboardViewHolder(
            LayoutInflater.from(activity).inflate(
                R.layout.leaderboard_item, parent, false))

    // binds the individual leaderboard info to the viewholder
    override fun onBindViewHolder(holder: LeaderboardViewHolder, position: Int) =
        holder.bind(leaderboardList[position])

    override fun getItemCount() = leaderboardList.size
}

class LeaderboardViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    var name: TextView = view.leaderboard_name
    var score: TextView = view.leaderboard_score
    var view: View = view

    // for binding leaderboard info to view
    fun bind(item: LeaderboardItem) {
        name.text = item.name
        score.text = item.score.toString()
    }
}
