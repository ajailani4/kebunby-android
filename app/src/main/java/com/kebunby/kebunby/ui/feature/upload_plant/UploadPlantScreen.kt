package com.kebunby.kebunby.ui.feature.upload_plant

import android.app.Activity
import android.content.Context
import android.view.WindowManager
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.kebunby.kebunby.R
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
import compose.icons.evaicons.fill.PlusCircle
import compose.icons.evaicons.outline.Close
import id.zelory.compressor.Compressor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.io.File

@Composable
fun UploadPlantScreen(
    navController: NavController,
    uploadPlantViewModel: UploadPlantViewModel = hiltViewModel()
) {
    val onEvent = uploadPlantViewModel::onEvent
    val uploadPlantState = uploadPlantViewModel.uploadPlantState.value
    val cameraScreenVis = uploadPlantViewModel.cameraScreenVis.value
    val onCameraScreenVisChanged = uploadPlantViewModel::onCameraScreenVisChanged
    val photo = uploadPlantViewModel.photo.value
    val onPhotoChanged = uploadPlantViewModel::onPhotoChanged
    val plantName = uploadPlantViewModel.plantName.value
    val onPlantNameChanged = uploadPlantViewModel::onPlantNameChanged
    val growthEst = uploadPlantViewModel.growthEst.value
    val onGrowEstChanged = uploadPlantViewModel::onGrowthEstChanged
    val wateringFreq = uploadPlantViewModel.wateringFreq.value
    val onWateringFreqChanged = uploadPlantViewModel::onWateringFreqChanged
    val desc = uploadPlantViewModel.desc.value
    val onDescChanged = uploadPlantViewModel::onDescChanged
    val tools = uploadPlantViewModel.tools
    val onToolsChanged = uploadPlantViewModel::onToolsChanged
    val materials = uploadPlantViewModel.materials
    val onMaterialsChanged = uploadPlantViewModel::onMaterialsChanged
    val steps = uploadPlantViewModel.steps
    val onStepsChanged = uploadPlantViewModel::onStepsChanged

    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    val context = LocalContext.current
    (context as Activity).window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            if (!cameraScreenVis) {
                CustomToolbar(
                    title = stringResource(id = R.string.upload_plant),
                    hasBackButton = true,
                    onBackButtonClicked = { navController.navigateUp() }
                )
            }
        }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            UploadPlantForm(
                onEvent = onEvent,
                photo = photo,
                plantName = plantName,
                onPlantNameChanged = onPlantNameChanged,
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
fun UploadPlantForm(
    onEvent: (UploadPlantEvent) -> Unit,
    photo: File?,
    plantName: String,
    onPlantNameChanged: (String) -> Unit,
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
                    contentDescription = "Plant photo"
                )
            }

            Spacer(modifier = Modifier.height(25.dp))
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
                        photo != null && plantName.isNotEmpty() && growthEst.isNotEmpty() &&
                        wateringFreq.isNotEmpty() && desc.isNotEmpty() && tools.isNotEmpty() &&
                        materials.isNotEmpty() && steps.isNotEmpty()
                    ) {
                        onEvent(UploadPlantEvent.UploadPlant)
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