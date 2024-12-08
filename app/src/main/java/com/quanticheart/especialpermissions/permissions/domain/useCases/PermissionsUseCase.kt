package com.quanticheart.especialpermissions.permissions.domain.useCases

import android.Manifest
import android.os.Build
import com.quanticheart.especialpermissions.permissions.domain.helpers.SystemPermission
import com.quanticheart.especialpermissions.permissions.domain.models.CommonPermissions
import com.quanticheart.especialpermissions.permissions.domain.models.Permission
import com.quanticheart.especialpermissions.permissions.domain.models.Permissions

class PermissionsUseCase(private val permission: SystemPermission) {

    suspend fun verifyPermission(): Permissions {
        val adminPermission = Permission(
            required = true,
            enabled = permission.isDeviceAdmin(),
        )

        val backgroundPermission = Permission(
            required = permission.apiRequireBackGround(),
            enabled = permission.runInBackGround()
        )

        val exactAlarm = Permission(
            required = permission.apiRequireExactAlarm(),
            enabled = permission.exactAlarm()
        )

        val notificationPermission = Permission(
            required = permission.apiRequireNotification(),
            enabled = permission.notifications()
        )

        val commons = permission.verifyCommonsPermissions(permissionList())

        val allOK =
            adminPermission.enabled &&
                    backgroundPermission.enabled &&
                    notificationPermission.enabled &&
                    exactAlarm.enabled &&
                    commons.first

        return Permissions(
            allEnabled = allOK,
            admin = adminPermission,
            background = backgroundPermission,
            notification = notificationPermission,
            exactAlarm = exactAlarm,
            commons = CommonPermissions(
                allEnabled = commons.first,
                listToRequire = commons.second,
            )
        )
    }

    suspend fun verifyCommonsPermission(): CommonPermissions {
        val commons = permission.verifyCommonsPermissions(permissionList())
        return CommonPermissions(
            allEnabled = commons.first,
            listToRequire = commons.second,
        )
    }

    private fun permissionList(): Array<String> {
        val permissions = mutableListOf<String>()

        // Permissões comuns
        permissions.addAll(
            listOf(
                Manifest.permission.CAMERA,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.ACCESS_WIFI_STATE,
            ),
        )
        // Adiciona permissões de bluetooth se a versão do SDK for superior ao Android 11 (R)
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.R) {
            permissions.addAll(
                listOf(
                    Manifest.permission.BLUETOOTH_SCAN,
                    Manifest.permission.BLUETOOTH_CONNECT,
                    Manifest.permission.BLUETOOTH_ADVERTISE,
                ),
            )
        } else {
            // Adiciona permissões de bluetooth se a versão do SDK for anterior ao Android 11 (R)
            permissions.addAll(
                listOf(
                    Manifest.permission.BLUETOOTH,
                    Manifest.permission.BLUETOOTH_ADMIN,
                ),
            )
        }

        // Adiciona permissões de armazenamento externo se a versão do SDK for anterior ao Android 11 (R)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            permissions.addAll(
                listOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                ),
            )
        }

        return permissions.toTypedArray()
    }

}