package tachiyomi.presentation.core.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview

/**
 * A combined [CircularProgressIndicator] that always rotates.
 *
 * By always rotating we give the feedback to the user that the application isn't 'stuck'.
 */
@Composable
fun CombinedCircularProgressIndicator(
    //传入一个很返回Float作为状态
    progress: () -> Float,
    modifier: Modifier = Modifier,
) {
    val density = LocalDensity.current
    AnimatedContent(
        //AnimatedContent会根据 targetState的变化来进行动画
        //AnimatedVisibility 知识单纯的做动画，没有targetState
        //如果一直变化的话要用 contentKey
        targetState = progress() == 0f,
        // 一个返回 ContentTransform的lambda函数 this可以拿取到targetState的内容
        // https://developer.android.com/reference/kotlin/androidx/compose/animation/ContentTransform
        //https://developer.android.com/jetpack/compose/animation/composables-modifiers
        transitionSpec = {
            fadeIn() togetherWith fadeOut()
        },
        label = "progressState",
        modifier = modifier,
    ) { indeterminate ->
        if (indeterminate) {
            // Indeterminate
            // 为0 就一直正常转圈
            CircularProgressIndicator()
        } else {
            // Determinate
            // 添加一个无限的动画
            val infiniteTransition = rememberInfiniteTransition(label = "infiniteRotation")

            //后面 animate float用到这个无限float
            //  animateFloat必须在一个无限动画的作用域上面
            val rotation by infiniteTransition.animateFloat(
                initialValue = 0f,
                targetValue = 360f,
                //animationSpec： InfiniteRepeatableSpec<T> 在这里面是float
                animationSpec = infiniteRepeatable(
                    //使用线性的曲线，持续2000，默认0延迟
                    //表示initialValue和targetValue之间的过去
                    animation = tween(2000, easing = LinearEasing),
                    repeatMode = RepeatMode.Restart,
                ),
                label = "rotation",
            )
            //可以监听progress()的变化， 放到CircularProgressIndicator里面库监听到
            val animatedProgress by animateFloatAsState(
                targetValue = progress(),
                animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec,
                label = "progress",
            )
            CircularProgressIndicator(
                progress = { animatedProgress },
                //旋转的视图， clockwise rotation is positive
                modifier = Modifier.rotate(rotation),
            )
        }
    }
}

@Preview
@Composable
private fun CombinedCircularProgressIndicatorPreview() {
    var progress by remember {
        // FloatStateOf的状态
        mutableFloatStateOf(0f)
    }
    MaterialTheme {
        Scaffold(
            bottomBar = {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        // 使用when来切换progress的状态
                        progress = when (progress) {
                            0f -> 0.15f
                            0.15f -> 0.25f
                            0.25f -> 0.5f
                            0.5f -> 0.75f
                            0.75f -> 0.95f
                            else -> 0f
                        }
                    },
                ) {
                    Text("change")
                }
            },
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it),
            ) {
                CombinedCircularProgressIndicator(progress = { progress })
            }
        }
    }
}
