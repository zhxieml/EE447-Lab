package com.example.lab1.ui.contactList

import androidx.compose.Composable
import androidx.compose.remember
import androidx.ui.core.Modifier
import androidx.ui.core.Opacity
import androidx.ui.foundation.VerticalScroller
import androidx.ui.layout.Column
import androidx.ui.layout.LayoutPadding
import androidx.ui.material.*
import androidx.ui.unit.dp
import com.example.lab1.R
import com.example.lab1.data.appTitle
import com.example.lab1.entity.Contact
import com.example.lab1.ui.AppDrawer
import com.example.lab1.ui.AppTopBar
import com.example.lab1.ui.utils.EasyDialerStatus
import com.example.lab1.ui.utils.Screen
import com.example.lab1.ui.utils.navigateTo

@Composable
fun HomeScreen(scaffoldState: ScaffoldState = remember { ScaffoldState() }) {
    Column {
        Scaffold(
            scaffoldState = scaffoldState,
            drawerContent = {
                AppDrawer(
                    currentScreen = Screen.Home,
                    closeDrawer = { scaffoldState.drawerState = DrawerState.Closed }
                )   
            },
            topAppBar = { 
                AppTopBar(
                    title = appTitle, 
                    icon = R.drawable.ic_call, 
                    scaffoldState = scaffoldState
                ) 
            },
            floatingActionButton = {
                FloatingActionButton(text = "Dial", onClick = { navigateTo(Screen.DialPad) })
            }
        ) { modifier -> 
            HomeScreenBody(
                modifier = modifier, 
                contacts = EasyDialerStatus.contacts
            )
        }
    }
}

@Composable
private fun HomeScreenBody(
    modifier: Modifier = Modifier.None,
    contacts: List<Contact>
) {
    VerticalScroller {
        Column(modifier = modifier) {
            contacts.forEach { contact ->
                ContactCard(contact = contact)
                HomeScreenDivider()
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