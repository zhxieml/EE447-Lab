package com.example.locateme.model

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.wifi.ScanResult
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import android.os.Handler
import android.util.Log

class WifiScanner(
    private val doOnResults: (results: WifiInfo, List<ScanResult>) -> Unit
) {
    private val logTag = "WiFiScannerHelper"
    private lateinit var wifiManager: WifiManager
    
    private val wifiScanReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            Log.i(logTag, "Receive scanning results")
            
            intent?.getBooleanExtra(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION, false)?.let {
                if (it) 
                    scanSuccess()
                else 
                    scanFailure()
            }
        }
    }

    fun setupWifiManager(applicationContext: Context, context: Context) {
        wifiManager = applicationContext.applicationContext?.getSystemService(Context.WIFI_SERVICE) as WifiManager

        val intentFilter = IntentFilter()
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)
        context.registerReceiver(wifiScanReceiver, intentFilter)
    }

    fun unregisterReceiver(context: Context?) {
        context?.unregisterReceiver(wifiScanReceiver)
    }

    private fun scanSuccess() {
        val results = wifiManager.scanResults
        val connectionInfo = wifiManager.connectionInfo
        
        Log.i(logTag, "Result size: " + results.size.toString())
        doOnResults.invoke(connectionInfo, results)
    }

    private fun scanFailure() {
        // consider using old scan results
        val results = wifiManager.scanResults
        val connectionInfo = wifiManager.connectionInfo

        Log.i(logTag, "Result size: " + results.size.toString())
        doOnResults.invoke(connectionInfo, results)
    }

    fun startScanner(attachHandler: Boolean = true) {
        if (attachHandler) {
            val handler = Handler()
            val runnableCode = object : Runnable {
                override fun run() {
                    Log.i(logTag, "Start Scanning")

                    wifiManager.startScan()
                    handler.postDelayed(this, 5000)
                }
            }
            handler.post(runnableCode)
        } else {
            wifiManager.startScan()
        }
    }
}