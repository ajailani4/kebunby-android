package com.kebunby.kebunby.ui.feature.plant_detail

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.kebunby.kebunby.R
import com.kebunby.kebunby.ui.common.UIState
import com.kebunby.kebunby.ui.common.component.CustomAlertDialog
import com.kebunby.kebunby.ui.common.component.FullSizeProgressBar
import com.kebunby.kebunby.ui.feature.plant_detail.component.InfoSurface
import com.kebunby.kebunby.ui.feature.plant_detail.component.StepItem
import com.kebunby.kebunby.ui.feature.plant_list.PlantListEvent
import com.kebunby.kebunby.ui.theme.Grey
import com.kebunby.kebunby.ui.theme.Red
import com.kebunby.kebunby.ui.theme.poppinsFamily
import com.kebunby.kebunby.util.Formatter
import compose.icons.EvaIcons
import compose.icons.SimpleIcons
import compose.icons.evaicons.Fill
import compose.icons.evaicons.Outline
import compose.icons.evaicons.fill.ArrowBack
import compose.icons.evaicons.fill.Heart
import compose.icons.evaicons.outline.Clock
import compose.icons.evaicons.outline.Heart
import compose.icons.simpleicons.Rainmeter
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoilApi::class)
@Composable
fun PlantDetailScreen(
    navController: NavController,
    plantDetailViewModel: PlantDetailViewModel = hiltViewModel()
) {
    val onEvent = plantDetailViewModel::onEvent
    val plantDetailState = plantDetailViewModel.plantDetailState
    val addFavPlantState = plantDetailViewModel.addFavPlantState
    val deleteFavPlantState = plantDetailViewModel.deleteFavPlantState
    val addPlantingPlantState = plantDetailViewModel.addPlantingPlantState
    val addPlantedPlantState = plantDetailViewModel.addPlantedPlantState

    val swipeRefreshing = plantDetailViewModel.swipeRefreshing
    val onSwipeRefreshingChanged = plantDetailViewModel::onSwipeRefreshingChanged
    val isFavorited = plantDetailViewModel.isFavorited
    val onFavoritePlant = plantDetailViewModel::onFavoritePlant
    val plantNowDialogVis = plantDetailViewModel.plantNowDialogVis
    val onPlantNowDialogVisChanged = plantDetailViewModel::onPlantNowDialogVisChanged
    val finishPlantingDlgVis = plantDetailViewModel.finishPlantingDlgVis
    val onFinishPlantingDlgVisChanged = plantDetailViewModel::onFinishPlantingDlgVisChanged

    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    Scaffold(scaffoldState = scaffoldState) {
        SwipeRefresh(
            state = rememberSwipeRefreshState(isRefreshing = swipeRefreshing),
            onRefresh = {
                onSwipeRefreshingChanged(true)
                onEvent(PlantDetailEvent.LoadPlantDetail)
            },
            indicator = { state, trigger ->
                SwipeRefreshIndicator(
                    state = state,
                    refreshTriggerDistance = trigger,
                    contentColor = MaterialTheme.colors.primary
                )
            }
        ) {
            Box {
                // Observe plant detail state
                when (plantDetailState) {
                    is UIState.Loading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    is UIState.Success -> {
                        onSwipeRefreshingChanged(false)

                        val plant = plantDetailState.data!!

                        if (isFavorited == null) onFavoritePlant(plant.isFavorited)

                        Column(
                            modifier = Modifier
                                .background(color = MaterialTheme.colors.background)
                                .fillMaxSize()
                                .verticalScroll(rememberScrollState())
                        ) {
                            // Plant Image
                            Box {
                                Image(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(240.dp),
                                    painter = rememberImagePainter(plant.image),
                                    contentScale = ContentScale.Crop,
                                    contentDescription = "Plant image"
                                )
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(20.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Button(
                                        modifier = Modifier.size(48.dp),
                                        shape = CircleShape,
                                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                                        contentPadding = PaddingValues(0.dp),
                                        onClick = { navController.navigateUp() }
                                    ) {
                                        Icon(
                                            imageVector = EvaIcons.Fill.ArrowBack,
                                            tint = Grey,
                                            contentDescription = "Back button"
                                        )
                                    }
                                    Button(
                                        modifier = Modifier.size(48.dp),
                                        shape = CircleShape,
                                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                                        contentPadding = PaddingValues(0.dp),
                                        onClick = {
                                            onFavoritePlant(!isFavorited!!)

                                            if (isFavorited == false) {
                                                onEvent(PlantDetailEvent.AddFavoritePlant)
                                            } else {
                                                onEvent(PlantDetailEvent.DeleteFavoritePlant)
                                            }
                                        }
                                    ) {
                                        Icon(
                                            imageVector = if (isFavorited == true) {
                                                EvaIcons.Fill.Heart
                                            } else {
                                                EvaIcons.Outline.Heart
                                            },
                                            tint = if (isFavorited == true) Red else Grey,
                                            contentDescription = "Love button"
                                        )
                                    }
                                }
                            }

                            // Short Info
                            Column(
                                modifier = Modifier
                                    .padding(horizontal = 20.dp)
                                    .padding(top = 15.dp, bottom = 80.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = Formatter.formatDate(plant.publishedOn),
                                        color = Grey,
                                        style = MaterialTheme.typography.body2
                                    )
                                    Row {
                                        Icon(
                                            imageVector = EvaIcons.Fill.Heart,
                                            tint = Red,
                                            contentDescription = "Heart icon"
                                        )
                                        Spacer(modifier = Modifier.width(5.dp))
                                        Text(
                                            text = "${plant.popularity}",
                                            color = MaterialTheme.colors.onBackground,
                                            fontWeight = FontWeight.SemiBold,
                                            style = MaterialTheme.typography.subtitle2
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(15.dp))
                                Text(
                                    text = plant.name,
                                    color = MaterialTheme.colors.onBackground,
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.h3
                                )
                                Text(
                                    text = plant.category,
                                    color = Grey,
                                    fontWeight = FontWeight.SemiBold,
                                    style = MaterialTheme.typography.body1
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = buildAnnotatedString {
                                        withStyle(
                                            style = SpanStyle(
                                                color = Grey,
                                                fontFamily = poppinsFamily,
                                                fontSize = 13.sp
                                            )
                                        ) {
                                            append(stringResource(id = R.string.by))
                                            append(": ")
                                        }

                                        append(plant.author)
                                    },
                                    color = MaterialTheme.colors.onBackground,
                                    style = MaterialTheme.typography.body2
                                )
                                Spacer(modifier = Modifier.height(20.dp))
                                Row {
                                    InfoSurface(
                                        icon = EvaIcons.Outline.Clock,
                                        info = plant.growthEst
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    InfoSurface(
                                        icon = SimpleIcons.Rainmeter,
                                        info = plant.wateringFreq
                                    )
                                }
                                Spacer(modifier = Modifier.height(20.dp))

                                // Tools and Materials
                                Text(
                                    text = stringResource(id = R.string.tools_and_materials),
                                    color = MaterialTheme.colors.onBackground,
                                    fontWeight = FontWeight.SemiBold,
                                    style = MaterialTheme.typography.subtitle2
                                )
                                Spacer(modifier = Modifier.padding(5.dp))

                                plant.tools.forEach { tool ->
                                    Text(
                                        text = tool,
                                        color = MaterialTheme.colors.onBackground,
                                        style = MaterialTheme.typography.body1
                                    )
                                    Spacer(modifier = Modifier.height(5.dp))
                                }

                                plant.materials.forEach { material ->
                                    Text(
                                        text = material,
                                        color = MaterialTheme.colors.onBackground,
                                        style = MaterialTheme.typography.body1
                                    )
                                    Spacer(modifier = Modifier.height(5.dp))
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                // Steps
                                Text(
                                    text = stringResource(id = R.string.steps),
                                    color = MaterialTheme.colors.onBackground,
                                    fontWeight = FontWeight.SemiBold,
                                    style = MaterialTheme.typography.subtitle2
                                )
                                Spacer(modifier = Modifier.padding(5.dp))

                                plant.steps.forEachIndexed { index, step ->
                                    StepItem(
                                        number = index + 1,
                                        step = step
                                    )
                                    Spacer(modifier = Modifier.height(10.dp))
                                }
                            }
                        }
                        Surface(
                            modifier = Modifier.align(Alignment.BottomCenter),
                            color = MaterialTheme.colors.background
                        ) {
                            if (!plant.isPlanting) {
                                Button(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(15.dp),
                                    shape = MaterialTheme.shapes.medium,
                                    enabled = !plant.isPlanted,
                                    onClick = { onPlantNowDialogVisChanged(true) }
                                ) {
                                    Text(
                                        text = if (!plant.isPlanted) {
                                            stringResource(id = R.string.plant_now)
                                        } else {
                                            stringResource(id = R.string.already_planted)
                                        },
                                        color = MaterialTheme.colors.onPrimary,
                                        fontWeight = FontWeight.Bold,
                                        style = MaterialTheme.typography.subtitle1
                                    )
                                }
                            } else if (plant.isPlanting) {
                                OutlinedButton(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(15.dp),
                                    shape = MaterialTheme.shapes.medium,
                                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),
                                    border = BorderStroke(
                                        width = 1.dp,
                                        color = MaterialTheme.colors.primary
                                    ),
                                    enabled = plantDetailState != UIState.Loading,
                                    onClick = { onFinishPlantingDlgVisChanged(true) }
                                ) {
                                    Text(
                                        text = stringResource(id = R.string.finish_planting),
                                        color = MaterialTheme.colors.primary,
                                        fontWeight = FontWeight.Bold,
                                        style = MaterialTheme.typography.subtitle1
                                    )
                                }
                            }
                        }
                    }

                    is UIState.Fail -> {
                        onSwipeRefreshingChanged(false)

                        LaunchedEffect(Unit) {
                            coroutineScope.launch {
                                plantDetailState.message?.let { message ->
                                    scaffoldState.snackbarHostState.showSnackbar(message)
                                }
                            }
                        }
                    }

                    is UIState.Error -> {
                        onSwipeRefreshingChanged(false)

                        LaunchedEffect(Unit) {
                            coroutineScope.launch {
                                plantDetailState.message?.let { message ->
                                    scaffoldState.snackbarHostState.showSnackbar(message)
                                }
                            }
                        }
                    }

                    else -> {}
                }
            }
        }

        // Plant now dialog
        if (plantNowDialogVis) {
            CustomAlertDialog(
                onVisibilityChanged = onPlantNowDialogVisChanged,
                title = stringResource(id = R.string.plant_now),
                message = stringResource(id = R.string.plant_now_confirm_msg),
                onConfirmed = {
                    onPlantNowDialogVisChanged(false)
                    onEvent(PlantDetailEvent.AddPlantingPlant)
                },
                onDismissed = { onPlantNowDialogVisChanged(false) }
            )
        }

        // Finish planting dialog
        if (finishPlantingDlgVis) {
            CustomAlertDialog(
                onVisibilityChanged = onFinishPlantingDlgVisChanged,
                title = stringResource(id = R.string.finish_planting),
                message = stringResource(id = R.string.finish_planting_confirm_msg),
                onConfirmed = {
                    onFinishPlantingDlgVisChanged(false)
                    onEvent(PlantDetailEvent.AddPlantedPlant)
                },
                onDismissed = { onFinishPlantingDlgVisChanged(false) }
            )
        }
        
        // Observe add favorite plant state
        when (addFavPlantState) {
            is UIState.Error -> {
                LaunchedEffect(Unit) {
                    coroutineScope.launch {
                        addFavPlantState.message?.let { message ->
                            scaffoldState.snackbarHostState.showSnackbar(message)
                        }
                    }
                }
            }

            else -> {}
        }

        // Observe delete favorite plant state
        when (deleteFavPlantState) {
            is UIState.Error -> {
                LaunchedEffect(Unit) {
                    coroutineScope.launch {
                        deleteFavPlantState.message?.let { message ->
                            scaffoldState.snackbarHostState.showSnackbar(message)
                        }
                    }
                }
            }

            else -> {}
        }

        // Observe add planting plant state
        when (addPlantingPlantState) {
            is UIState.Loading -> {
                FullSizeProgressBar()
            }

            is UIState.Success -> {
                LaunchedEffect(Unit) {
                    onEvent(PlantDetailEvent.LoadPlantDetail)
                }
            }

            is UIState.Error -> {
                LaunchedEffect(Unit) {
                    coroutineScope.launch {
                        addPlantingPlantState.message?.let { message ->
                            scaffoldState.snackbarHostState.showSnackbar(message)
                        }
                    }
                }
            }

            else -> {}
        }

        // Observe add planted plant state
        when (addPlantedPlantState) {
            is UIState.Loading -> {
                FullSizeProgressBar()
            }

            is UIState.Success -> {
                LaunchedEffect(Unit) {
                    onEvent(PlantDetailEvent.LoadPlantDetail)
                }
            }

            is UIState.Error -> {
                LaunchedEffect(Unit) {
                    coroutineScope.launch {
                        addPlantedPlantState.message?.let { message ->
                            scaffoldState.snackbarHostState.showSnackbar(message)
                        }
                    }
                }
            }

            else -> {}
        }
    }
}