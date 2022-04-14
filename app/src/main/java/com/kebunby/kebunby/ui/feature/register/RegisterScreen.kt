package com.kebunby.kebunby.ui.feature.register

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
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
import androidx.navigation.NavController
import com.kebunby.kebunby.R
import com.kebunby.kebunby.ui.feature.login.LoginEvent
import com.kebunby.kebunby.ui.feature.login.LoginState
import com.kebunby.kebunby.ui.theme.Grey
import com.kebunby.kebunby.ui.theme.Primary
import com.kebunby.kebunby.ui.theme.poppinsFamily
import compose.icons.EvaIcons
import compose.icons.evaicons.Fill
import compose.icons.evaicons.Outline
import compose.icons.evaicons.fill.Person
import compose.icons.evaicons.outline.*
import kotlinx.coroutines.launch

@Composable
fun RegisterScreen(navController: NavController) {
    val context = LocalContext.current
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    Scaffold(scaffoldState = scaffoldState) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Column(modifier = Modifier.background(color = Primary)) {
                Text(
                    modifier = Modifier
                        .padding(top = 30.dp)
                        .align(CenterHorizontally),
                    text = stringResource(id = R.string.register),
                    color =  Color.White,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.h1
                )
                Spacer(modifier = Modifier.height(30.dp))
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    shape = RoundedCornerShape(topStart = 40.dp),
                    color = Color.White
                ) {
                    Column(
                        modifier = Modifier
                            .padding(top = 30.dp, bottom = 20.dp)
                            .padding(horizontal = 20.dp)
                    ) {
                        Text(
                            text = stringResource(id = R.string.username),
                            color = Color.Black,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.body1
                        )
                        Spacer(modifier = Modifier.height(5.dp))
                        OutlinedTextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = "",
                            onValueChange = {},
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
                            color = Color.Black,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.body1
                        )
                        Spacer(modifier = Modifier.height(5.dp))
                        OutlinedTextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = "",
                            onValueChange = {},
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
                            )
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        Text(
                            text = stringResource(id = R.string.name),
                            color = Color.Black,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.body1
                        )
                        Spacer(modifier = Modifier.height(5.dp))
                        OutlinedTextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = "",
                            onValueChange = {},
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
                            color = Color.Black,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.body1
                        )
                        Spacer(modifier = Modifier.height(5.dp))
                        OutlinedTextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = "",
                            onValueChange = {},
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
                                IconButton(onClick = {}) {
                                    Icon(
                                        imageVector = if (true) {
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
                            visualTransformation = if (true) VisualTransformation.None else PasswordVisualTransformation()
                        )
                        Spacer(modifier = Modifier.height(30.dp))
                        Button(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(15.dp),
                            enabled = true,
                            onClick = {
                                /*if (username.isNotEmpty() && password.isNotEmpty()) {
                                    onEvent(LoginEvent.Submit)
                                } else {
                                    coroutineScope.launch {
                                        scaffoldState.snackbarHostState.showSnackbar(
                                            context.resources.getString(R.string.fill_the_form)
                                        )
                                    }
                                }*/
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
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            text = buildAnnotatedString {
                                append(stringResource(id = R.string.have_account))
                                append(" ")

                                withStyle(
                                    style = SpanStyle(
                                        color = Primary,
                                        fontFamily = poppinsFamily,
                                        fontSize = 15.sp
                                    )
                                ) {
                                    append(stringResource(id = R.string.login_here))
                                }
                            },
                            style = MaterialTheme.typography.subtitle1,
                            onClick = {}
                        )
                    }
                }
            }
        }
    }
}