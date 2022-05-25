package com.kebunby.kebunby.ui.feature.explore

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.items
import coil.annotation.ExperimentalCoilApi
import com.kebunby.kebunby.R
import com.kebunby.kebunby.data.model.PlantItem
import com.kebunby.kebunby.ui.Screen
import com.kebunby.kebunby.ui.common.component.CustomToolbar
import com.kebunby.kebunby.ui.common.component.PlantCard
import com.kebunby.kebunby.ui.theme.Grey
import com.kebunby.kebunby.ui.theme.SearchTextFieldGrey
import com.kebunby.kebunby.ui.theme.poppinsFamily
import compose.icons.EvaIcons
import compose.icons.evaicons.Fill
import compose.icons.evaicons.fill.Search
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoilApi::class, ExperimentalComposeUiApi::class)
@Composable
fun ExploreScreen(navController: NavController) {
    val pagingPlants = listOf(
        PlantItem(
            id = 1,
            name = "Tanaman",
            image = "test",
            category = "Tanaman Hias",
            growthEst = "2-3 Tahun",
            wateringFreq = "3x Sehari",
            popularity = 10,
            isFavorited = true
        ),
        PlantItem(
            id = 1,
            name = "Tanaman",
            image = "test",
            category = "Tanaman Hias",
            growthEst = "2-3 Tahun",
            wateringFreq = "3x Sehari",
            popularity = 10,
            isFavorited = true
        ),
        PlantItem(
            id = 1,
            name = "Tanaman",
            image = "test",
            category = "Tanaman Hias",
            growthEst = "2-3 Tahun",
            wateringFreq = "3x Sehari",
            popularity = 10,
            isFavorited = true
        )
    )

    val coroutineScope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()

    val localFocusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            CustomToolbar(
                navController = navController,
                title = stringResource(id = R.string.explore),
                hasBackButton = true
            )
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .background(color = MaterialTheme.colors.background)
                .fillMaxSize(),
            contentPadding = PaddingValues(20.dp)
        ) {
            item {
                SearchTextField(
                    localFocusManager = localFocusManager,
                    keyboardController = keyboardController
                )
                Spacer(modifier = Modifier.height(30.dp))
            }

            items(pagingPlants) { plant ->
                PlantCard(
                    plantItem = plant,
                    onClick = {
                        navController.navigate(
                            Screen.PlantDetailScreen.route + "?plantId=${plant?.id}"
                        )
                    }
                )
                Spacer(modifier = Modifier.height(20.dp))
            }

            // Handle paging plants state
            /*pagingPlants.apply {
                when {
                    loadState.refresh is LoadState.Loading -> {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(top = 170.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                    }

                    loadState.append is LoadState.Loading -> {
                        item {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentWidth(Alignment.CenterHorizontally)
                            )
                        }
                    }

                    loadState.append is LoadState.Error -> {
                        item {
                            coroutineScope.launch {
                                (loadState.append as LoadState.Error).error.localizedMessage?.let { message ->
                                    scaffoldState.snackbarHostState.showSnackbar(message)
                                }
                            }
                        }
                    }
                }
            }*/
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchTextField(
    localFocusManager: FocusManager,
    keyboardController: SoftwareKeyboardController?
) {
    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = "",
        onValueChange = {},
        shape = RoundedCornerShape(8.dp),
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = SearchTextFieldGrey,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        trailingIcon = {
            Icon(
                imageVector = EvaIcons.Fill.Search,
                tint = Grey,
                contentDescription = "Search icon"
            )
        },
        placeholder = {
            Text(
                text = stringResource(id = R.string.search_plant)
            )
        },
        singleLine = true,
        textStyle = TextStyle(
            color = MaterialTheme.colors.onBackground,
            fontFamily = poppinsFamily,
            fontSize = 14.sp
        ),
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = {
            localFocusManager.clearFocus()
            keyboardController?.hide()
        })
    )
}