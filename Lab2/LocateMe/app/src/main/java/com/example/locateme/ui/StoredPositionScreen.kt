package com.example.locateme.ui

import android.util.Log
import androidx.compose.Composable
import androidx.compose.remember
import androidx.ui.core.Modifier
import androidx.ui.core.Opacity
import androidx.ui.foundation.VerticalScroller
import androidx.ui.layout.Column
import androidx.ui.layout.LayoutPadding
import androidx.ui.material.Divider
import androidx.ui.material.DrawerState
import androidx.ui.material.Scaffold
import androidx.ui.material.ScaffoldState
import androidx.ui.unit.dp
import com.example.locateme.R
import com.example.locateme.data.appTitle
import com.example.locateme.model.AccessPoint
import com.example.locateme.model.LocateMeStatus
import com.example.locateme.model.Position

@Composable
fun StoredPositionScreen(scaffoldState: ScaffoldState = remember { ScaffoldState() }) {
    Scaffold(
        scaffoldState = scaffoldState,
        topAppBar = {
            AppTopBar(
                title = appTitle,
                icon = R.drawable.ic_wifi,
                scaffoldState = scaffoldState
            )
        },
        drawerContent = {
            AppDrawer(
                currentScreen = LocateMeStatus.currentScreen,
                closeDrawer = {
                    scaffoldState.drawerState = DrawerState.Closed
                }
            )
        }
    ) {
        StoredPositionScreenBody(
            positions = LocateMeStatus.storedPositions
        )
    }
}

@Composable
private fun StoredPositionScreenBody(
    modifier: Modifier = Modifier.None,
    positions: List<Position>
) {
    Column {
        VerticalScroller {
            Column(modifier = modifier) {
                Log.i("Stored Position Screen", "# of positions: ${positions.size.toString()}")
                positions.forEach { position ->
                    PositionCard(position = position)
                    StoredPositionScreenDivider()
                }
            }
        }
    }
}

@Composable
private fun StoredPositionScreenDivider() {
    Opacity(0.08f) {
        Divider(modifier = LayoutPadding(start = 14.dp, top = 0.dp, end = 14.dp, bottom = 0.dp))
    }
}