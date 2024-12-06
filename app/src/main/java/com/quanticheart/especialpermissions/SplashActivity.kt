package com.quanticheart.especialpermissions

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.quanticheart.especialpermissions.permissions.ui.PermissionsActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash)

        // Using a Handler to delay the navigation
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, PermissionsActivity::class.java))
            finish() // Finish the splash activity
        }, 1000) // Delay for 3 seconds (3000 milliseconds)
    }
}