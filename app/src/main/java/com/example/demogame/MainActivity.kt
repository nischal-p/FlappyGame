package com.example.demogame

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var sharedPref : SharedPreferences =
            this.getSharedPreferences("high_scores", Context.MODE_PRIVATE)
        var editor : SharedPreferences.Editor = sharedPref.edit()


        flappy.setOnClickListener {
            if (name_field.text.isNotEmpty()) {
                val username: String = name_field.text.toString()
                println("name from text field: $username")
                val intent = Intent(this, FlappyActivity::class.java)
                intent.putExtra("player_name", username)
                startActivity(intent)
            } else {
                name_field.hint = "Please enter a name first"
            }
        }

        leaderboard_btn.setOnClickListener {
            val intent = Intent(this, LeaderboardPageActivity::class.java)
            startActivity(intent)
        }

        clear_leaderboard_btn.setOnClickListener {
            val dialogBuilder = AlertDialog.Builder(this)

            // set message of alert dialog
            dialogBuilder.setMessage(R.string.alert_message)
                // if the dialog is cancelable
                .setCancelable(false)
                // positive button text and action
                .setPositiveButton(R.string.yes, DialogInterface.OnClickListener {
                        dialog, id ->
                    editor.clear()
                    editor.apply()

                })
                // negative button text and action
                .setNegativeButton(R.string.no, DialogInterface.OnClickListener {
                        dialog, id -> dialog.cancel()
                })

            // create dialog box
            val alert = dialogBuilder.create()
            // set title for alert dialog box
            alert.setTitle(R.string.confirmation)
            // show alert dialog
            alert.show()
        }

    }



}