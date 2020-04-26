package com.example.locateme.ui

import androidx.compose.Composable
import androidx.ui.core.Text
import androidx.ui.foundation.shape.corner.RoundedCornerShape
import androidx.ui.layout.*
import androidx.ui.material.EmphasisLevels
import androidx.ui.material.ProvideEmphasis
import androidx.ui.material.surface.Card
import androidx.ui.tooling.preview.Preview
import androidx.ui.unit.dp
import com.example.locateme.R
import com.example.locateme.model.AccessPoint
import com.example.locateme.ui.utils.VectorImage

@Composable
fun AccessPointCard(accessPoint: AccessPoint) {
    Card(modifier = LayoutPadding(4.dp), shape = RoundedCornerShape(4.dp)) {
        Row(modifier = LayoutPadding(all = 16.dp)) {
            VectorImage(
                modifier = LayoutGravity.Center,
                id = R.drawable.ic_wifi
            )

            Spacer(modifier = LayoutWidth(8.dp))
            Column(modifier = LayoutFlexible(1f)) {
                AccessPointName(accessPoint = accessPoint)
                AccessPointBasicInfo(accessPoint = accessPoint)
            }
        }
    }
}

@Composable
fun AccessPointName(accessPoint: AccessPoint) {
    ProvideEmphasis(emphasis = EmphasisLevels().high) {
        Text("${accessPoint.SSID} (${accessPoint.BSSID})")
    }
}

@Composable
fun AccessPointBasicInfo(accessPoint: AccessPoint) {
    ProvideEmphasis(emphasis = EmphasisLevels().medium) {
        Text("${accessPoint.strength}dBm")
    }
}

@Preview
@Composable
fun PreviewAccessPointCard() {
    val accessPoint = AccessPoint(
        BSSID = "safdsd",
        SSID = "ChinaNet"
    )

    AccessPointCard(accessPoint = accessPoint)
}
