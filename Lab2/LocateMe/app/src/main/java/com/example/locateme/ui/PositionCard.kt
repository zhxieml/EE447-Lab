package com.example.locateme.ui

import androidx.compose.Composable
import androidx.ui.core.Text
import androidx.ui.foundation.shape.corner.RoundedCornerShape
import androidx.ui.layout.*
import androidx.ui.material.EmphasisLevels
import androidx.ui.material.ProvideEmphasis
import androidx.ui.material.surface.Card
import androidx.ui.unit.dp
import com.example.locateme.R
import com.example.locateme.model.Position
import com.example.locateme.ui.utils.VectorImage

@Composable
fun PositionCard(position: Position) {
    Card(modifier = LayoutPadding(4.dp), shape = RoundedCornerShape(4.dp)) {
        Row(modifier = LayoutPadding(all = 16.dp)) {
            VectorImage(
                modifier = LayoutGravity.Center,
                id = R.drawable.ic_location
            )

            Spacer(modifier = LayoutWidth(8.dp))
            Column(modifier = LayoutFlexible(1f)) {
                PositionLocation(position = position)
                PositionNumOfAP(position = position)
            }
        }
    }
}

@Composable
fun PositionLocation(position: Position) {
    ProvideEmphasis(emphasis = EmphasisLevels().high) {
        Text("Latitude: %.2f\tLongitude: %.2f".format(position.latitude, position.longitude))
    }
}

@Composable
fun PositionNumOfAP(position: Position) {
    ProvideEmphasis(emphasis = EmphasisLevels().medium) {
        Text("${position.numOfAP} APs detected")
    }
}
