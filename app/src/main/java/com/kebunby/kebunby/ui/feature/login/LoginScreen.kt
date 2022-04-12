package com.kebunby.kebunby.ui.feature.login

import androidx.compose.foundation.Image
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
import androidx.compose.ui.res.painterResource
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
import com.kebunby.kebunby.ui.common.component.FullSizeProgressBar
import com.kebunby.kebunby.ui.theme.Grey
import com.kebunby.kebunby.ui.theme.Primary
import com.kebunby.kebunby.ui.theme.poppinsFamily
import compose.icons.EvaIcons
import compose.icons.evaicons.Outline
import compose.icons.evaicons.outline.Eye
import compose.icons.evaicons.outline.EyeOff
import compose.icons.evaicons.outline.Lock
import compose.icons.evaicons.outline.Person
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    navController: NavController,
    loginViewModel: LoginViewModel = hiltViewModel()
) {
    val onEvent = loginViewModel::onEvent
    val loginState = loginViewModel.loginState
    val username = loginViewModel.username
    val onUsernameChanged = loginViewModel::onUsernameChanged
    val password = loginViewModel.password
    val onPasswordChanged = loginViewModel::onPasswordChanged
    val passwordVisibility = loginViewModel.passwordVisibility
    val onPasswordVisibilityChanged = loginViewModel::onPasswordVisibilityChanged

    val context = LocalContext.current
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    Scaffold(scaffoldState = scaffoldState) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
            ) {
                Text(
                    modifier = Modifier
                        .padding(top = 50.dp)
                        .align(CenterHorizontally),
                    text = stringResource(id = R.string.login),
                    color = Primary,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.h1
                )
                Spacer(modifier = Modifier.height(30.dp))
                Text(
                    text = stringResource(id = R.string.username),
                    color = Color.Black,
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
                    text = stringResource(id = R.string.enter_password),
                    color = Color.Black,
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
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password
                    ),
                    visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation()
                )
                Spacer(modifier = Modifier.height(30.dp))
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(15.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Primary),
                    enabled = loginState != LoginState.LoggingIn,
                    onClick = {
                        if (username.isNotEmpty() && password.isNotEmpty()) {
                            onEvent(LoginEvent.Submit)
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
                        text = stringResource(id = R.string.login),
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.subtitle1
                    )
                }
                Spacer(modifier = Modifier.height(15.dp))
                ClickableText(
                    modifier = Modifier.align(CenterHorizontally),
                    text = buildAnnotatedString {
                        append(stringResource(id = R.string.have_no_account))
                        append(" ")

                        withStyle(
                            style = SpanStyle(
                                color = Primary,
                                fontFamily = poppinsFamily,
                                fontSize = 15.sp
                            )
                        ) {
                            append(stringResource(id = R.string.register_here))
                        }
                    },
                    style = MaterialTheme.typography.subtitle1,
                    onClick = {}
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            Image(
                modifier = Modifier.fillMaxWidth(),
                painter = painterResource(id = R.drawable.img_wave),
                contentDescription = "Wave illustration"
            )
        }

        // Observe login state
        when (loginState) {
            is LoginState.Idle -> {}

            is LoginState.LoggingIn -> {
                FullSizeProgressBar()
            }

            is LoginState.Success -> {
                navController.navigate(Screen.HomeScreen.route) {
                    launchSingleTop = true

                    popUpTo(Screen.LoginScreen.route) {
                        inclusive = true
                    }
                }
            }

            is LoginState.Fail -> {
                LaunchedEffect(Unit) {
                    coroutineScope.launch {
                        loginState.message?.let { message ->
                            scaffoldState.snackbarHostState.showSnackbar(message)
                        }
                    }
                }

                onEvent(LoginEvent.Idle)
            }

            is LoginState.Error -> {
                LaunchedEffect(Unit) {
                    coroutineScope.launch {
                        loginState.message?.let { message ->
                            scaffoldState.snackbarHostState.showSnackbar(message)
                        }
                    }
                }

                onEvent(LoginEvent.Idle)
            }
        }
    }
}