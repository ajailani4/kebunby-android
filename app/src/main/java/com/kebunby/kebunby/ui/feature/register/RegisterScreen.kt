package com.kebunby.kebunby.ui.feature.register

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.kebunby.kebunby.R
import com.kebunby.kebunby.ui.Screen
import com.kebunby.kebunby.ui.common.UIState
import com.kebunby.kebunby.ui.common.component.FullSizeProgressBar
import com.kebunby.kebunby.ui.theme.Grey
import com.kebunby.kebunby.ui.theme.poppinsFamily
import compose.icons.EvaIcons
import compose.icons.evaicons.Fill
import compose.icons.evaicons.Outline
import compose.icons.evaicons.fill.Person
import compose.icons.evaicons.outline.*
import kotlinx.coroutines.launch

@Composable
fun RegisterScreen(
    navController: NavController,
    registerViewModel: RegisterViewModel = hiltViewModel()
) {
    val onEvent = registerViewModel::onEvent
    val registerState = registerViewModel.registerState
    val username = registerViewModel.username
    val onUsernameChanged = registerViewModel::onUsernameChanged
    val email = registerViewModel.email
    val onEmailChanged = registerViewModel::onEmailChanged
    val name = registerViewModel.name
    val onNameChanged = registerViewModel::onNameChanged
    val password = registerViewModel.password
    val onPasswordChanged = registerViewModel::onPasswordChanged
    val passwordVisibility = registerViewModel.passwordVisibility
    val onPasswordVisibilityChanged = registerViewModel::onPasswordVisibilityChanged

    val context = LocalContext.current
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    Scaffold(scaffoldState = scaffoldState) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Column(modifier = Modifier.background(color = MaterialTheme.colors.primary)) {
                Text(
                    modifier = Modifier
                        .padding(top = 30.dp)
                        .align(CenterHorizontally),
                    text = stringResource(id = R.string.register),
                    color = MaterialTheme.colors.onPrimary,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.h1
                )
                Spacer(modifier = Modifier.height(30.dp))
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    shape = RoundedCornerShape(topStart = 40.dp),
                    color = MaterialTheme.colors.background
                ) {
                    Column(
                        modifier = Modifier
                            .padding(top = 30.dp, bottom = 20.dp)
                            .padding(horizontal = 20.dp)
                    ) {
                        Text(
                            text = stringResource(id = R.string.username),
                            color = MaterialTheme.colors.onBackground,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.body1
                        )
                        Spacer(modifier = Modifier.height(5.dp))
                        OutlinedTextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = username,
                            onValueChange = onUsernameChanged,
                            leadingIcon = {
                                Icon(
                                    imageVector = EvaIcons.Outline.Person,
                                    tint = Grey,
                                    contentDescription = "Username icon"
                                )
                            },
                            placeholder = {
                                Text(
                                    text = stringResource(id = R.string.enter_username),
                                    color = Grey,
                                    style = MaterialTheme.typography.subtitle1
                                )
                            },
                            singleLine = true,
                            textStyle = TextStyle(
                                color = Color.Black,
                                fontFamily = poppinsFamily,
                                fontSize = 15.sp
                            )
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        Text(
                            text = stringResource(id = R.string.email),
                            color = MaterialTheme.colors.onBackground,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.body1
                        )
                        Spacer(modifier = Modifier.height(5.dp))
                        OutlinedTextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = email,
                            onValueChange = onEmailChanged,
                            leadingIcon = {
                                Icon(
                                    imageVector = EvaIcons.Outline.Email,
                                    tint = Grey,
                                    contentDescription = "Email icon"
                                )
                            },
                            placeholder = {
                                Text(
                                    text = stringResource(id = R.string.enter_email),
                                    color = Grey,
                                    style = MaterialTheme.typography.subtitle1
                                )
                            },
                            singleLine = true,
                            textStyle = TextStyle(
                                color = Color.Black,
                                fontFamily = poppinsFamily,
                                fontSize = 15.sp
                            ),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Email
                            )
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        Text(
                            text = stringResource(id = R.string.name),
                            color = MaterialTheme.colors.onBackground,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.body1
                        )
                        Spacer(modifier = Modifier.height(5.dp))
                        OutlinedTextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = name,
                            onValueChange = onNameChanged,
                            leadingIcon = {
                                Icon(
                                    imageVector = EvaIcons.Fill.Person,
                                    tint = Grey,
                                    contentDescription = "Name icon"
                                )
                            },
                            placeholder = {
                                Text(
                                    text = stringResource(id = R.string.enter_name),
                                    color = Grey,
                                    style = MaterialTheme.typography.subtitle1
                                )
                            },
                            singleLine = true,
                            textStyle = TextStyle(
                                color = Color.Black,
                                fontFamily = poppinsFamily,
                                fontSize = 15.sp
                            )
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        Text(
                            text = stringResource(id = R.string.password),
                            color = MaterialTheme.colors.onBackground,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.body1
                        )
                        Spacer(modifier = Modifier.height(5.dp))
                        OutlinedTextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = password,
                            onValueChange = onPasswordChanged,
                            leadingIcon = {
                                Icon(
                                    imageVector = EvaIcons.Outline.Lock,
                                    tint = Grey,
                                    contentDescription = "Password icon"
                                )
                            },
                            placeholder = {
                                Text(
                                    text = stringResource(id = R.string.enter_password),
                                    color = Grey,
                                    style = MaterialTheme.typography.subtitle1
                                )
                            },
                            trailingIcon = {
                                IconButton(onClick = onPasswordVisibilityChanged) {
                                    Icon(
                                        imageVector = if (passwordVisibility) {
                                            EvaIcons.Outline.Eye
                                        } else {
                                            EvaIcons.Outline.EyeOff
                                        },
                                        contentDescription = "Password visibility icon"
                                    )
                                }
                            },
                            singleLine = true,
                            textStyle = TextStyle(
                                color = Color.Black,
                                fontFamily = poppinsFamily,
                                fontSize = 15.sp
                            ),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Password
                            ),
                            visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation()
                        )
                        Spacer(modifier = Modifier.height(30.dp))
                        Button(
                            modifier = Modifier.fillMaxWidth(),
                            shape = MaterialTheme.shapes.medium,
                            enabled = registerState != UIState.Loading,
                            onClick = {
                                if (
                                    username.isNotEmpty() && email.isNotEmpty() &&
                                    name.isNotEmpty() && password.isNotEmpty()
                                ) {
                                    onEvent(RegisterEvent.Submit)
                                } else {
                                    coroutineScope.launch {
                                        scaffoldState.snackbarHostState.showSnackbar(
                                            context.resources.getString(R.string.fill_the_form)
                                        )
                                    }
                                }
                            }
                        ) {
                            Text(
                                modifier = Modifier.padding(5.dp),
                                text = stringResource(id = R.string.register),
                                color = MaterialTheme.colors.onPrimary,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.subtitle1
                            )
                        }
                        Spacer(modifier = Modifier.height(15.dp))
                        ClickableText(
                            modifier = Modifier.align(CenterHorizontally),
                            text = buildAnnotatedString {
                                append(stringResource(id = R.string.have_account))
                                append(" ")

                                withStyle(
                                    style = SpanStyle(
                                        color = MaterialTheme.colors.primary,
                                        fontFamily = poppinsFamily,
                                        fontSize = 15.sp
                                    )
                                ) {
                                    append(stringResource(id = R.string.login_here))
                                }
                            },
                            style = MaterialTheme.typography.subtitle1,
                            onClick = { navController.navigate(Screen.LoginScreen.route) }
                        )
                    }
                }
            }
        }

        // Observe register state
        when (registerState) {
            is UIState.Idle -> {}

            is UIState.Loading -> {
                FullSizeProgressBar()
            }

            is UIState.Success -> {
                navController.navigate(Screen.HomeScreen.route) {
                    launchSingleTop = true

                    popUpTo(Screen.OnboardingScreen.route) {
                        inclusive = true
                    }
                }
            }

            is UIState.Fail -> {
                LaunchedEffect(Unit) {
                    coroutineScope.launch {
                        registerState.message?.let { message ->
                            scaffoldState.snackbarHostState.showSnackbar(message)
                        }
                    }
                }

                onEvent(RegisterEvent.Idle)
            }

            is UIState.Error -> {
                LaunchedEffect(Unit) {
                    coroutineScope.launch {
                        registerState.message?.let { message ->
                            scaffoldState.snackbarHostState.showSnackbar(message)
                        }
                    }
                }

                onEvent(RegisterEvent.Idle)
            }
        }
    }
}