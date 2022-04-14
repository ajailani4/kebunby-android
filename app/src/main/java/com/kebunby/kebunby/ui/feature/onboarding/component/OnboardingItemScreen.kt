package com.kebunby.kebunby.ui.feature.onboarding.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kebunby.kebunby.ui.feature.onboarding.OnboardingItem
import com.kebunby.kebunby.ui.theme.PrimaryLight

@Composable
fun OnboardingItemScreen(item: OnboardingItem) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 30.dp)
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier.size(300.dp),
            painter = painterResource(id = item.image),
            contentDescription = "Onboarding illustration"
        )

        if (item.title != null) {
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = stringResource(id = item.title),
                textAlign = TextAlign.Center,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.h1
            )
            Spacer(modifier = Modifier.height(10.dp))
        }

        if (item.description != null) {
            Text(
                text = stringResource(id = item.description),
                textAlign = TextAlign.Center,
                color = Color.White,
                style = MaterialTheme.typography.h3
            )
        }
    }
}
