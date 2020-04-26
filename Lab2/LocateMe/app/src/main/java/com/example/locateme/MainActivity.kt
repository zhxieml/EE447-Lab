package com.example.locateme

import android.Manifest
import android.content.pm.PackageManager
import android.net.wifi.ScanResult
import android.net.wifi.WifiInfo
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.ui.core.setContent
import androidx.ui.material.MaterialTheme
import com.example.locateme.data.internetRequestCode
import com.example.locateme.data.wifiRequestCode
import com.example.locateme.model.AccessPoint
import com.example.locateme.model.LocateMeStatus
import com.example.locateme.model.WifiScanner

class MainActivity : AppCompatActivity() {
    private var wifiScannerHelper: WifiScanner? = null
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme() {
                AppContent()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        getInternetPermissions()
    }

    override fun onPause() {
        wifiScannerHelper?.unregisterReceiver(this)
        super.onPause()
    }
    
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        
        if (requestCode == wifiRequestCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setupWifiScanner()
            }
        }
        else if (requestCode == internetRequestCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getWifiPermissions()
            }
        }
    }

    private fun setupWifiScanner() {
        wifiScannerHelper = WifiScanner(::doOnResults)
        wifiScannerHelper?.setupWifiManager(applicationContext!!, this)
        wifiScannerHelper?.startScanner()
    }

    private fun doOnResults(connectionInfo: WifiInfo, results: List<ScanResult>) {
        LocateMeStatus.accessPoints.clear()
        Toast.makeText(this, "Updating with ${results.size} APs", Toast.LENGTH_LONG).show()

        for (result in results) {
            LocateMeStatus.accessPoints.add(
                AccessPoint(
                    BSSID = result.BSSID,
                    SSID = if ((result.SSID).isEmpty()) "<Hidden AP>" else result.SSID,
                    strength = result.level
                )
            )
        }
        
        LocateMeStatus.currentConnection = connectionInfo
    }
    
    private fun getInternetPermissions() {
        val permission = Manifest.permission.INTERNET
        
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this, 
                arrayOf(permission),
                internetRequestCode
            )
        }
        else {
            getWifiPermissions()
        }
    }

    private fun getWifiPermissions() {
        val permission = Manifest.permission.ACCESS_COARSE_LOCATION
        
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(permission),
                wifiRequestCode
            )
        }
        else {
            Toast.makeText(this, "Setting up Wifi Scanner", Toast.LENGTH_LONG).show()

            setupWifiScanner()
        }
    }
}
