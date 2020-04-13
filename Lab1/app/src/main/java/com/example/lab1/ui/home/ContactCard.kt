package com.example.lab1.ui.home

import androidx.compose.Composable
import androidx.ui.core.Text
import androidx.ui.foundation.Clickable
import androidx.ui.layout.Column
import androidx.ui.layout.LayoutPadding
import androidx.ui.layout.Row
import androidx.ui.material.EmphasisLevels
import androidx.ui.material.ProvideEmphasis
import androidx.ui.material.ripple.Ripple
import androidx.ui.tooling.preview.Preview
import androidx.ui.unit.dp
import com.example.lab1.data.contact1
import com.example.lab1.model.Contact
import com.example.lab1.ui.utils.ThemedPreview
import com.example.lab1.ui.utils.themeTypography

@Composable
fun ContactName(contact: Contact) {
    ProvideEmphasis(emphasis = EmphasisLevels().high) {
        Text(contact.firstName + " " + contact.lastName, style = themeTypography.subtitle1)
    }
}

@Composable
fun ContactPhoneNumber(contact: Contact) {
    Row {
        ProvideEmphasis(emphasis = EmphasisLevels().medium) {
            Text(text = contact.phoneNumber.toString(), style = themeTypography.body2)
        }
    }
}

@Composable
fun ContactCard(contact: Contact) {
    Ripple(bounded = true) {
        Clickable() {
            Row(modifier = LayoutPadding(all = 16.dp)) {
                Column(modifier = LayoutFlexible(1f)) {
                    ContactName(contact = contact)
                    ContactPhoneNumber(contact = contact)
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewContactCard() {
    ThemedPreview {
        ContactCard(contact = contact1)
    }
}