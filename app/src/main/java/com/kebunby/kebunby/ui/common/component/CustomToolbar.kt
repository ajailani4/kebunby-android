package com.kebunby.kebunby.ui.common.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import compose.icons.EvaIcons
import compose.icons.evaicons.Fill
import compose.icons.evaicons.fill.ArrowBack

@Composable
fun CustomToolbar(
    title: String,
    hasBackButton: Boolean = false,
    onBackButtonClicked: () -> Unit = {},
    hasMenuIcon: Boolean = false,
    menuIcons: List<Pair<@Composable () -> Unit, () -> Unit>>? = null
) {
    Box(
        modifier = Modifier
            .background(color = MaterialTheme.colors.primary)
            .fillMaxWidth()
            .padding(16.dp),
    ) {
        if (hasBackButton) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterStart
            ) {
                IconButton(
                    modifier = Modifier.size(24.dp),
                    onClick = onBackButtonClicked
                ) {
                    Icon(
                        imageVector = EvaIcons.Fill.ArrowBack,
                        tint = Color.White,
                        contentDescription = "Back button"
                    )
                }
            }
        }

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                modifier = Modifier.sizeIn(maxWidth = 250.dp),
                text = title,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.h4
            )
        }

        if (hasMenuIcon) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterEnd
            ) {
                Row {
                    menuIcons?.forEach { icon ->
                        IconButton(
                            modifier = Modifier.size(24.dp),
                            onClick = icon.second
                        ) {
                            icon.first()
                        }
                    }
                }
            }
        }
    }
}