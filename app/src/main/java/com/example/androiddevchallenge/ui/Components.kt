/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.androiddevchallenge.ui

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.androiddevchallenge.R
import com.example.androiddevchallenge.ui.theme.primary500

@Composable
fun TimerTopAppBar(titleId: Int, iconId: Int, iconContentDescriptionId: Int) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(id = titleId),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center)
            )
        },
        navigationIcon =
        {
            Image(
                painter = painterResource(id = iconId),
                contentDescription = stringResource(id = iconContentDescriptionId),
                modifier = Modifier
                    .padding(horizontal = 16.dp)
            )
        },
        actions = {
            Spacer(modifier = Modifier.width(68.dp))
        },
        backgroundColor = MaterialTheme.colors.surface,
        elevation = 0.dp
    )
}

@Composable
fun TimerEdit(time: String, onNumDelete: () -> Unit) {
    ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
        val (text, button) = createRefs()
        TimerText(
            time,
            Modifier.constrainAs(text) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                top.linkTo(parent.top)
            }
        )
        IconButton(
            onClick = onNumDelete,
            Modifier.constrainAs(button) {
                start.linkTo(text.end, margin = 24.dp)
                top.linkTo(text.top)
                bottom.linkTo(text.bottom)
            }
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_backspace),
                contentDescription = stringResource(id = R.string.delete)
            )
        }
    }
}

@Composable
fun TimerText(time: String, modifier: Modifier = Modifier) {
    Text(
        time,
        modifier = modifier,
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.h2
    )
}

@Composable
fun Keypad(onClick: (Long) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Box(
                Modifier.weight(1f)
            ) {
                KeypadButton(value = 1, onClick)
            }
            Box(
                Modifier.weight(1f)
            ) {
                KeypadButton(value = 2, onClick)
            }
            Box(
                Modifier.weight(1f)
            ) {
                KeypadButton(value = 3, onClick)
            }
        }
        Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Box(
                Modifier.weight(1f)
            ) {
                KeypadButton(value = 4, onClick)
            }
            Box(
                Modifier.weight(1f)
            ) {
                KeypadButton(value = 5, onClick)
            }
            Box(
                Modifier.weight(1f)
            ) {
                KeypadButton(value = 6, onClick)
            }
        }
        Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Box(
                Modifier.weight(1f)
            ) {
                KeypadButton(value = 7, onClick)
            }
            Box(
                Modifier.weight(1f)
            ) {
                KeypadButton(value = 8, onClick)
            }
            Box(
                Modifier.weight(1f)
            ) {
                KeypadButton(value = 9, onClick)
            }
        }
        Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Box(
                Modifier.weight(1f)
            )
            Box(
                Modifier.weight(1f)
            ) {
                KeypadButton(value = 0, onClick)
            }
            Box(
                Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun KeypadButton(value: Long, onClick: (Long) -> Unit) {
    TextButton(onClick = { onClick(value) }) {
        Text(
            value.toString(),
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.h1
        )
    }
}

@Composable
fun TimerCircle(
    duration: Int,
    modifier: Modifier = Modifier,
    progressState: MutableState<Float>,
) {
    val currentState = remember {
        MutableTransitionState(AnimatedCircleProgress.START)
            .apply { targetState = AnimatedCircleProgress.END }
    }

    val stroke = with(LocalDensity.current) { Stroke(10.dp.toPx()) }
    val transition = updateTransition(currentState)
    val angleOffset by transition.animateFloat(
        transitionSpec = {
            tween(
                durationMillis = duration,
                easing = LinearEasing
            )
        }
    ) { progress ->
        if (progress == AnimatedCircleProgress.START) {
            0f
        } else {
            1f
        }
    }

    progressState.value = angleOffset
    val angleOffsetX = angleOffset * 360
    Canvas(modifier) {
        val innerRadius = (size.minDimension - stroke.width) / 2
        val halfSize = size / 2.0f
        val topLeft = Offset(
            halfSize.width - innerRadius,
            halfSize.height - innerRadius
        )
        val size = Size(innerRadius * 2, innerRadius * 2)

        val startAngle = 0f
        val sweepAngle = angleOffsetX - startAngle
        drawArc(
            color = primary500,
            startAngle = startAngle,
            sweepAngle = sweepAngle,
            topLeft = topLeft,
            size = size,
            useCenter = false,
            style = stroke
        )
    }
}

private enum class AnimatedCircleProgress { START, END }
