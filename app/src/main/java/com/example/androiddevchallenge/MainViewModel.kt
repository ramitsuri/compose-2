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

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.lang.StringBuilder
import java.time.Duration
import kotlin.system.measureTimeMillis

class MainViewModel : ViewModel() {
    private val TIME_LENGTH = 6
    private val SEPARATOR = ":"

    private lateinit var timer: CountDownTimer

    private val _editTimeString: MutableLiveData<String> =
        MutableLiveData(getRunTimeRepresentation(0))
    val editTimeString: LiveData<String>
        get() = _editTimeString

    private val _durationRemaining: MutableLiveData<Long> =
        MutableLiveData(-1)
    val durationRemaining: LiveData<Long>
        get() = _durationRemaining

    private val timerKeypadValue: TimerKeypadValue = TimerKeypadValue(TIME_LENGTH)

    fun startTimer() {
        val durationFromKeypad = timerKeypadValue.getDuration().toMillis()
        if (durationFromKeypad == 0L) {
            return
        }
        val duration = durationFromKeypad + 1000
        val timeElapsed = measureTimeMillis {
            timer = object : CountDownTimer(
                duration,
                1000
            ) {
                override fun onTick(millisUntilFinished: Long) {
                    updateDurationRemaining(millisUntilFinished)
                }

                override fun onFinish() {
                    stopTimer()
                }
            }.start()
        }
        updateDurationRemaining(duration - timeElapsed)
    }

    fun stopTimer() {
        if (this::timer.isInitialized) {
            timer.cancel()
        }
        timerKeypadValue.onClear()
        updateDurationRemaining(-1L)
        updateEditTimeString(getEditTimeRepresentation())
    }

    fun onNumAdd(num: Long) {
        timerKeypadValue.onNumAdd(num)
        updateEditTimeString(getEditTimeRepresentation())
    }

    fun onNumDelete() {
        timerKeypadValue.onDelete()
        updateEditTimeString(getEditTimeRepresentation())
    }

    override fun onCleared() {
        super.onCleared()
        stopTimer()
    }

    fun getRunTimeRepresentation(millis: Long): String {
        var duration = Duration.ofMillis(millis)
        val representation = StringBuilder()

        representation.append("%02d".format(duration.toHours()) + SEPARATOR)
        duration = duration.minusHours(duration.toHours())

        representation.append("%02d".format(duration.toMinutes()) + SEPARATOR)
        duration = duration.minusMinutes(duration.toMinutes())

        representation.append("%02d".format(duration.seconds))

        return representation.toString()
    }

    private fun getEditTimeRepresentation(): String {
        return timerKeypadValue.getString(SEPARATOR)
    }

    private fun updateEditTimeString(time: String) {
        _editTimeString.value = time
    }

    private fun updateDurationRemaining(time: Long) {
        _durationRemaining.value = time
    }
}
