package com.example.locateme.ui

import androidx.annotation.DrawableRes
import androidx.compose.Composable
import androidx.ui.core.Modifier
import androidx.ui.core.Text
import androidx.ui.foundation.shape.corner.RoundedCornerShape
import androidx.ui.layout.*
import androidx.ui.material.*
import androidx.ui.material.surface.Surface
import androidx.ui.unit.dp
import com.example.locateme.R
import com.example.locateme.data.appTitle
import com.example.locateme.model.LocateMeStatus
import com.example.locateme.model.Position
import com.example.locateme.model.Screen
import com.example.locateme.model.navigateTo
import com.example.locateme.net.getStoredPositions
import com.example.locateme.ui.utils.VectorImage

@Composable
fun AppDrawer(currentScreen: Screen, closeDrawer: () -> Unit) {
    val typography = MaterialTheme.typography()
    val colors = MaterialTheme.colors()

    Column(modifier = LayoutSize.Fill) {
        Spacer(modifier = LayoutHeight(24.dp))

        Row(modifier = LayoutPadding(16.dp)) {
            Text(
                text = appTitle,
                style = typography.h1
            )
        }

        Divider(color = colors.onSurface.copy(alpha = .2f))

        DrawerButton(
            typography = typography,
            colors = colors,
            icon = R.drawable.ic_home,
            label = "Home",
            isSelected = currentScreen == Screen.Home
        ) {
            navigateTo(Screen.Home)
            closeDrawer()
        }
        
        DrawerButton(
            typography = typography,
            colors = colors,
            icon = R.drawable.ic_favorite,
            label = "Stored Positions",
            isSelected = currentScreen == Screen.StoredAP
        ) {
            var tmpPositions = ArrayList<Position>()
            val thread = Thread { 
                tmpPositions.addAll(getStoredPositions()) 
            }
            
            thread.start()
            thread.join(500)
            
            LocateMeStatus.storedPositions.clear()
            LocateMeStatus.storedPositions.addAll(tmpPositions)
            
            navigateTo(Screen.StoredAP)
            closeDrawer()
        }
    }
}

@Composable
fun DrawerButton(
    modifier: Modifier = Modifier.None,
    colors: ColorPalette,
    typography: Typography,
    @DrawableRes icon: Int,
    label: String,
    isSelected: Boolean,
    onClickAction: () -> Unit
) {
    val textIconColor = if (isSelected) colors.primary else colors.onSurface.copy(alpha = 0.6f)
    val backgroundColor = if (isSelected) colors.primary.copy(alpha = 0.12f) else colors.surface

    val surfaceModifier = modifier +
            LayoutPadding(start = 8.dp, top = 8.dp, end = 8.dp, bottom = 8.dp) +
            LayoutWidth.Fill

    Surface(
        modifier = surfaceModifier,
        color = backgroundColor,
        shape = RoundedCornerShape(4.dp)
    ) {
        TextButton(
            onClick = onClickAction,
            modifier = LayoutWidth.Fill
        ) {
            Row(
                arrangement = Arrangement.Start,
                modifier = LayoutWidth.Fill
            ) {
                VectorImage(
                    modifier = LayoutGravity.Center,
                    id = icon,
                    tint = textIconColor
                )

                Spacer(modifier = LayoutWidth(16.dp))

                Text(
                    text = label,
                    style = typography.body2.copy(color = textIconColor)
                )
            }
        }
    }
}