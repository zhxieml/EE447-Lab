package com.example.lab1.ui

import androidx.compose.Composable
import androidx.compose.remember
import androidx.compose.state
import androidx.ui.core.Modifier
import androidx.ui.core.Opacity
import androidx.ui.core.Text
import androidx.ui.core.TextField
import androidx.ui.foundation.Icon
import androidx.ui.foundation.VerticalScroller
import androidx.ui.foundation.shape.corner.RoundedCornerShape
import androidx.ui.graphics.Color
import androidx.ui.input.KeyboardType
import androidx.ui.layout.*
import androidx.ui.material.*
import androidx.ui.material.icons.Icons
import androidx.ui.material.icons.filled.Favorite
import androidx.ui.material.surface.Card
import androidx.ui.material.surface.Surface
import androidx.ui.text.TextFieldValue
import androidx.ui.unit.dp
import com.example.lab1.R
import com.example.lab1.data.appTitle
import com.example.lab1.model.Contact
import com.example.lab1.model.EasyDialerStatus
import com.example.lab1.model.Screen
import com.example.lab1.ui.utils.*

@Composable
fun HomeScreen(scaffoldState: ScaffoldState = remember { ScaffoldState() }) {
    val (bottomDrawerState, bottomDrawerOnStateChange) = state { DrawerState.Closed }
    var dialogShow by state { false }

    if (dialogShow) {
        var firstNameValue by state { TextFieldValue("Enter first name") }
        var lastNameValue by state { TextFieldValue("Enter last name") }
        var phoneNumberValue by state { TextFieldValue("Enter phone number")}
        
        AlertDialog(
            onCloseRequest = { dialogShow = false },
            text = {
                Column {
                    VectorImage(
                        modifier = LayoutGravity.Center,
                        id = R.drawable.ic_profile_image
                    )
                    
                    Card(shape = RoundedCornerShape(4.dp)) {
                        Column {
                            Surface(color = Color.LightGray, modifier = LayoutPadding(16.dp)) {
                                TextField(
                                    value = firstNameValue,
                                    modifier = LayoutPadding(16.dp) + LayoutWidth.Fill,
                                    keyboardType = KeyboardType.Text,
                                    onFocus = { firstNameValue = TextFieldValue("") },
                                    onValueChange = {
                                        firstNameValue = it
                                    }
                                )
                            }

                            Surface(color = Color.LightGray, modifier = LayoutPadding(16.dp)) {
                                TextField(
                                    value = lastNameValue,
                                    modifier = LayoutPadding(16.dp) + LayoutWidth.Fill,
                                    keyboardType = KeyboardType.Text,
                                    onFocus = { lastNameValue = TextFieldValue("") },
                                    onValueChange = {
                                        lastNameValue = it
                                    }
                                )
                            }

                            Surface(color = Color.LightGray, modifier = LayoutPadding(16.dp)) {
                                TextField(
                                    value = phoneNumberValue,
                                    modifier = LayoutPadding(16.dp) + LayoutWidth.Fill,
                                    onFocus = { phoneNumberValue = TextFieldValue("") },
                                    keyboardType = KeyboardType.Phone,
                                    onValueChange = {
                                        phoneNumberValue = it
                                    }
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        dialogShow = false
                        EasyDialerStatus.contacts.add(
                            Contact(
                                firstName = firstNameValue.text,
                                lastName = lastNameValue.text,
                                phoneNumber = phoneNumberValue.text
                            )
                        )
                    }
                ) {
                    Text(text = "Add")
                }
            }
        )
    }

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
                FloatingActionButton(
                    onClick = {
                         dialogShow = true
                    },
                    color = mainThemeColors.secondary
                ) {
                    IconButton(onClick = { dialogShow = true }) {
                        Icon(icon = Icons.Filled.Favorite)
                    }
                }
            }
        ) { modifier ->
            BottomDrawerLayout(
                drawerState = bottomDrawerState,
                onStateChange = bottomDrawerOnStateChange,
                drawerContent = { DialPad() }
            ) {
                ContactListScreenBody(
                    modifier = modifier,
                    contacts = EasyDialerStatus.contacts
                )
            }
        }
    }
}

@Composable
private fun ContactListScreenBody(
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