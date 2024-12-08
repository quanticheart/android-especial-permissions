package com.quanticheart.especialpermissions.permissions.domain.models

data class Permissions(
    val allEnabled: Boolean = false,
    val admin: Permission = Permission(),
    val background: Permission = Permission(),
    val notification: Permission = Permission(),
    val exactAlarm: Permission = Permission(),
    val commons: CommonPermissions = CommonPermissions()
)