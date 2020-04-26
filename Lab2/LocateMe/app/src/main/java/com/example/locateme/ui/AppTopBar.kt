package com.example.locateme.ui

import androidx.annotation.DrawableRes
import androidx.compose.Composable
import androidx.ui.core.Text
import androidx.ui.material.DrawerState
import androidx.ui.material.ScaffoldState
import androidx.ui.material.TopAppBar
import com.example.locateme.ui.utils.VectorImageButton

@Composable
fun AppTopBar(
    title: String,
    @DrawableRes icon: Int,
    scaffoldState: ScaffoldState
) {
    TopAppBar(
        title = { Text(text = title) },
        navigationIcon = {
            VectorImageButton(icon) {
                scaffoldState.drawerState = DrawerState.Opened
            }
        }
    )
}