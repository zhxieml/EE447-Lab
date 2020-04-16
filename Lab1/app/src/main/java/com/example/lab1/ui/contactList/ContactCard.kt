package com.example.lab1.ui.contactList

import androidx.compose.Composable
import androidx.compose.state
import androidx.ui.core.Text
import androidx.ui.foundation.Clickable
import androidx.ui.layout.*
import androidx.ui.material.Button
import androidx.ui.material.EmphasisLevels
import androidx.ui.material.ProvideEmphasis
import androidx.ui.material.ripple.Ripple
import androidx.ui.tooling.preview.Preview
import androidx.ui.unit.dp
import com.example.lab1.entity.Contact
import com.example.lab1.ui.utils.ThemedPreview
import com.example.lab1.ui.utils.VectorImage
import com.example.lab1.ui.utils.themeTypography
import com.example.lab1.R
import com.example.lab1.ui.MakePhoneCall
import com.example.lab1.ui.utils.EasyDialerStatus

@Composable
fun ContactName(contact: Contact) {
    ProvideEmphasis(emphasis = EmphasisLevels().high) {
        Text(contact.firstName + " " + contact.lastName, style = themeTypography.subtitle1)
    }
}

@Composable
fun ContactPhoneNumber(contact: Contact) {
    if (EasyDialerStatus.isCalling)
        MakePhoneCall(phoneNumber = contact.phoneNumber)
    
    Row {
        ProvideEmphasis(emphasis = EmphasisLevels().medium) {
            Button(
                onClick = { EasyDialerStatus.isCalling = true }, 
                children = {
                    Row {
                        VectorImage(id = R.drawable.ic_call_made)
                        
                        Spacer(modifier = LayoutWidth(6.dp))
                        
                        Text(text = contact.phoneNumber.toString(), style = themeTypography.body2)
                    }
            })
        }
    }
}

@Composable
fun ContactCard(
    contact: Contact
) {
    Ripple(bounded = true) {
        Clickable() {
            Row(modifier = LayoutPadding(all = 16.dp)) {
                VectorImage(
                    modifier = LayoutGravity.Center,
                    id = R.drawable.ic_profile_image
                )
                
                Spacer(modifier = LayoutWidth(8.dp))
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
    val contact1 = Contact(
        id = 0,
        firstName = "Zhihui",
        lastName = "Xie",
        phoneNumber = "11111111111",
        email = "fffffarmer@gmail.com",
        github = "fffffarmer"
    )
    
    ThemedPreview {
        ContactCard(contact = contact1)
    }
}