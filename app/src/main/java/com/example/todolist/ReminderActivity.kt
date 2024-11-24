package com.example.todolist

import android.app.DatePickerDialog
import android.app.TimePickerDialog
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

class ReminderActivity : AppCompatActivity() {

    private lateinit var edtReminderMessage: EditText
    private lateinit var txtSelectDate: TextView
    private lateinit var txtSelectTime: TextView
    private lateinit var btnCreateReminder: Button

    private var selectedDate: Calendar = Calendar.getInstance()
    private var selectedTime: Calendar = Calendar.getInstance()

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reminder)

        edtReminderMessage = findViewById(R.id.edtReminderMessage)
        txtSelectDate = findViewById(R.id.txtSelectDate)
        txtSelectTime = findViewById(R.id.txtSelectTime)
        btnCreateReminder = findViewById(R.id.btnCreateReminder)

        sharedPreferences = getSharedPreferences("Reminders", MODE_PRIVATE)

        txtSelectDate.setOnClickListener {
            showDatePickerDialog()
        }

        txtSelectTime.setOnClickListener {
            showTimePickerDialog()
        }

        btnCreateReminder.setOnClickListener {
            createReminder()
        }

        // Setup Navigation
        setupNavigation()
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

    private fun createReminder() {
        val reminderMessage = edtReminderMessage.text.toString()
        val date = txtSelectDate.text.toString()
        val time = txtSelectTime.text.toString()

        if (reminderMessage.isEmpty()) {
            Toast.makeText(this, R.string.error_empty_reminder_message, Toast.LENGTH_SHORT).show()
            return
        }
        if (date.isEmpty()) {
            Toast.makeText(this, R.string.error_empty_date, Toast.LENGTH_SHORT).show()
            return
        }
        if (time.isEmpty()) {
            Toast.makeText(this, R.string.error_empty_time, Toast.LENGTH_SHORT).show()
            return
        }

        val newReminder = "$reminderMessage | $date | $time"
        val reminders = sharedPreferences.getStringSet("reminders", mutableSetOf())?.toMutableSet() ?: mutableSetOf()

        reminders.add(newReminder)
        sharedPreferences.edit().putStringSet("reminders", reminders).apply()

        Toast.makeText(this, R.string.reminder_created, Toast.LENGTH_LONG).show()

        finish()  // Go back to MainActivity
    }

    private fun setupNavigation() {
        findViewById<ImageView>(R.id.navTaskList).setOnClickListener {
            startActivity(Intent(this, TaskListActivity::class.java))
        }

        findViewById<ImageView>(R.id.navTimer).setOnClickListener {
            startActivity(Intent(this, TimerStopwatchActivity::class.java))
        }

        findViewById<ImageView>(R.id.navReminder).setOnClickListener {
            // Current activity, no action needed
        }

//        findViewById<ImageView>(R.id.navHome).setOnClickListener {
//            startActivity(Intent(this, HomeActivity::class.java))
//        }
    }
}
