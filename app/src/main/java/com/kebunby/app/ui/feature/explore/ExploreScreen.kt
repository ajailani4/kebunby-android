package com.kebunby.app.ui.feature.explore

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import coil.annotation.ExperimentalCoilApi
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.kebunby.app.R
import com.kebunby.app.ui.Screen
import com.kebunby.app.ui.common.component.PlantCard
import com.kebunby.app.ui.theme.Grey
import com.kebunby.app.ui.theme.SearchTextFieldGrey
import com.kebunby.app.ui.theme.poppinsFamily
import compose.icons.EvaIcons
import compose.icons.evaicons.Fill
import compose.icons.evaicons.fill.ArrowBack
import compose.icons.evaicons.fill.Search
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoilApi::class, ExperimentalComposeUiApi::class)
@Composable
fun ExploreScreen(
    navController: NavController,
    exploreViewModel: ExploreViewModel = hiltViewModel()
) {
    val onEvent = exploreViewModel::onEvent
    val pagingPlants = exploreViewModel.pagingPlants.collectAsLazyPagingItems()
    val swipeRefreshing = exploreViewModel.swipeRefreshing.value
    val onSwipeRefreshingChanged = exploreViewModel::onSwipeRefreshingChanged
    val searchQuery = exploreViewModel.searchQuery.value
    val onSearchQueryChanged = exploreViewModel::onSearchQueryChanged
    val isSearched = exploreViewModel.isSearched.value
    val onSearchPlant = exploreViewModel::onSearchPlant

    val coroutineScope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()

    val localFocusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            SearchTextField(
                onEvent = onEvent,
                searchQuery = searchQuery,
                onSearchQueryChanged = onSearchQueryChanged,
                isSearched = isSearched,
                onSearchPlant = onSearchPlant,
                localFocusManager = localFocusManager,
                keyboardController = keyboardController
            )
        }
    ) {
        SwipeRefresh(
            state = rememberSwipeRefreshState(isRefreshing = swipeRefreshing),
            onRefresh = {
                onSwipeRefreshingChanged(true)
                onEvent(ExploreEvent.LoadPlants)
            },
            indicator = { state, trigger ->
                SwipeRefreshIndicator(
                    state = state,
                    refreshTriggerDistance = trigger,
                    contentColor = MaterialTheme.colors.primary
                )
            }
        ) {
            LazyColumn(
                modifier = Modifier
                    .background(color = MaterialTheme.colors.background)
                    .fillMaxSize(),
                contentPadding = PaddingValues(20.dp)
            ) {
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
                pagingPlants.apply {
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

                        loadState.source.refresh is LoadState.NotLoading &&
                            loadState.append.endOfPaginationReached -> {
                            onSwipeRefreshingChanged(false)

                            if (itemCount < 1) {
                                item {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(top = 140.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Image(
                                            modifier = Modifier.size(200.dp),
                                            painter = painterResource(id = R.drawable.img_empty_plant),
                                            contentDescription = "Empty plant illustration"
                                        )
                                        Text(
                                            text = stringResource(id = R.string.searched_plant_is_empty),
                                            color = Grey,
                                            fontWeight = FontWeight.Bold,
                                            textAlign = TextAlign.Center,
                                            style = MaterialTheme.typography.h4
                                        )
                                    }
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
                            onSwipeRefreshingChanged(false)

                            item {
                                coroutineScope.launch {
                                    (loadState.append as LoadState.Error).error.localizedMessage?.let { message ->
                                        scaffoldState.snackbarHostState.showSnackbar(message)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (isSearched) {
        BackHandler {
            onSearchPlant(false)
            onSearchQueryChanged("")
            onEvent(ExploreEvent.LoadPlants)
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchTextField(
    onEvent: (ExploreEvent) -> Unit,
    searchQuery: String,
    onSearchQueryChanged: (String) -> Unit,
    isSearched: Boolean,
    onSearchPlant: (Boolean) -> Unit,
    localFocusManager: FocusManager,
    keyboardController: SoftwareKeyboardController?
) {
    Row(
        modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AnimatedVisibility(visible = isSearched) {
            IconButton(
                modifier = Modifier.size(24.dp),
                onClick = {
                    onSearchPlant(false)
                    onSearchQueryChanged("")
                    onEvent(ExploreEvent.LoadPlants)
                }
            ) {
                Icon(
                    imageVector = EvaIcons.Fill.ArrowBack,
                    tint = Grey,
                    contentDescription = "Back button"
                )
            }
        }

        if (isSearched) Spacer(modifier = Modifier.width(10.dp))

        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = searchQuery,
            onValueChange = onSearchQueryChanged,
            shape = RoundedCornerShape(8.dp),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = MaterialTheme.colors.SearchTextFieldGrey,
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
                    text = stringResource(id = R.string.search_plant),
                    color = Grey,
                    style = MaterialTheme.typography.body1
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
                onSearchPlant(true)
                onEvent(ExploreEvent.LoadPlants)
            })
        )
    }
}