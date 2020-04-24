package com.example.lab1.ui

import androidx.compose.Composable
import androidx.ui.core.ContextAmbient
import androidx.ui.core.Text
import androidx.ui.foundation.shape.corner.RoundedCornerShape
import androidx.ui.layout.*
import androidx.ui.material.Button
import androidx.ui.material.EmphasisLevels
import androidx.ui.material.ProvideEmphasis
import androidx.ui.material.surface.Card
import androidx.ui.tooling.preview.Preview
import androidx.ui.unit.dp
import com.example.lab1.model.Contact
import com.example.lab1.ui.utils.ThemedPreview
import com.example.lab1.ui.utils.VectorImage
import com.example.lab1.ui.utils.mainThemeTypography
import com.example.lab1.R
import com.example.lab1.model.EasyDialerStatus

@Composable
fun ContactName(contact: Contact) {
    ProvideEmphasis(emphasis = EmphasisLevels().high) {
        Text(contact.firstName + " " + contact.lastName, style = mainThemeTypography.subtitle1)
    }
}

@Composable
fun ContactPhoneNumber(contact: Contact) {
    if (EasyDialerStatus.isCalling)
        makePhoneCall(context = ContextAmbient.current)
    
    Row {
        ProvideEmphasis(emphasis = EmphasisLevels().medium) {
            Button(
                onClick = {
                    EasyDialerStatus.phoneNumber = contact.phoneNumber
                    EasyDialerStatus.isCalling = true 
                }, 
                children = {
                    Row {
                        VectorImage(id = R.drawable.ic_call_made)
                        
                        Spacer(modifier = LayoutWidth(6.dp))
                        
                        Text(text = contact.phoneNumber.toString(), style = mainThemeTypography.body2)
                    }
            })
        }
    }
}

@Composable
fun ContactCard(contact: Contact) {
    Card(modifier = LayoutPadding(4.dp), shape = RoundedCornerShape(4.dp)) {
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

@Preview
@Composable
fun PreviewContactCard() {
    val contact1 = Contact(
        firstName = "Zhihui",
        lastName = "Xie",
        phoneNumber = "11111111111"
    )
    
    ThemedPreview {
        ContactCard(contact = contact1)
    }
}