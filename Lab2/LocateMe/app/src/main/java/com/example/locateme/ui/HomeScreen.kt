package com.example.locateme.ui

import android.net.wifi.WifiInfo
import android.util.Log
import androidx.compose.Composable
import androidx.compose.remember
import androidx.compose.state
import androidx.ui.foundation.VerticalScroller
import androidx.ui.core.Modifier
import androidx.ui.core.Opacity
import androidx.ui.core.Text
import androidx.ui.foundation.Icon
import androidx.ui.layout.*
import androidx.ui.material.*
import androidx.ui.material.icons.Icons
import androidx.ui.material.icons.filled.Favorite
import androidx.ui.unit.dp
import com.example.locateme.R
import com.example.locateme.data.appTitle
import com.example.locateme.model.*
import com.example.locateme.net.sendFingerprint
//import com.example.locateme.net.ClientSocket
import com.example.locateme.ui.utils.VectorImage
import com.google.gson.Gson

@Composable
fun HomeScreen(scaffoldState: ScaffoldState = remember { ScaffoldState() }) {
    var dialogShow by state { false }
    val colors = MaterialTheme.colors()
    val typography = MaterialTheme.typography()
    
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
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    dialogShow = true
                },
                color = colors.secondary
            ) {
                IconButton(onClick = { dialogShow = true }) {
                    Icon(icon = Icons.Filled.Favorite)
                }
            }
        }
    ) {
        AccessPointListScreenBody(
            connectionInfo = LocateMeStatus.currentConnection,
            accessPoints = LocateMeStatus.accessPoints
        )
    }
    
    if (dialogShow) {
        var latitude by state { 0.5f }
        var longitude by state { 0.5f }

        AlertDialog(
            onCloseRequest = { dialogShow = false },
            text = {
                Column {
                    VectorImage(
                        modifier = LayoutGravity.Center,
                        id = R.drawable.ic_fingerprint
                    )

                    Column {
                        Row(modifier = LayoutGravity.Center) {
                            Text(
                                text = "Add Position",
                                style = typography.h5
                            )
                        }
                        
                        Spacer(modifier = LayoutHeight(4.dp))
                        
                        Row(modifier = LayoutGravity.Center) {
                            Text(
                                text = "What is your latitude? %.2f".format(latitude),
                                style = typography.subtitle1
                            )
                        }
                        Slider(
                            position = SliderPosition(latitude),
                            onValueChange = { newValue ->
                                latitude = newValue
                            }
                        )
                        
                        Row(modifier = LayoutGravity.Center) {
                            Text(
                                text = "What is your longitude? %.2f".format(longitude),
                                style = typography.subtitle1
                            )
                        }
                        Slider(
                            position = SliderPosition(longitude),
                            onValueChange = { newValue ->
                                longitude = newValue
                            }
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val fingerprint = Fingerprint(
                            location = Location(
                                latitude = latitude,
                                longitude = longitude
                            ),
                            accessPoints = LocateMeStatus.accessPoints
                        )
                        val jsonFingerprint = Gson().toJson(fingerprint)
                        var estimatedLocation = Location()
                        
                        val thread = Thread {
                            Log.i("Home Screen", "hello from ${Thread.currentThread().name}")

                            estimatedLocation = sendFingerprint(jsonFingerprint = jsonFingerprint)
                        }

                        thread.start()
                        thread.join(500)
                        
                        dialogShow = false
                    }
                ) {
                    Text(text = "Add")
                }
            }
        )
    }
}

@Composable
private fun AccessPointListScreenBody(
    modifier: Modifier = Modifier.None,
    connectionInfo: WifiInfo?,
    accessPoints: List<AccessPoint>
) {
    Column {
        CurrentConnectionBox(connectionInfo = connectionInfo)
        VerticalScroller {
            Column(modifier = modifier) {
                Log.i("Home Screen", "# of APs: " + accessPoints.size.toString())
                accessPoints.forEach { accessPoint ->
                    AccessPointCard(accessPoint = accessPoint)
                    HomeScreenDivider()
                }
            }
        }   
    }
}

@Composable
private fun HomeScreenDivider() {
    Opacity(0.08f) {
        Divider(modifier = LayoutPadding(start = 14.dp, top = 0.dp, end = 14.dp, bottom = 0.dp))
    }
}
