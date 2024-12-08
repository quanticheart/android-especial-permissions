package com.quanticheart.especialpermissions.permissions.ui

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.quanticheart.especialpermissions.MainActivity
import com.quanticheart.especialpermissions.databinding.ActivityPermissionsBinding
import com.quanticheart.especialpermissions.permissions.domain.helpers.Permissions
import com.quanticheart.especialpermissions.permissions.domain.helpers.Permissions.callPermissions
import com.quanticheart.especialpermissions.permissions.domain.models.CommonPermissions
import com.quanticheart.especialpermissions.permissions.domain.models.Permission
import org.koin.androidx.viewmodel.ext.android.viewModel

class PermissionsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPermissionsBinding
    private val viewModel: PermissionsViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPermissionsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.next.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        viewModel.permissions.observe(this) { permissions ->
            setupAdminPermission(permissions.admin)
            setupBackgroundPermission(permissions.background)
            setupNotificationPermission(permissions.notification)
            setupOthersPermission(permissions.commons)
            setupExactAlarmPermission(permissions.exactAlarm)
            setupButton(permissions.allEnabled)
        }
    }

    private fun setupAdminPermission(background: Permission) {
        if (background.required) {
            binding.pAdm.visibility = View.VISIBLE
            binding.switchAdm.apply {
                isChecked = background.enabled
            }
            binding.btnPAdm.setOnClickListener {
                if (!background.enabled)
                    Permissions.admin(this@PermissionsActivity)
            }
        } else {
            binding.pAdm.visibility = View.GONE
        }
    }

    private fun setupBackgroundPermission(background: Permission) {
        if (background.required) {
            binding.pBackground.visibility = View.VISIBLE
            binding.switchBackground.apply {
                isChecked = background.enabled
            }
            binding.btnPBackground.setOnClickListener {
                if (!background.enabled)
                    Permissions.background(this@PermissionsActivity)
            }
        } else {
            binding.pBackground.visibility = View.GONE
        }
    }

    private fun setupNotificationPermission(background: Permission) {
        if (background.required) {
            binding.pNotification.visibility = View.VISIBLE
            binding.switchNotification.apply {
                isChecked = background.enabled
            }
            binding.btnPNotification.setOnClickListener {
                if (!background.enabled)
                    Permissions.notification(this@PermissionsActivity)
            }
        } else {
            binding.pNotification.visibility = View.GONE
        }
    }

    private fun setupExactAlarmPermission(background: Permission) {
        if (background.required) {
            binding.pExactAlarm.visibility = View.VISIBLE
            binding.switchExactAlarm.apply {
                isChecked = background.enabled
            }
            binding.btnPExactAlarm.setOnClickListener {
                if (!background.enabled)
                    Permissions.exactAlarm(this@PermissionsActivity)
            }
        } else {
            binding.pExactAlarm.visibility = View.GONE
        }
    }

    private fun setupOthersPermission(background: CommonPermissions) {
        binding.switchOthers.apply {
            isChecked = background.allEnabled
        }
        binding.btnPOthers.setOnClickListener {
            if (!background.allEnabled)
                callPermissions(this@PermissionsActivity, background.listToRequire)
        }
    }

    private fun setupButton(allEnabled: Boolean) {
        binding.next.isEnabled = allEnabled
        if (allEnabled) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            Permissions.PERMISSION_REQUEST_CODE -> {
                // Check if all permissions were granted
                val allPermissionsGranted =
                    grantResults.all { it == PackageManager.PERMISSION_GRANTED }

                if (allPermissionsGranted) {
                    viewModel.verifyPermissions()
                } else {
//                    for (i in permissions.indices) {
//                        if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
//                            val deniedPermission = permissions[i]
//                        }
//                    }
                    Permissions.showModalForRequestPermission(this)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.verifyPermissions()
    }
}