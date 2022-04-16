package com.kebunby.kebunby.ui.feature.home

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.kebunby.kebunby.R
import com.kebunby.kebunby.ui.feature.home.component.PlantMiniCard

@Composable
fun HomeScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Column(modifier = Modifier.background(color = MaterialTheme.colors.primary)) {
            HomeHeader()
            HomeContent()
        }
    }
}

@Composable
fun HomeHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = "${stringResource(id = R.string.hello)}, Anna",
                color = MaterialTheme.colors.onPrimary,
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.h2
            )
            Text(
                text = stringResource(id = R.string.have_a_nice_day),
                color = MaterialTheme.colors.onPrimary,
                style = MaterialTheme.typography.h3
            )
        }
        Image(
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape),
            painter = /*rememberImagePainter()*/painterResource(id = R.drawable.img_default_ava),
            contentDescription = "User ava"
        )
    }
}

@Composable
fun HomeContent() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        shape = RoundedCornerShape(topEnd = 30.dp),
        color = MaterialTheme.colors.background
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = stringResource(id = R.string.trending),
                color = MaterialTheme.colors.onBackground,
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.h3
            )
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}