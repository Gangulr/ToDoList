package com.example.todolist

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class TaskListActivity : AppCompatActivity() {

    private lateinit var listView: ListView
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var adapter: ArrayAdapter<String>
    private val tasksWithReminders = mutableListOf<String>()

    private lateinit var navTaskList: ImageView
    private lateinit var navTimer: ImageView
    private lateinit var navReminder: ImageView
    private lateinit var navHome: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_list)

        listView = findViewById(R.id.listView)
        sharedPreferences = getSharedPreferences("Reminders", MODE_PRIVATE)

        // Initialize navigation icons
        navTaskList = findViewById(R.id.navTaskList)
        navTimer = findViewById(R.id.navTimer)
        navReminder = findViewById(R.id.navReminder)
        navHome = findViewById(R.id.navHome)

        loadTasksAndReminders()

        // Set up the list adapter
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, tasksWithReminders)
        listView.adapter = adapter

        // Set up onClickListener to open ManageReminderActivity
        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedTask = tasksWithReminders[position]
            openManageReminderActivity(selectedTask, position)
        }

        // Set up navigation click listeners
        navTaskList.setOnClickListener {
            startActivity(Intent(this, TaskListActivity::class.java))
        }

        navTimer.setOnClickListener {
            startActivity(Intent(this, TimerStopwatchActivity::class.java))
        }

        navReminder.setOnClickListener {
            startActivity(Intent(this, ReminderActivity::class.java))
        }

        navHome.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    private fun loadTasksAndReminders() {
        // Retrieve reminders from SharedPreferences
        val reminders = sharedPreferences.getStringSet("reminders", null)

        // Clear the previous list to avoid duplication
        tasksWithReminders.clear()

        if (reminders != null && reminders.isNotEmpty()) {
            tasksWithReminders.addAll(reminders)
        } else {
            // Notify if no reminders exist
            Toast.makeText(this, "No reminders found.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun openManageReminderActivity(reminder: String, position: Int) {
        // Open ManageReminderActivity to manage the selected reminder
        val intent = Intent(this, ManageReminderActivity::class.java)
        intent.putExtra("reminder", reminder)  // Pass the reminder details
        intent.putExtra("position", position)  // Pass the position
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        // Reload the tasks and reminders when returning to this activity
        loadTasksAndReminders()
        adapter.notifyDataSetChanged()  // Notify the adapter of changes
    }
}
