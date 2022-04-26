package com.kebunby.kebunby.ui.common.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
    navController: NavController,
    title: String,
    hasBackButton: Boolean = false,
    isBackImmediately: Boolean = true,
    onBackButtonClicked: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .background(color = MaterialTheme.colors.primary)
            .fillMaxWidth()
            .padding(16.dp),
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.CenterStart
        ) {
            if (hasBackButton) {
                IconButton(
                    modifier = Modifier.size(24.dp),
                    onClick = {
                        if (isBackImmediately) {
                            navController.navigateUp()
                        } else {
                            onBackButtonClicked()
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        tint = Color.White,
                        contentDescription = "Arrow Back"
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
    }
}