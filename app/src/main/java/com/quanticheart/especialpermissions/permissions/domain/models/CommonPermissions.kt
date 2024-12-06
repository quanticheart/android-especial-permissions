package com.quanticheart.especialpermissions.permissions.domain.models

data class CommonPermissions(
    val allEnabled: Boolean = true,
    var listToRequire: List<String> = listOf()
)