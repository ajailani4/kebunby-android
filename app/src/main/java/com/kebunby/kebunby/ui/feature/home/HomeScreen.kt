package com.kebunby.kebunby.ui.feature.home

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.kebunby.kebunby.R
import com.kebunby.kebunby.ui.feature.home.component.HomeUserProfileShimmer
import com.kebunby.kebunby.ui.feature.home.component.PlantCategoryCard
import com.kebunby.kebunby.ui.feature.home.component.PlantMiniCard
import com.kebunby.kebunby.ui.feature.home.component.TitleSection
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    navController: NavController,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val onEvent = homeViewModel::onEvent
    val userProfileState = homeViewModel.userProfileState

    val coroutineScope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()

    Scaffold(scaffoldState = scaffoldState) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Column(modifier = Modifier.background(color = MaterialTheme.colors.primary)) {
                HomeHeader(
                    userProfileState = userProfileState,
                    coroutineScope = coroutineScope,
                    scaffoldState = scaffoldState
                )
                HomeContent()
            }
        }
    }
}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun HomeHeader(
    userProfileState: HomeState,
    coroutineScope: CoroutineScope,
    scaffoldState: ScaffoldState
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Observe user profile state
        when (userProfileState) {
            is HomeState.LoadingUserProfile -> {
                HomeUserProfileShimmer()
            }

            is HomeState.UserProfile -> {
                val user = userProfileState.user

                Column {
                    if (user != null) {
                        Text(
                            text = "${stringResource(id = R.string.hello)}, ${user.name.split(" ")[0]}",
                            color = MaterialTheme.colors.onPrimary,
                            fontWeight = FontWeight.SemiBold,
                            style = MaterialTheme.typography.h2
                        )
                    }

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
                    painter = if (user?.avatar != null) {
                        rememberImagePainter(user.avatar)
                    } else {
                        painterResource(id = R.drawable.img_default_ava)
                    },
                    contentScale = ContentScale.Crop,
                    contentDescription = "User avatar"
                )
            }

            is HomeState.FailUserProfile -> {
                LaunchedEffect(Unit) {
                    coroutineScope.launch {
                        userProfileState.message?.let { message ->
                            scaffoldState.snackbarHostState.showSnackbar(message)
                        }
                    }
                }
            }

            is HomeState.ErrorUserProfile -> {
                LaunchedEffect(Unit) {
                    coroutineScope.launch {
                        userProfileState.message?.let { message ->
                            scaffoldState.snackbarHostState.showSnackbar(message)
                        }
                    }
                }
            }

            else -> {}
        }
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
            TrendingSection()
            Spacer(modifier = Modifier.height(20.dp))
            ForBeginnerSection()
            Spacer(modifier = Modifier.height(20.dp))
            PlantCategorySection()
        }
    }
}

@Composable
fun TrendingSection() {
    TitleSection(
        title = stringResource(id = R.string.trending),
        isViewAllEnabled = true
    )
    Spacer(modifier = Modifier.height(10.dp))
    // PlantMiniCard()
}

@Composable
fun ForBeginnerSection() {
    TitleSection(
        title = stringResource(id = R.string.for_beginner),
        isViewAllEnabled = true
    )
    Spacer(modifier = Modifier.height(10.dp))
    // PlantMiniCard()
}

@Composable
fun PlantCategorySection() {
    TitleSection(title = stringResource(id = R.string.plant_categories))
    Spacer(modifier = Modifier.height(10.dp))
    // PlantCategoryCard()
}