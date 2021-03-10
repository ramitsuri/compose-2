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
package com.example.androiddevchallenge

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.Crossfade
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import com.example.androiddevchallenge.ui.screen.TimerEntryScreen
import com.example.androiddevchallenge.ui.screen.TimerRunScreen
import com.example.androiddevchallenge.ui.theme.MyTheme

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyTheme {
                MyApp(viewModel)
            }
        }
    }
}

@Composable
fun MyApp(
    viewModel: MainViewModel
) {
    Surface(color = MaterialTheme.colors.background) {
        var showTimerEntryScreen by remember { mutableStateOf(true) }
        var duration by remember { mutableStateOf(-1L) }
        viewModel.durationRemaining.observeAsState().value?.let {
            showTimerEntryScreen = it == -1L
            duration = it
        }
        Crossfade(targetState = showTimerEntryScreen) { show ->
            if (show) {
                viewModel.editTimeString.observeAsState().value?.let { timeString ->
                    TimerEntryScreen(
                        timeString,
                        { viewModel.onNumDelete() },
                        { num -> viewModel.onNumAdd(num) }
                    ) {
                        viewModel.startTimer()
                    }
                }
            } else {
                TimerRunScreen(viewModel, duration) {
                    viewModel.stopTimer()
                }
            }
        }
    }
}

@Preview("Light Theme", widthDp = 360, heightDp = 640)
@Composable
fun LightPreview() {
    MyTheme {
        MyApp(MainViewModel())
    }
}

@Preview("Dark Theme", widthDp = 360, heightDp = 640)
@Composable
fun DarkPreview() {
    MyTheme(darkTheme = true) {
        MyApp(MainViewModel())
    }
}
