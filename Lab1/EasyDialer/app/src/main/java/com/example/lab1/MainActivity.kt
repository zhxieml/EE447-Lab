package com.example.lab1

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.ui.core.setContent
import com.example.lab1.data.dialRequestCode
import com.example.lab1.ui.EasyDialerApp
import com.example.lab1.model.EasyDialerStatus

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EasyDialerApp()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        
        if (requestCode == dialRequestCode) {
            EasyDialerStatus.isCalling = false
            EasyDialerStatus.phoneNumber = ""
        }
    }
}
