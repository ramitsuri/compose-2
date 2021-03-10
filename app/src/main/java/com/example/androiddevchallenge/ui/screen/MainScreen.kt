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
package com.example.androiddevchallenge.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.androiddevchallenge.MainViewModel
import com.example.androiddevchallenge.R
import com.example.androiddevchallenge.ui.Keypad
import com.example.androiddevchallenge.ui.TimerCircle
import com.example.androiddevchallenge.ui.TimerEdit
import com.example.androiddevchallenge.ui.TimerText
import com.example.androiddevchallenge.ui.TimerTopAppBar

@Composable
fun TimerEntryScreen(
    timeString: String,
    onNumDelete: () -> Unit,
    onNumPadClick: (Long) -> Unit,
    onTimerStart: () -> Unit
) {
    Scaffold(
        topBar = {
            TimerTopAppBar(
                titleId = R.string.title,
                iconId = R.drawable.ic_icon,
                iconContentDescriptionId = R.string.title
            )
        },
        content = {
            Column(verticalArrangement = Arrangement.Bottom, modifier = Modifier.padding(8.dp)) {
                TimerEdit(timeString, onNumDelete)
                Spacer(modifier = Modifier.height(44.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                )
                Keypad(
                    onClick = {
                        onNumPadClick(it)
                    }
                )
                Spacer(modifier = Modifier.height(44.dp))
                Button(
                    onClick = onTimerStart,
                    modifier = Modifier.fillMaxWidth().align(Alignment.CenterHorizontally)
                ) {
                    Text(
                        text = stringResource(id = R.string.start)
                    )
                }
            }
        }
    )
}

@Composable
fun TimerRunScreen(viewModel: MainViewModel, duration: Long, onTimerStop: () -> Unit) {
    Scaffold(
        topBar = {
            TimerTopAppBar(
                titleId = R.string.title,
                iconId = R.drawable.ic_icon,
                iconContentDescriptionId = R.string.title
            )
        },
        content = {
            Column(verticalArrangement = Arrangement.Bottom, modifier = Modifier.padding(8.dp)) {
                Spacer(modifier = Modifier.height(120.dp))
                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TimerCircle(
                        duration = duration.toInt(),
                        modifier = Modifier
                            .padding(0.dp).align(Alignment.Center)
                            .fillMaxWidth().aspectRatio(1F),
                        progressState = mutableStateOf(0f)
                    )
                    viewModel.durationRemaining.observeAsState().value?.let { time ->
                        if (time >= 0) {
                            TimerText(
                                time = viewModel.getRunTimeRepresentation(time),
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(66.dp))
                Button(
                    onClick = onTimerStop,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(id = R.string.stop)
                    )
                }
            }
        }
    )
}
