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
        val intent = Intent()
        intent.setComponent(
            ComponentName(
                "com.android.settings",
                "com.android.settings.DeviceAdminSettings"
            )
        )
        context.startActivity(intent)
    }

    @SuppressLint("BatteryLife")
    fun background(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val intent = Intent()
            val packageName: String = context.packageName
            val pm = context.getSystemService(POWER_SERVICE) as PowerManager?
            if (!pm!!.isIgnoringBatteryOptimizations(packageName)) {
                intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS)
                intent.setData(Uri.parse("package:$packageName"))
                context.startActivity(intent)
            }
        }
    }

    fun notification(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            when {
                activity.shouldShowRequestPermissionRationale(android.Manifest.permission.POST_NOTIFICATIONS) -> {
                    val packageName: String = activity.packageName
                    val settingsIntent: Intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        .putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
                    activity.startActivity(settingsIntent)
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
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        with(intent) {
            data = Uri.fromParts("package", context.packageName, null)
            addCategory(Intent.CATEGORY_DEFAULT)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
            addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
        }
        context.startActivity(intent)
    }

    fun goSchedulePermissionSettingsScreen(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            Intent().also { intent ->
                intent.action = Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM
                intent.setData(Uri.fromParts("package", context.packageName, null))
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
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
}