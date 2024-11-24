package com.example.todolist

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.os.SystemClock
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat

class TimerStopwatchActivity : AppCompatActivity() {

    private lateinit var txtTimer: TextView
    private lateinit var txtStopwatch: TextView
    private lateinit var btnStartTimer: Button
    private lateinit var btnStopTimer: Button
    private lateinit var btnResetTimer: Button
    private lateinit var btnStartStopwatch: Button
    private lateinit var btnStopStopwatch: Button
    private lateinit var btnResetStopwatch: Button

    private var timer: CountDownTimer? = null
    private var stopwatchRunning = false
    private var stopwatchBase: Long = 0

    private val CHANNEL_ID = "timer_stopwatch_channel"
    private val REQUEST_CODE_NOTIFICATIONS = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timer_stopwatch)

        // Initialize UI elements
        txtTimer = findViewById(R.id.txtTimer)
        txtStopwatch = findViewById(R.id.txtStopwatch)
        btnStartTimer = findViewById(R.id.btnStartTimer)
        btnStopTimer = findViewById(R.id.btnStopTimer)
        btnResetTimer = findViewById(R.id.btnResetTimer)
        btnStartStopwatch = findViewById(R.id.btnStartStopwatch)
        btnStopStopwatch = findViewById(R.id.btnStopStopwatch)
        btnResetStopwatch = findViewById(R.id.btnResetStopwatch)

        // Create notification channel
        createNotificationChannel()

        // Check and request notification permission if needed
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), REQUEST_CODE_NOTIFICATIONS)
            }
        }

        // Set button listeners
        btnStartTimer.setOnClickListener { startTimer() }
        btnStopTimer.setOnClickListener { stopTimer() }
        btnResetTimer.setOnClickListener { resetTimer() }
        btnStartStopwatch.setOnClickListener { startStopwatch() }
        btnStopStopwatch.setOnClickListener { stopStopwatch() }
        btnResetStopwatch.setOnClickListener { resetStopwatch() }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Timer and Stopwatch Channel"
            val descriptionText = "Channel for Timer and Stopwatch notifications"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun showNotification(title: String, content: String) {
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification) // Ensure you have this icon
            .setContentTitle(title)
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true) // Dismiss the notification when tapped

        with(NotificationManagerCompat.from(this)) {
            notify(1, builder.build())
        }
    }

    private fun startTimer() {
        val timerDuration: Long = 60000 // 1 minute
        timer = object : CountDownTimer(timerDuration, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val secondsRemaining = millisUntilFinished / 1000
                txtTimer.text = String.format("Timer: %02d:%02d", secondsRemaining / 60, secondsRemaining % 60)
            }

            override fun onFinish() {
                txtTimer.text = "Timer: 00:00"
                showNotification("Timer Finished", "Your timer has completed.")
            }
        }.start()
    }

    private fun stopTimer() {
        timer?.cancel()
    }

    private fun resetTimer() {
        timer?.cancel()
        txtTimer.text = "Timer: 00:00"
    }

    private fun startStopwatch() {
        stopwatchBase = SystemClock.elapsedRealtime()
        stopwatchRunning = true
        updateStopwatch()
    }

    private fun updateStopwatch() {
        val elapsedMillis = SystemClock.elapsedRealtime() - stopwatchBase
        val seconds = (elapsedMillis / 1000) % 60
        val minutes = (elapsedMillis / (1000 * 60)) % 60
        val hours = (elapsedMillis / (1000 * 60 * 60)) % 24
        txtStopwatch.text = String.format("Stopwatch: %02d:%02d:%02d", hours, minutes, seconds)
        if (stopwatchRunning) {
            txtStopwatch.postDelayed({ updateStopwatch() }, 1000)
        }
    }

    private fun stopStopwatch() {
        stopwatchRunning = false
        showNotification("Stopwatch Stopped", "Your stopwatch has been stopped.")
    }

    private fun resetStopwatch() {
        stopwatchBase = SystemClock.elapsedRealtime()
        txtStopwatch.text = "Stopwatch: 00:00:00"
        stopwatchRunning = false
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_NOTIFICATIONS) {
            if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                // Handle the case where permission was not granted
            }
        }
    }
}
