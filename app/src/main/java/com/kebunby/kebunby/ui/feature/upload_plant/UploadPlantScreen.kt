package com.kebunby.kebunby.ui.feature.upload_plant

import android.app.Activity
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
import com.kebunby.kebunby.ui.common.component.CustomToolbar
import com.kebunby.kebunby.ui.feature.camera.CameraScreen
import com.kebunby.kebunby.ui.theme.poppinsFamily
import compose.icons.EvaIcons
import compose.icons.evaicons.Fill
import compose.icons.evaicons.fill.PlusCircle
import id.zelory.compressor.Compressor
import kotlinx.coroutines.launch
import java.io.File

@Composable
fun UploadPlantScreen(
    navController: NavController,
    uploadPlantViewModel: UploadPlantViewModel = hiltViewModel()
) {
    val photo = uploadPlantViewModel.photo.value
    val onPhotoChanged = uploadPlantViewModel::onPhotoChanged
    val cameraScreenVis = uploadPlantViewModel.cameraScreenVis.value
    val onCameraScreenVisChanged = uploadPlantViewModel::onCameraScreenVisChanged

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
            UploadPlantForm(photo = photo)
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
    }
}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun UploadPlantForm(
    photo: File?
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
                value = "",
                onValueChange = {},
                colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.Transparent),
                textStyle = TextStyle(
                    color = MaterialTheme.colors.onBackground,
                    fontFamily = poppinsFamily,
                    fontSize = 14.sp
                )
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = stringResource(id = R.string.growth_estimation),
                color = MaterialTheme.colors.onBackground,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.body1
            )
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = "",
                onValueChange = {},
                colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.Transparent),
                textStyle = TextStyle(
                    color = MaterialTheme.colors.onBackground,
                    fontFamily = poppinsFamily,
                    fontSize = 14.sp
                )
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = stringResource(id = R.string.watering_frequency),
                color = MaterialTheme.colors.onBackground,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.body1
            )
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = "",
                onValueChange = {},
                colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.Transparent),
                textStyle = TextStyle(
                    color = MaterialTheme.colors.onBackground,
                    fontFamily = poppinsFamily,
                    fontSize = 14.sp
                )
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = stringResource(id = R.string.tools_and_materials),
                color = MaterialTheme.colors.onBackground,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.body1
            )
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = "",
                onValueChange = {},
                colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.Transparent),
                textStyle = TextStyle(
                    color = MaterialTheme.colors.onBackground,
                    fontFamily = poppinsFamily,
                    fontSize = 14.sp
                )
            )
            Spacer(modifier = Modifier.height(15.dp))
            OutlinedButton(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),
                border = BorderStroke(
                    width = 1.dp,
                    color = MaterialTheme.colors.primary
                ),
                onClick = {}
            ) {
                Icon(
                    modifier = Modifier.size(30.dp),
                    imageVector = EvaIcons.Fill.PlusCircle,
                    tint = MaterialTheme.colors.primary,
                    contentDescription = "Add icon"
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = stringResource(id = R.string.steps),
                color = MaterialTheme.colors.onBackground,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.body1
            )
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = "",
                onValueChange = {},
                colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.Transparent),
                textStyle = TextStyle(
                    color = MaterialTheme.colors.onBackground,
                    fontFamily = poppinsFamily,
                    fontSize = 14.sp
                )
            )
            Spacer(modifier = Modifier.height(15.dp))
            OutlinedButton(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),
                border = BorderStroke(
                    width = 1.dp,
                    color = MaterialTheme.colors.primary
                ),
                onClick = {}
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
                onClick = {}
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