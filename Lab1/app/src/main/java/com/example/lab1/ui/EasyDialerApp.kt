package com.example.lab1.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.annotation.DrawableRes
import androidx.compose.Composable
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.ui.animation.Crossfade
import androidx.ui.core.ContextAmbient
import androidx.ui.core.Text
import androidx.ui.layout.*
import androidx.ui.material.*
import androidx.ui.material.surface.Surface
import androidx.ui.unit.dp
import com.example.lab1.R
import com.example.lab1.data.dialRequestCode
import com.example.lab1.data.permissionsNeeded
import com.example.lab1.model.EasyDialerStatus
import com.example.lab1.model.Screen
import com.example.lab1.model.navigateTo
import com.example.lab1.ui.utils.*

@Composable
fun EasyDialerApp() {
    checkPermissions(context = ContextAmbient.current)
    
    MaterialTheme(colors = mainThemeColors, typography = mainThemeTypography) {
        AppContent()
    }
}

@Composable
private fun AppContent() {
    Crossfade(current = EasyDialerStatus.currentScreen) { screen ->
        Surface(color = mainThemeColors.background) {
            HomeScreen()
        }
    }
}

@Composable
fun AppDrawer(currentScreen: Screen, closeDrawer: () -> Unit) {
    Column(modifier = LayoutSize.Fill) {
        Spacer(modifier = LayoutHeight(24.dp))
        
        Row(modifier = LayoutPadding(16.dp)) {
            Text(
                text = "Easy Dialer",
                style = mainThemeTypography.h1
            )
        }
        
        Divider(color = mainThemeColors.onSurface.copy(alpha = .2f))
        
        DrawerButton(
            icon = R.drawable.ic_home,
            label = "Home",
            isSelected = currentScreen == Screen.Home
        ) {
            navigateTo(Screen.Home)
            closeDrawer()
        }
    }
}

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
)}

@Composable
fun makePhoneCall() {
    val intent = Intent(Intent.ACTION_CALL)
    val context = ContextAmbient.current
    
    intent.data = Uri.parse("tel:${EasyDialerStatus.phoneNumber}")
    ActivityCompat.startActivityForResult(context as Activity, intent, dialRequestCode, null)
}

@Composable
fun checkPermissions(context: Context) {
    val isAllSatisfied = permissionsNeeded.all {
        ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
    }

    if (!isAllSatisfied) {
        ActivityCompat.requestPermissions(
            context as Activity,
            permissionsNeeded.toTypedArray(),
            10)
    }
}
