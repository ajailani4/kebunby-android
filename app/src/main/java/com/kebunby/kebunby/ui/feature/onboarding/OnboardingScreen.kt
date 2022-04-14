package com.kebunby.kebunby.ui.feature.onboarding

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import com.kebunby.kebunby.R
import com.kebunby.kebunby.ui.Screen
import com.kebunby.kebunby.ui.feature.onboarding.component.OnboardingItemScreen
import com.kebunby.kebunby.ui.theme.PrimaryLight
import com.kebunby.kebunby.ui.theme.PrimaryVariantLight
import com.kebunby.kebunby.ui.theme.Red
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun OnboardingScreen(navController: NavController) {
    val onboardingItems = listOf(
        OnboardingItem.OnboardingScreen1,
        OnboardingItem.OnboardingScreen2,
        OnboardingItem.OnboardingScreen3,
        OnboardingItem.OnboardingScreen4
    )
    val pagerState = rememberPagerState(initialPage = 0)
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .background(color = PrimaryLight)
            .fillMaxSize()
            .padding(
                top = if (pagerState.currentPage != onboardingItems.size - 1) {
                    80.dp
                } else {
                    30.dp
                }
            )
    ) {
        AnimatedVisibility(visible = pagerState.currentPage == onboardingItems.size - 1) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(id = R.string.welcome),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.h1
                )
                Spacer(modifier = Modifier.height(10.dp))
                Image(
                    modifier = Modifier.size(240.dp, 70.dp),
                    painter = painterResource(id = R.drawable.logo_type_app),
                    contentDescription = "Logo type"
                )
            }
        }
        HorizontalPager(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            count = onboardingItems.size,
            state = pagerState
        ) { index ->
            OnboardingItemScreen(item = onboardingItems[index])
        }

        // Login and register button
        AnimatedVisibility(visible = pagerState.currentPage == onboardingItems.size - 1) {
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(15.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                    onClick = { navController.navigate(Screen.LoginScreen.route) }
                ) {
                    Text(
                        modifier = Modifier.padding(5.dp),
                        text = stringResource(id = R.string.login),
                        color = PrimaryLight,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.h3
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
                OutlinedButton(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(15.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),
                    border = BorderStroke(width = 1.dp, color = Color.White),
                    onClick = { navController.navigate(Screen.RegisterScreen.route) }
                ) {
                    Text(
                        modifier = Modifier.padding(5.dp),
                        text = stringResource(id = R.string.register),
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.h3
                    )
                }
            }
        }

        // Pager indicator
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
        ) {
            Column(modifier = Modifier.align(Alignment.CenterEnd)) {
                AnimatedVisibility(visible = pagerState.currentPage != onboardingItems.size - 1) {
                    TextButton(
                        onClick = {
                            coroutineScope.launch {
                                pagerState.scrollToPage(onboardingItems.size - 1)
                            }
                        }
                    ) {
                        Text(
                            text = stringResource(id = R.string.skip),
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold,
                            style = MaterialTheme.typography.subtitle1
                        )
                    }
                }
            }
            HorizontalPagerIndicator(
                modifier = Modifier.align(Alignment.Center),
                pagerState = pagerState,
                activeColor = Color.White,
                inactiveColor = PrimaryVariantLight
            )
        }
    }
}