package com.kebunby.kebunby.ui.feature.home.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import com.kebunby.kebunby.R
import com.kebunby.kebunby.ui.theme.Grey
import com.kebunby.kebunby.ui.theme.poppinsFamily

@Composable
fun TitleSection(
    modifier: Modifier = Modifier,
    title: String,
    isViewAllEnabled: Boolean = false
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            color = MaterialTheme.colors.onBackground,
            fontWeight = FontWeight.SemiBold,
            style = MaterialTheme.typography.h3
        )

        if (isViewAllEnabled) {
            ClickableText(
                text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            color = Grey,
                            fontFamily = poppinsFamily,
                            fontSize = 14.sp
                        )
                    ) {
                        append(stringResource(id = R.string.view_all))
                    }
                },
                onClick = {}
            )
        }
    }
}