package com.example.todolist

import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class ManageReminderActivity : AppCompatActivity() {

    private lateinit var edtReminderMessage: EditText
    private lateinit var txtSelectDate: TextView
    private lateinit var txtSelectTime: TextView
    private lateinit var btnUpdateReminder: Button
    private lateinit var btnDeleteReminder: Button

    private lateinit var sharedPreferences: SharedPreferences

    private var selectedDate: Calendar = Calendar.getInstance()
    private var selectedTime: Calendar = Calendar.getInstance()

    private var reminderPosition: Int = -1
    private var originalReminder: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_reminder)

        edtReminderMessage = findViewById(R.id.edtReminderMessage)
        txtSelectDate = findViewById(R.id.txtSelectDate)
        txtSelectTime = findViewById(R.id.txtSelectTime)
        btnUpdateReminder = findViewById(R.id.btnUpdateReminder)
        btnDeleteReminder = findViewById(R.id.btnDeleteReminder)

        sharedPreferences = getSharedPreferences("Reminders", MODE_PRIVATE)

        // Retrieve and set up the reminder details
        reminderPosition = intent.getIntExtra("position", -1)
        originalReminder = intent.getStringExtra("reminder")
        if (originalReminder != null) {
            populateReminderFields(originalReminder!!)
        }

        txtSelectDate.setOnClickListener {
            showDatePickerDialog()
        }

        txtSelectTime.setOnClickListener {
            showTimePickerDialog()
        }

        btnUpdateReminder.setOnClickListener {
            updateReminder()
        }

        btnDeleteReminder.setOnClickListener {
            deleteReminder()
        }

        setupNavigation()
    }

    private fun populateReminderFields(reminder: String) {
        val parts = reminder.split(" | ")
        if (parts.size == 3) {
            edtReminderMessage.setText(parts[0])
            txtSelectDate.text = parts[1]
            txtSelectTime.text = parts[2]
        }
    }

    private fun showDatePickerDialog() {
        val currentDate = Calendar.getInstance()
        DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                selectedDate.set(year, month, dayOfMonth)
                txtSelectDate.text = getString(R.string.selected_date, dayOfMonth, month + 1, year)
            },
            currentDate.get(Calendar.YEAR),
            currentDate.get(Calendar.MONTH),
            currentDate.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun showTimePickerDialog() {
        val currentTime = Calendar.getInstance()
        TimePickerDialog(
            this,
            { _, hourOfDay, minute ->
                selectedTime.set(Calendar.HOUR_OF_DAY, hourOfDay)
                selectedTime.set(Calendar.MINUTE, minute)
                txtSelectTime.text = getString(R.string.selected_time, hourOfDay, minute)
            },
            currentTime.get(Calendar.HOUR_OF_DAY),
            currentTime.get(Calendar.MINUTE),
            true
        ).show()
    }

    private fun updateReminder() {
        val reminderMessage = edtReminderMessage.text.toString()
        val date = txtSelectDate.text.toString()
        val time = txtSelectTime.text.toString()

        if (reminderMessage.isEmpty() || date == getString(R.string.select_date) || time == getString(R.string.select_time)) {
            Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val updatedReminder = "$reminderMessage | $date | $time"

        val reminders = sharedPreferences.getStringSet("reminders", null)?.toMutableSet() ?: mutableSetOf()
        reminders.remove(originalReminder)
        reminders.add(updatedReminder)

        sharedPreferences.edit().putStringSet("reminders", reminders).apply()

        Toast.makeText(this, "Reminder updated", Toast.LENGTH_SHORT).show()
        finish()
    }

    private fun deleteReminder() {
        val reminders = sharedPreferences.getStringSet("reminders", null)?.toMutableSet() ?: mutableSetOf()
        reminders.remove(originalReminder)

        sharedPreferences.edit().putStringSet("reminders", reminders).apply()

        Toast.makeText(this, "Reminder deleted", Toast.LENGTH_SHORT).show()
        finish()
    }

    private fun setupNavigation() {
        val navTaskList: ImageView = findViewById(R.id.navTaskList)
        val navTimer: ImageView = findViewById(R.id.navTimer)
        val navReminder: ImageView = findViewById(R.id.navReminder)
        val navHome: ImageView = findViewById(R.id.navHome)

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
}
