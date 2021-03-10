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

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.Duration

class Timer internal constructor(
    private val timerDuration: Duration,
    private val interval: Duration = Duration.ofSeconds(1),
    private val coroutineScope: CoroutineScope,
    private val action: () -> Unit
) {
    private var durationElapsed = Duration.ZERO
    private var job: Job? = null

    fun start() {
        job = coroutineScope.launch {
            while (durationElapsed < timerDuration) {
                try {
                    action()
                } catch (e: Throwable) {
                }
                delay(interval.toMillis())
                durationElapsed = durationElapsed.plus(interval)
            }
        }
    }

    fun stop() {
        job?.cancel("Stop requested")
    }
}
