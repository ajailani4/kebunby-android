package com.kebunby.kebunby.ui.feature.onboarding

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.kebunby.kebunby.R

sealed class OnboardingItem(
    @StringRes val title: Int?,
    @StringRes val description: Int?,
    @DrawableRes val image: Int
) {
    object OnboardingScreen1 : OnboardingItem(
        title = R.string.title_onboarding1,
        description = R.string.desc_onboarding1,
        image = R.drawable.img_onboarding_1
    )

    object OnboardingScreen2 : OnboardingItem(
        title = R.string.title_onboarding2,
        description = R.string.desc_onboarding2,
        image = R.drawable.img_onboarding_2
    )

    object OnboardingScreen3 : OnboardingItem(
        title = R.string.title_onboarding3,
        description = R.string.desc_onboarding3,
        image = R.drawable.img_onboarding_3
    )

    object OnboardingScreen4 : OnboardingItem(
        title = null,
        description = null,
        image = R.drawable.img_onboarding_4
    )
}