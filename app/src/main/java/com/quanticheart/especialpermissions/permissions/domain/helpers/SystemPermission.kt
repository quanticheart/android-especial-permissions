package com.quanticheart.especialpermissions.permissions.domain.helpers

import android.Manifest
import android.annotation.SuppressLint
import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.content.Context.POWER_SERVICE
import android.content.pm.PackageManager
import android.os.Build
import android.os.PowerManager
import androidx.core.content.ContextCompat
import com.quanticheart.especialpermissions.permissions.domain.receivers.DeviceAdminReceiver

class SystemPermission(private val context: Context) {

    fun isDeviceAdmin(): Boolean {
        val devicePolicyManager = context.getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
        val componentName = ComponentName(context, DeviceAdminReceiver::class.java)
        return devicePolicyManager.isAdminActive(componentName)
    }

    @SuppressLint("ObsoleteSdkInt")
    fun runInBackGround(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val packageName: String = context.packageName
            val pm = context.getSystemService(POWER_SERVICE) as PowerManager
            pm.isIgnoringBatteryOptimizations(packageName)
        } else true
    }

    @SuppressLint("ObsoleteSdkInt")
    fun apiRequireBackGround() =
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.M

    fun notifications(): Boolean {
        // This is only necessary for API level >= 33 (TIRAMISU)
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else true
    }

    @SuppressLint("ObsoleteSdkInt")
    fun apiRequireNotification() =
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU

    @SuppressLint("ObsoleteSdkInt")
    fun verifyCommonsPermissions(permissions: Array<String>): Pair<Boolean, List<String>> {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val listPermissionsRequired = ArrayList<String>()
            permissions.forEach { manifestPermission ->
                if (!Permissions.checkPermission(context, manifestPermission)) {
                    listPermissionsRequired.add(manifestPermission)
                }
            }
            return if (listPermissionsRequired.isEmpty()) {
                Pair(true, listOf())
            } else {
                Pair(false, listPermissionsRequired.toList())
            }
        }
        return Pair(true, listOf())
    }
}
