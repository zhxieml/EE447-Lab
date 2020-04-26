package com.example.locateme.ui

import android.net.wifi.WifiInfo
import androidx.compose.Composable
import androidx.ui.core.Text
import androidx.ui.foundation.Border
import androidx.ui.foundation.Box
import androidx.ui.layout.*
import androidx.ui.material.MaterialTheme
import androidx.ui.unit.dp
import com.example.locateme.R
import com.example.locateme.ui.utils.VectorImage

@Composable
fun CurrentConnectionBox(connectionInfo: WifiInfo?) {
    val colors = MaterialTheme.colors()
    
    Box(
        backgroundColor = colors.background,
        border = Border(2.dp, color = colors.secondary)
    ){
        Row(modifier = LayoutPadding(all = 16.dp)) {
            VectorImage(
                modifier = LayoutGravity.Center,
                id = R.drawable.ic_wifi
            )

            Spacer(modifier = LayoutWidth(8.dp))
            Column(modifier = LayoutFlexible(1f)) {
                if (connectionInfo != null)
                    Text(text = "$connectionInfo")
            }
        }
    }
}