package com.example.locateme.net

import android.util.Log
import com.example.locateme.data.serverHost
import com.example.locateme.data.serverPort
import com.example.locateme.model.Location
import com.example.locateme.model.Position
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.net.InetAddress
import java.net.Socket

fun sendFingerprint(jsonFingerprint: String): Location {
    val clientSocket = Socket(serverHost, serverPort)
    val inputStream = clientSocket.getInputStream()
    val outputStream = clientSocket.getOutputStream()
    val localHost = InetAddress.getLocalHost()
    val itemType = object : TypeToken<Location>() {}.type
    val receipt: String

    Log.i("Send Fingerprint", "Connection: $serverHost(S) <--> $localHost(C)")
    
    outputStream.write(jsonFingerprint.toByteArray());
    outputStream.flush();
    clientSocket.shutdownOutput()
    
    receipt = String(inputStream.readBytes(), charset = charset("utf-8"))

    outputStream.close()
    inputStream.close()
    clientSocket.close()

    Log.i("Send Fingerprint", "Receive from server: $receipt")
    
    return Gson().fromJson(receipt, itemType)
}

fun getStoredPositions(): ArrayList<Position> {
    val clientSocket = Socket(serverHost, serverPort)
    val inputStream = clientSocket.getInputStream()
    val outputStream = clientSocket.getOutputStream()
    val receipt: String
    val storedPositions: ArrayList<Position>
    val itemType = object : TypeToken<ArrayList<Position>>() {}.type
    
    outputStream.write("retrieve".toByteArray())
    outputStream.flush();
    clientSocket.shutdownOutput()

    receipt = String(inputStream.readBytes(), charset = charset("utf-8"))
    
    outputStream.close()
    inputStream.close()
    clientSocket.close()

    storedPositions = Gson().fromJson(receipt, itemType)
    Log.i("Get Stored Position", "Receive from server: $storedPositions")
    
    return storedPositions
}
