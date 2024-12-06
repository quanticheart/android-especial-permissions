package com.quanticheart.especialpermissions

import com.quanticheart.especialpermissions.permissions.domain.helpers.SystemPermission
import com.quanticheart.especialpermissions.permissions.domain.useCases.PermissionsUseCase
import com.quanticheart.especialpermissions.permissions.ui.PermissionsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    factory { SystemPermission(get()) }
    factory { PermissionsUseCase(get()) }
    viewModel { PermissionsViewModel(get()) }
}