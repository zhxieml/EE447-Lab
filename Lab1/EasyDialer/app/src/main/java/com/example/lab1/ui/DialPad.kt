package com.example.lab1.ui

import androidx.compose.Composable
import androidx.compose.Model
import androidx.core.content.ContextCompat
import androidx.ui.core.ContextAmbient
import androidx.ui.core.Modifier
import androidx.ui.core.Text
import androidx.ui.foundation.Border
import androidx.ui.foundation.Box
import androidx.ui.graphics.Color
import androidx.ui.layout.*
import androidx.ui.material.surface.Surface
import androidx.ui.tooling.preview.Preview
import androidx.ui.unit.dp
import com.example.lab1.R
import com.example.lab1.model.EasyDialerStatus
import com.example.lab1.ui.utils.DialFunctionalButton
import com.example.lab1.ui.utils.DialNumberButton
import com.example.lab1.ui.utils.mainThemeColors


@Composable
fun DialPad(
    modifier: Modifier = Modifier.None,
    numberTextState: NumberTextState = NumberTextState()
) {
    if (EasyDialerStatus.isCalling)
        makePhoneCall(context = ContextAmbient.current)

    Box(modifier = LayoutSize.Fill) {
        Column(modifier = modifier) {
            NumberText(numberTextState = numberTextState)

            Row(modifier = LayoutWidth.Fill, arrangement = Arrangement.Center) {
                DialNumberButton(text = "1", onClickAction = { numberTextState.textValue += "1" });
                DialNumberButton(text = "2", onClickAction = { numberTextState.textValue += "2" });
                DialNumberButton(text = "3", onClickAction = { numberTextState.textValue += "3" });
            }
            Row(modifier = LayoutWidth.Fill, arrangement = Arrangement.Center) {
                DialNumberButton(text = "4", onClickAction = { numberTextState.textValue += "4" });
                DialNumberButton(text = "5", onClickAction = { numberTextState.textValue += "5" });
                DialNumberButton(text = "6", onClickAction = { numberTextState.textValue += "6" });
            }
            Row(modifier = LayoutWidth.Fill, arrangement = Arrangement.Center) {
                DialNumberButton(text = "7", onClickAction = { numberTextState.textValue += "7" });
                DialNumberButton(text = "8", onClickAction = { numberTextState.textValue += "8" });
                DialNumberButton(text = "9", onClickAction = { numberTextState.textValue += "9" })
            }
            Row(modifier = LayoutWidth.Fill, arrangement = Arrangement.Center) {
                DialNumberButton(text = "*", onClickAction = { numberTextState.textValue += "*" });
                DialNumberButton(text = "0", onClickAction = { numberTextState.textValue += "0" });
                DialNumberButton(text = "#", onClickAction = { numberTextState.textValue += "#" });
            }
            Row(modifier = LayoutWidth.Fill, arrangement = Arrangement.Center) {
                DialFunctionalButton(
                    icon = R.drawable.ic_backspace,
                    onClick = {
                        numberTextState.textValue = numberTextState.textValue.dropLast(1)
                    }
                )

                Spacer(modifier = LayoutWidth(width = 100.dp) + LayoutPadding(4.dp))

                DialFunctionalButton(
                    icon = R.drawable.ic_call,
                    onClick = {
                        EasyDialerStatus.phoneNumber = numberTextState.textValue
                        EasyDialerStatus.isCalling = true
                    }
                )
            }
        }
    }
}

@Composable
fun NumberText(
    modifier: Modifier = LayoutWidth(width = 200.dp) + LayoutPadding(4.dp),
    numberTextState: NumberTextState
) {
    Surface(
        color = mainThemeColors.surface,
        modifier = LayoutPadding(16.dp)
    ) {
        Row(modifier = LayoutWidth.Fill, arrangement = Arrangement.Center) {
            Box(border = Border(2.dp, Color.Gray)) {
                Text(
                    text = numberTextState.textValue,
                    modifier = modifier,
                    maxLines = 1
                )
            }
        }
    }
}

@Model
class NumberTextState(var textValue: String = "")

@Preview
@Composable
fun PreviewNumberText() {
    NumberText(numberTextState = NumberTextState())
}

@Preview
@Composable
fun PreviewDialPadScreen() {
    DialPad()
}