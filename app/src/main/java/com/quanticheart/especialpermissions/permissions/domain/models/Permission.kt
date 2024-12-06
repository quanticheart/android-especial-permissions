package com.quanticheart.especialpermissions.permissions.domain.models

data class Permission(
    val required: Boolean = true,
    var enabled: Boolean = false
)