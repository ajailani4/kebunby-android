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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.kebunby.kebunby.R
import com.kebunby.kebunby.ui.common.BaseUIState
import com.kebunby.kebunby.ui.common.component.FullSizeProgressBar
import com.kebunby.kebunby.ui.feature.plant_detail.component.InfoSurface
import com.kebunby.kebunby.ui.feature.plant_detail.component.StepItem
import com.kebunby.kebunby.ui.theme.Grey
import com.kebunby.kebunby.ui.theme.Red
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
    val isFavorited = plantDetailViewModel.isFavorited
    val onFavoritePlant = plantDetailViewModel::onFavoritePlant

    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    Scaffold(scaffoldState = scaffoldState) {
        Box {
            // Observe plant detail state
            when (plantDetailState) {
                is BaseUIState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is BaseUIState.Success -> {
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
                                .padding(top = 20.dp, bottom = 80.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
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
                                }
                                Row {
                                    Icon(
                                        imageVector = EvaIcons.Fill.Heart,
                                        tint = Red,
                                        contentDescription = "Heart icon"
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "${plant.popularity}",
                                        color = MaterialTheme.colors.onBackground,
                                        fontWeight = FontWeight.SemiBold,
                                        style = MaterialTheme.typography.subtitle2
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(15.dp))
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
                                Spacer(modifier = Modifier.height(15.dp))
                            }

                            Spacer(modifier = Modifier.height(5.dp))

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
                                onClick = {
                                    onEvent(PlantDetailEvent.AddPlantingPlant)
                                }
                            ) {
                                Text(
                                    text = if (!plant.isPlanted) {
                                        stringResource(id = R.string.plant)
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
                                enabled = plantDetailState != BaseUIState.Loading,
                                onClick = {
                                    onEvent(PlantDetailEvent.AddPlantedPlant)
                                }
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

                is BaseUIState.Fail -> {
                    LaunchedEffect(Unit) {
                        coroutineScope.launch {
                            plantDetailState.message?.let { message ->
                                scaffoldState.snackbarHostState.showSnackbar(message)
                            }
                        }
                    }
                }

                is BaseUIState.Error -> {
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

        // Observe add favorite plant state
        when (addFavPlantState) {
            is BaseUIState.Error -> {
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
            is BaseUIState.Error -> {
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
            is BaseUIState.Loading -> {
                FullSizeProgressBar()
            }

            is BaseUIState.Success -> {
                LaunchedEffect(Unit) {
                    onEvent(PlantDetailEvent.LoadPlantDetail)
                }
            }

            is BaseUIState.Error -> {
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
            is BaseUIState.Loading -> {
                FullSizeProgressBar()
            }

            is BaseUIState.Success -> {
                LaunchedEffect(Unit) {
                    onEvent(PlantDetailEvent.LoadPlantDetail)
                }
            }

            is BaseUIState.Error -> {
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