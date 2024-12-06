package com.quanticheart.especialpermissions.permissions.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.quanticheart.especialpermissions.permissions.domain.models.Permissions
import com.quanticheart.especialpermissions.permissions.domain.useCases.PermissionsUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PermissionsViewModel(private val permissionsUseCase: PermissionsUseCase) : ViewModel() {

    private val _permissions = MutableLiveData<Permissions>()
    val permissions: LiveData<Permissions> = _permissions

    fun verifyPermissions() {
        CoroutineScope(Dispatchers.IO).launch {
            val permissions = permissionsUseCase.verifyPermission()
            _permissions.postValue(permissions)
        }
    }
}