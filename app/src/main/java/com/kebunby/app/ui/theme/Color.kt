package com.kebunby.app.ui.theme

import androidx.compose.material.Colors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Light theme
val PrimaryLight = Color(0xFF91C637)
val PrimaryVariantLight = Color(0xFF89BA35)
val SecondaryLight = Color(0xFF3DB35F)
val SecondaryVariantLight = Color(0xFF36A155)
val BackgroundLight = Color.White
val SurfaceLight = Color.White
val ErrorLight = Color(0xFFB00020)
val OnPrimaryLight = Color.White
val OnSecondaryLight = Color.White
val OnBackgroundLight = Color.Black
val OnSurfaceLight = Color.Black
val OnErrorLight = Color.White

// Dark theme
val PrimaryDark = Color(0xFF91C637)
val PrimaryVariantDark = Color(0xFF89BA35)
val SecondaryDark = Color(0xFF3DB35F)
val SecondaryVariantDark = Color(0xFF36A155)
val BackgroundDark = Color(0xFF242222)
val SurfaceDark = Color(0xFF2F2F2F)
val ErrorDark = Color(0xFFB00020)
val OnPrimaryDark = Color.White
val OnSecondaryDark = Color.White
val OnBackgroundDark = Color.White
val OnSurfaceDark = Color.White
val OnErrorDark = Color.White

// Additional
val Colors.SearchTextFieldGrey: Color
    @Composable
    get() = if (isLight) Color(0xFFECECEC) else Color(0xFF2F2F2F)

// Common
val Grey = Color(0xFFBDBDBD)
val Red = Color(0xFFEB5757)
val BackgroundShimmer = Color(0xFFA3A3A3)