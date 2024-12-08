package com.quanticheart.especialpermissions.permissions.services

import android.app.AlarmManager.ACTION_SCHEDULE_EXACT_ALARM_PERMISSION_STATE_CHANGED
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import androidx.appcompat.app.AppCompatActivity

class ExactAlarmService : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            ACTION_SCHEDULE_EXACT_ALARM_PERMISSION_STATE_CHANGED -> {
//                Toast.makeText(context, "RECEIVED ALARM PERMISSION", Toast.LENGTH_LONG).show()
            }

            "from alarm" -> {
//                Toast.makeText(context, "ALARM FIRED", Toast.LENGTH_LONG).show()
            }
        }
    }
}

private var alarmReceiver: ExactAlarmService? = null
fun AppCompatActivity.registerExactAlarm() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        alarmReceiver = ExactAlarmService()
        registerReceiver(
            alarmReceiver,
            IntentFilter(ACTION_SCHEDULE_EXACT_ALARM_PERMISSION_STATE_CHANGED),
        )
    }
}

fun AppCompatActivity.destroyExactAlarm() {
    alarmReceiver?.let {
        unregisterReceiver(it)
    }
}