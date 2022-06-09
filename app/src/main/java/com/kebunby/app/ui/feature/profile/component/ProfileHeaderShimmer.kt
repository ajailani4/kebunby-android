package com.kebunby.app.ui.feature.profile.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.kebunby.app.ui.theme.BackgroundShimmer
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import com.valentinilk.shimmer.shimmer

@Composable
fun ProfileHeaderShimmer() {
    val shimmerInstance = rememberShimmer(shimmerBounds = ShimmerBounds.View)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .shimmer(shimmerInstance)
    ) {
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .background(color = BackgroundShimmer)
                .size(90.dp)
                .shimmer(shimmerInstance)
        )
        Spacer(modifier = Modifier.width(25.dp))
        Column(verticalArrangement = Arrangement.SpaceBetween) {
            Column {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(5.dp))
                        .background(color = BackgroundShimmer)
                        .size(120.dp, 20.dp)
                        .shimmer(shimmerInstance)
                )
                Spacer(modifier = Modifier.height(5.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(5.dp))
                        .background(color = BackgroundShimmer)
                        .size(100.dp, 20.dp)
                        .shimmer(shimmerInstance)
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            Row {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(5.dp))
                        .background(color = BackgroundShimmer)
                        .size(50.dp, 20.dp)
                        .shimmer(shimmerInstance)
                )
                Spacer(modifier = Modifier.width(20.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(5.dp))
                        .background(color = BackgroundShimmer)
                        .size(50.dp, 20.dp)
                        .shimmer(shimmerInstance)
                )
                Spacer(modifier = Modifier.width(20.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(5.dp))
                        .background(color = BackgroundShimmer)
                        .size(50.dp, 20.dp)
                        .shimmer(shimmerInstance)
                )
            }
        }
    }
}