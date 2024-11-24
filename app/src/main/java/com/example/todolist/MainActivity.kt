package com.example.todolist

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.ImageView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnTaskList: Button = findViewById(R.id.btnTaskList)
        val btnTimerStopwatch: Button = findViewById(R.id.btnTimerStopwatch)
        val btnReminder: Button = findViewById(R.id.btnReminder)

        val navTaskList: ImageView = findViewById(R.id.navTaskList)
        val navTimerStopwatch: ImageView = findViewById(R.id.navTimerStopwatch)
        val navReminder: ImageView = findViewById(R.id.navReminder)
        val navHome: ImageView = findViewById(R.id.navHome)

        // Buttons
        btnTaskList.setOnClickListener {
            // Start TaskListActivity
            val intent = Intent(this, TaskListActivity::class.java)
            startActivity(intent)
        }

        btnTimerStopwatch.setOnClickListener {
            // Start TimerStopwatchActivity
            val intent = Intent(this, TimerStopwatchActivity::class.java)
            startActivity(intent)
        }

        btnReminder.setOnClickListener {
            // Start ReminderActivity instead of ManageReminderActivity
            val intent = Intent(this, ReminderActivity::class.java)
            startActivity(intent)
        }

        // Navigation Bar Icons
        navTaskList.setOnClickListener {
            // Start TaskListActivity
            val intent = Intent(this, TaskListActivity::class.java)
            startActivity(intent)
        }

        navTimerStopwatch.setOnClickListener {
            // Start TimerStopwatchActivity
            val intent = Intent(this, TimerStopwatchActivity::class.java)
            startActivity(intent)
        }

        navReminder.setOnClickListener {
            // Start ReminderActivity instead of ManageReminderActivity
            val intent = Intent(this, ReminderActivity::class.java)
            startActivity(intent)
        }

//        navHome.setOnClickListener {
//            // Start HomeActivity or any other appropriate activity
//            val intent = Intent(this, HomeActivity::class.java)
//            startActivity(intent)
//        }
    }
}
