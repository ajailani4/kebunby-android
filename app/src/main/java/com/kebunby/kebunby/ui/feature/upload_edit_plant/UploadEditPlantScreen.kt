package com.kebunby.kebunby.ui.feature.upload_edit_plant

import android.app.Activity
import android.content.Context
import android.view.WindowManager
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.kebunby.kebunby.R
import com.kebunby.kebunby.data.model.PlantCategory
import com.kebunby.kebunby.ui.Screen
import com.kebunby.kebunby.ui.common.UIState
import com.kebunby.kebunby.ui.common.component.CustomToolbar
import com.kebunby.kebunby.ui.common.component.FullSizeProgressBar
import com.kebunby.kebunby.ui.feature.camera.CameraScreen
import com.kebunby.kebunby.ui.theme.Red
import com.kebunby.kebunby.ui.theme.poppinsFamily
import com.kebunby.kebunby.util.ListAction
import compose.icons.EvaIcons
import compose.icons.evaicons.Fill
import compose.icons.evaicons.Outline
import compose.icons.evaicons.fill.ArrowDown
import compose.icons.evaicons.fill.PlusCircle
import compose.icons.evaicons.outline.Close
import id.zelory.compressor.Compressor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.io.File

@Composable
fun UploadEditPlantScreen(
    navController: NavController,
    uploadEditPlantViewModel: UploadEditPlantViewModel = hiltViewModel()
) {
    val plantId = uploadEditPlantViewModel.plantId!!
    val onEvent = uploadEditPlantViewModel::onEvent
    val plantDetailState = uploadEditPlantViewModel.plantDetailState.value
    val plantCategoriesState = uploadEditPlantViewModel.plantCategoriesState.value
    val uploadPlantState = uploadEditPlantViewModel.uploadPlantState.value
    val cameraScreenVis = uploadEditPlantViewModel.cameraScreenVis.value
    val onCameraScreenVisChanged = uploadEditPlantViewModel::onCameraScreenVisChanged
    val photo = uploadEditPlantViewModel.photo.value
    val onPhotoChanged = uploadEditPlantViewModel::onPhotoChanged
    val plantName = uploadEditPlantViewModel.plantName.value
    val onPlantNameChanged = uploadEditPlantViewModel::onPlantNameChanged
    val selectedCategory = uploadEditPlantViewModel.selectedCategory.value
    val onSelectedCategoryChanged = uploadEditPlantViewModel::onSelectedCategoryChanged
    val categorySpinnerVis = uploadEditPlantViewModel.categorySpinnerVis.value
    val onCategorySpinnerVisChanged = uploadEditPlantViewModel::onCategorySpinnerVisChanged
    val growthEst = uploadEditPlantViewModel.growthEst.value
    val onGrowEstChanged = uploadEditPlantViewModel::onGrowthEstChanged
    val wateringFreq = uploadEditPlantViewModel.wateringFreq.value
    val onWateringFreqChanged = uploadEditPlantViewModel::onWateringFreqChanged
    val desc = uploadEditPlantViewModel.desc.value
    val onDescChanged = uploadEditPlantViewModel::onDescChanged
    val tools = uploadEditPlantViewModel.tools
    val onToolsChanged = uploadEditPlantViewModel::onToolsChanged
    val setTools = uploadEditPlantViewModel::setTools
    val materials = uploadEditPlantViewModel.materials
    val onMaterialsChanged = uploadEditPlantViewModel::onMaterialsChanged
    val setMaterials = uploadEditPlantViewModel::setMaterials
    val steps = uploadEditPlantViewModel.steps
    val onStepsChanged = uploadEditPlantViewModel::onStepsChanged
    val setSteps = uploadEditPlantViewModel::setSteps

    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    val context = LocalContext.current
    (context as Activity).window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            if (!cameraScreenVis) {
                CustomToolbar(
                    title = if (plantId > 0) {
                        stringResource(id = R.string.edit_plant)
                    } else {
                        stringResource(id = R.string.upload_plant)
                    },
                    hasBackButton = true,
                    onBackButtonClicked = { navController.navigateUp() }
                )
            }
        }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            PlantForm(
                onEvent = onEvent,
                photo = photo,
                plantName = plantName,
                onPlantNameChanged = onPlantNameChanged,
                plantCategoriesState = plantCategoriesState,
                selectedCategory = selectedCategory,
                onSelectedCategoryChanged = onSelectedCategoryChanged,
                categorySpinnerVis = categorySpinnerVis,
                onCategorySpinnerVisChanged = onCategorySpinnerVisChanged,
                growthEst = growthEst,
                onGrowEstChanged = onGrowEstChanged,
                wateringFreq = wateringFreq,
                onWateringFreqChanged = onWateringFreqChanged,
                desc = desc,
                onDescChanged = onDescChanged,
                tools = tools,
                onToolsChanged = onToolsChanged,
                materials = materials,
                onMaterialsChanged = onMaterialsChanged,
                steps = steps,
                onStepsChanged = onStepsChanged,
                coroutineScope = coroutineScope,
                scaffoldState = scaffoldState,
                context = context
            )
            AnimatedVisibility(
                visible = cameraScreenVis,
                exit = shrinkVertically()
            ) {
                CameraScreen(
                    onBackButtonClicked = { navController.navigateUp() },
                    onImageCaptured = { photo ->
                        coroutineScope.launch {
                            onPhotoChanged(Compressor.compress(context, photo))
                            onCameraScreenVisChanged(false)
                        }
                    },
                    scaffoldState = scaffoldState,
                    coroutineScope = coroutineScope
                )
            }
        }

        if (plantId > 0) {
            // Observe plant detail state
            when (plantDetailState) {
                is UIState.Loading -> {
                    FullSizeProgressBar()
                }

                is UIState.Success -> {
                    val plant = plantDetailState.data

                    // Setup values for PlantForm
                    if (plant != null) {
                        onPhotoChanged(plant.image)
                        onPlantNameChanged(plant.name)
                        onSelectedCategoryChanged(plant.category)
                        onGrowEstChanged(plant.growthEst)
                        onWateringFreqChanged(plant.wateringFreq)
                        onDescChanged(plant.desc)
                        setTools(plant.tools)
                        setMaterials(plant.materials)
                        setSteps(plant.steps)
                    }

                    onEvent(UploadEditPlantEvent.Idle)
                }

                is UIState.Fail -> {
                    LaunchedEffect(Unit) {
                        coroutineScope.launch {
                            plantDetailState.message?.let { message ->
                                scaffoldState.snackbarHostState.showSnackbar(message)
                            }
                        }
                    }
                }

                is UIState.Error -> {
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

        // Observe upload plant state
        when (uploadPlantState) {
            is UIState.Loading -> {
                FullSizeProgressBar()
            }

            is UIState.Success -> {
                LaunchedEffect(Unit) {
                    navController.navigate(Screen.ProfileScreen.route) {
                        popUpTo(Screen.ProfileScreen.route) {
                            inclusive = true
                        }
                    }
                }
            }

            is UIState.Fail -> {
                LaunchedEffect(Unit) {
                    coroutineScope.launch {
                        uploadPlantState.message?.let { message ->
                            scaffoldState.snackbarHostState.showSnackbar(message)
                        }
                    }
                }
            }

            is UIState.Error -> {
                LaunchedEffect(Unit) {
                    coroutineScope.launch {
                        uploadPlantState.message?.let { message ->
                            scaffoldState.snackbarHostState.showSnackbar(message)
                        }
                    }
                }
            }

            else -> {}
        }
    }
}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun PlantForm(
    onEvent: (UploadEditPlantEvent) -> Unit,
    photo: Any?,
    plantName: String,
    onPlantNameChanged: (String) -> Unit,
    plantCategoriesState: UIState<List<PlantCategory>>,
    selectedCategory: PlantCategory?,
    onSelectedCategoryChanged: (PlantCategory) -> Unit,
    categorySpinnerVis: Boolean,
    onCategorySpinnerVisChanged: (Boolean) -> Unit,
    growthEst: String,
    onGrowEstChanged: (String) -> Unit,
    wateringFreq: String,
    onWateringFreqChanged: (String) -> Unit,
    desc: String,
    onDescChanged: (String) -> Unit,
    tools: List<String>,
    onToolsChanged: (Int?, String?, ListAction) -> Unit,
    materials: List<String>,
    onMaterialsChanged: (Int?, String?, ListAction) -> Unit,
    steps: List<String>,
    onStepsChanged: (Int?, String?, ListAction) -> Unit,
    coroutineScope: CoroutineScope,
    scaffoldState: ScaffoldState,
    context: Context
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            if (photo != null) {
                Image(
                    modifier = Modifier
                        .fillMaxWidth()
                        .sizeIn(maxHeight = 400.dp)
                        .clip(MaterialTheme.shapes.medium),
                    painter = rememberImagePainter(photo),
                    contentScale = ContentScale.Crop,
                    contentDescription = "Plant photoFile"
                )
            }

            Spacer(modifier = Modifier.height(25.dp))

            // Plant Name
            Text(
                text = stringResource(id = R.string.plant_name),
                color = MaterialTheme.colors.onBackground,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.body1
            )
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = plantName,
                onValueChange = onPlantNameChanged,
                colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.Transparent),
                singleLine = true,
                textStyle = TextStyle(
                    color = MaterialTheme.colors.onBackground,
                    fontFamily = poppinsFamily,
                    fontSize = 14.sp
                )
            )
            Spacer(modifier = Modifier.height(25.dp))

            // Category
            // Observe plant categories state
            when (plantCategoriesState) {
                is UIState.Success -> {
                    val plantCategories = plantCategoriesState.data
                    var boxSize by remember { mutableStateOf(Size.Zero) }

                    if (plantCategories != null) {
                        if (selectedCategory == null) {
                            onSelectedCategoryChanged(plantCategories[0])
                        }

                        Text(
                            text = stringResource(id = R.string.category),
                            color = MaterialTheme.colors.onBackground,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.body1
                        )
                        Box(
                            modifier = Modifier.onGloballyPositioned { layoutCoordinates ->
                                boxSize = layoutCoordinates.size.toSize()
                            }
                        ) {
                            TextField(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable(onClick = {
                                        onCategorySpinnerVisChanged(true)
                                    }),
                                value = if (plantCategories.isNotEmpty() && selectedCategory != null) {
                                    selectedCategory.category
                                } else {
                                    ""
                                },
                                onValueChange = {},
                                trailingIcon = {
                                    Icon(
                                        imageVector = EvaIcons.Fill.ArrowDown,
                                        tint = MaterialTheme.colors.onBackground,
                                        contentDescription = "Delete tool icon"
                                    )
                                },
                                colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.Transparent),
                                enabled = false,
                                singleLine = true,
                                textStyle = TextStyle(
                                    color = MaterialTheme.colors.onBackground,
                                    fontFamily = poppinsFamily,
                                    fontSize = 14.sp
                                )
                            )
                            DropdownMenu(
                                modifier = Modifier
                                    .background(color = MaterialTheme.colors.background)
                                    .width(
                                        with(LocalDensity.current) {
                                            boxSize.width.toDp()
                                        }
                                    ),
                                expanded = categorySpinnerVis,
                                onDismissRequest = { onCategorySpinnerVisChanged(false) }
                            ) {
                                plantCategories.forEachIndexed { index, plantCategory ->
                                    DropdownMenuItem(
                                        onClick = {
                                            onSelectedCategoryChanged(plantCategory)
                                            onCategorySpinnerVisChanged(false)
                                        }
                                    ) {
                                        Text(
                                            text = plantCategory.category,
                                            color = MaterialTheme.colors.onBackground,
                                            style = MaterialTheme.typography.body1
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                is UIState.Fail -> {
                    LaunchedEffect(Unit) {
                        coroutineScope.launch {
                            plantCategoriesState.message?.let { message ->
                                scaffoldState.snackbarHostState.showSnackbar(message)
                            }
                        }
                    }
                }

                is UIState.Error -> {
                    LaunchedEffect(Unit) {
                        coroutineScope.launch {
                            plantCategoriesState.message?.let { message ->
                                scaffoldState.snackbarHostState.showSnackbar(message)
                            }
                        }
                    }
                }

                else -> {}
            }

            Spacer(modifier = Modifier.height(25.dp))

            // Growth Estimation
            Text(
                text = stringResource(id = R.string.growth_estimation),
                color = MaterialTheme.colors.onBackground,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.body1
            )
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = growthEst,
                onValueChange = onGrowEstChanged,
                colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.Transparent),
                singleLine = true,
                textStyle = TextStyle(
                    color = MaterialTheme.colors.onBackground,
                    fontFamily = poppinsFamily,
                    fontSize = 14.sp
                )
            )
            Spacer(modifier = Modifier.height(25.dp))

            // Watering Frequency
            Text(
                text = stringResource(id = R.string.watering_frequency),
                color = MaterialTheme.colors.onBackground,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.body1
            )
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = wateringFreq,
                onValueChange = onWateringFreqChanged,
                colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.Transparent),
                singleLine = true,
                textStyle = TextStyle(
                    color = MaterialTheme.colors.onBackground,
                    fontFamily = poppinsFamily,
                    fontSize = 14.sp
                )
            )
            Spacer(modifier = Modifier.height(25.dp))

            // Description
            Text(
                text = stringResource(id = R.string.desc),
                color = MaterialTheme.colors.onBackground,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.body1
            )
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = desc,
                onValueChange = onDescChanged,
                colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.Transparent),
                textStyle = TextStyle(
                    color = MaterialTheme.colors.onBackground,
                    fontFamily = poppinsFamily,
                    fontSize = 14.sp
                )
            )
            Spacer(modifier = Modifier.height(25.dp))

            // Tools
            Text(
                text = stringResource(id = R.string.tools),
                color = MaterialTheme.colors.onBackground,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.body1
            )

            tools.forEachIndexed { index, tool ->
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = tool,
                    onValueChange = { onToolsChanged(index, it, ListAction.UPDATE_ITEM) },
                    colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.Transparent),
                    trailingIcon = {
                        if (tools.size > 1) {
                            IconButton(onClick = {
                                onToolsChanged(index, null, ListAction.DELETE_ITEM)
                            }) {
                                Icon(
                                    imageVector = EvaIcons.Outline.Close,
                                    tint = Red,
                                    contentDescription = "Delete tool icon"
                                )
                            }
                        }
                    },
                    textStyle = TextStyle(
                        color = MaterialTheme.colors.onBackground,
                        fontFamily = poppinsFamily,
                        fontSize = 14.sp
                    )
                )
            }

            Spacer(modifier = Modifier.height(15.dp))
            OutlinedButton(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),
                border = BorderStroke(
                    width = 1.dp,
                    color = MaterialTheme.colors.primary
                ),
                onClick = { onToolsChanged(null, "Tool ${tools.size + 1}", ListAction.ADD_ITEM) }
            ) {
                Icon(
                    modifier = Modifier.size(30.dp),
                    imageVector = EvaIcons.Fill.PlusCircle,
                    tint = MaterialTheme.colors.primary,
                    contentDescription = "Add icon"
                )
            }
            Spacer(modifier = Modifier.height(25.dp))

            // Materials
            Text(
                text = stringResource(id = R.string.materials),
                color = MaterialTheme.colors.onBackground,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.body1
            )

            materials.forEachIndexed { index, material ->
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = material,
                    onValueChange = { onMaterialsChanged(index, it, ListAction.UPDATE_ITEM) },
                    colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.Transparent),
                    trailingIcon = {
                        if (materials.size > 1) {
                            IconButton(onClick = {
                                onMaterialsChanged(index, null, ListAction.DELETE_ITEM)
                            }) {
                                Icon(
                                    imageVector = EvaIcons.Outline.Close,
                                    tint = Red,
                                    contentDescription = "Delete tool icon"
                                )
                            }
                        }
                    },
                    textStyle = TextStyle(
                        color = MaterialTheme.colors.onBackground,
                        fontFamily = poppinsFamily,
                        fontSize = 14.sp
                    )
                )
            }

            Spacer(modifier = Modifier.height(15.dp))
            OutlinedButton(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),
                border = BorderStroke(
                    width = 1.dp,
                    color = MaterialTheme.colors.primary
                ),
                onClick = {
                    onMaterialsChanged(
                        null,
                        "Material ${materials.size + 1}",
                        ListAction.ADD_ITEM
                    )
                }
            ) {
                Icon(
                    modifier = Modifier.size(30.dp),
                    imageVector = EvaIcons.Fill.PlusCircle,
                    tint = MaterialTheme.colors.primary,
                    contentDescription = "Add icon"
                )
            }
            Spacer(modifier = Modifier.height(25.dp))

            // Steps
            Text(
                text = stringResource(id = R.string.steps),
                color = MaterialTheme.colors.onBackground,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.body1
            )

            steps.forEachIndexed { index, step ->
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = step,
                    onValueChange = { onStepsChanged(index, it, ListAction.UPDATE_ITEM) },
                    colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.Transparent),
                    trailingIcon = {
                        if (steps.size > 1) {
                            IconButton(onClick = {
                                onStepsChanged(index, null, ListAction.DELETE_ITEM)
                            }) {
                                Icon(
                                    imageVector = EvaIcons.Outline.Close,
                                    tint = Red,
                                    contentDescription = "Delete tool icon"
                                )
                            }
                        }
                    },
                    textStyle = TextStyle(
                        color = MaterialTheme.colors.onBackground,
                        fontFamily = poppinsFamily,
                        fontSize = 14.sp
                    )
                )
            }

            Spacer(modifier = Modifier.height(15.dp))
            OutlinedButton(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),
                border = BorderStroke(
                    width = 1.dp,
                    color = MaterialTheme.colors.primary
                ),
                onClick = { onStepsChanged(null, "Step ${steps.size + 1}", ListAction.ADD_ITEM) }
            ) {
                Icon(
                    modifier = Modifier.size(30.dp),
                    imageVector = EvaIcons.Fill.PlusCircle,
                    tint = MaterialTheme.colors.primary,
                    contentDescription = "Add icon"
                )
            }
            Spacer(modifier = Modifier.height(30.dp))
            Button(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.large,
                onClick = {
                    if (
                        photo != null && plantName.isNotEmpty() && selectedCategory != null &&
                        growthEst.isNotEmpty() && wateringFreq.isNotEmpty() && desc.isNotEmpty() &&
                        tools.isNotEmpty() && materials.isNotEmpty() && steps.isNotEmpty()
                    ) {
                        onEvent(UploadEditPlantEvent.UploadPlant)
                    } else {
                        coroutineScope.launch {
                            scaffoldState.snackbarHostState.showSnackbar(
                                context.getString(R.string.fill_the_form)
                            )
                        }
                    }
                }
            ) {
                Text(
                    modifier = Modifier.padding(5.dp),
                    text = stringResource(id = R.string.upload),
                    color = MaterialTheme.colors.onPrimary,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.subtitle1
                )
            }
        }
    }
}