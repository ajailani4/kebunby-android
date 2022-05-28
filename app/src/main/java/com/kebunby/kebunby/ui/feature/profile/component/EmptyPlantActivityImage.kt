package com.kebunby.kebunby.ui.feature.profile.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kebunby.kebunby.R
import com.kebunby.kebunby.ui.theme.Grey

@Composable
fun EmptyPlantActivityImage(
    modifier: Modifier = Modifier,
    message: String
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier.size(190.dp),
            painter = painterResource(id = R.drawable.img_empty_plant_profile),
            contentDescription = "Empty plant illustration"
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = message,
            color = Grey,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.h4
        )
    }
}