package com.quanticheart.especialpermissions.permissions.domain.helpers

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Context.POWER_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.PowerManager
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

@SuppressLint("ObsoleteSdkInt")
object Permissions {
    fun admin(context: Context) {
        with(Intent()) {
            component = ComponentName(
                "com.android.settings",
                "com.android.settings.DeviceAdminSettings"
            )
            setupDefaultIntent()
            context.startActivity(this)
        }
    }

    @SuppressLint("BatteryLife")
    fun background(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            with(Intent()) {
                val pm = context.getSystemService(POWER_SERVICE) as PowerManager?
                if (!pm!!.isIgnoringBatteryOptimizations(context.packageName)) {
                    action = Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
                    data = Uri.parse("package:${context.packageName}")
                    setupDefaultIntent()
                    context.startActivity(this)
                }
            }
        }
    }

    fun notification(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            when {
                activity.shouldShowRequestPermissionRationale(android.Manifest.permission.POST_NOTIFICATIONS) -> {
                    with(Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)) {
                        putExtra(Settings.EXTRA_APP_PACKAGE, activity.packageName)
                        setupDefaultIntent()
                        activity.startActivity(this)
                    }
                }

                else ->
                    ActivityCompat.requestPermissions(
                        activity,
                        arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                        PERMISSION_REQUEST_CODE
                    )
            }
        }
    }

    fun permissions(context: Context) {
        with(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)) {
            data = Uri.fromParts("package", context.packageName, null)
            setupDefaultIntent()
            context.startActivity(this)
        }
    }

    fun exactAlarm(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // Android 13 and above
            with(Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)) {
                data = Uri.parse("package:" + context.packageName)
                setupDefaultIntent()
                context.startActivity(this)
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) { // Android 12
            with(Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)) {
                setupDefaultIntent()
                context.startActivity(this)
            }
        }
    }

    const val PERMISSION_REQUEST_CODE = 10100
    fun callPermissions(activity: Activity, permissions: List<String>) {
        var rationale = false
        for (permission in permissions) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                rationale = true
            }
        }

        if (rationale)
            permissions(activity)
        else
            ActivityCompat.requestPermissions(
                activity,
                permissions.toTypedArray(),
                PERMISSION_REQUEST_CODE
            )
    }

    fun checkPermission(context: Context, permission: String) =
        ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED

    fun showModalForRequestPermission(activity: Activity) {
        val alertDialogBuilder = AlertDialog.Builder(activity)
        val alertDialogRational = alertDialogBuilder.setTitle("Permissões requeridas")
            .setMessage("Todas as permissoes são requeridas para o funcionamento do aplicativo")
            .setPositiveButton("Ir para configurações") { dialog, _ ->
                dialog.dismiss()
                permissions(activity)
            }
            .setCancelable(false)
            .create()
        alertDialogRational.show()
    }

    private fun Intent.setupDefaultIntent() {
        addCategory(Intent.CATEGORY_DEFAULT)
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
        addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
    }
}