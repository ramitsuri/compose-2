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

import java.time.Duration

class TimerKeypadValue(private val maxLength: Int = 6) {
    private val builder: StringBuilder = StringBuilder()

    fun onNumAdd(num: Long) {
        if (num > 9 || num < 0) {
            return
        }
        if (builder.isEmpty() && num == 0L) {
            return
        }
        if (builder.length < maxLength) {
            builder.append(num)
        }
    }

    fun onDelete() {
        if (builder.isEmpty()) {
            return
        }
        if (builder.length == 1) {
            builder.clear()
            return
        }
        val existing = builder.substring(0, builder.length - 1)
        builder.clear().append(existing)
    }

    fun onClear() {
        builder.clear()
    }

    fun getDuration(): Duration {
        return Duration.ZERO
            .plusSeconds(getSeconds())
            .plusMinutes(getMinutes())
            .plusHours(getHours())
    }

    private fun getSeconds(): Long {
        return when {
            builder.length >= 2 -> {
                builder.substring(builder.length - 2, builder.length).toLong()
            }
            builder.length == 1 -> {
                builder.toString().toLong()
            }
            else -> {
                0
            }
        }
    }

    private fun getMinutes(): Long {
        return when {
            builder.length >= 4 -> {
                builder.substring(builder.length - 4, builder.length - 2).toLong()
            }
            builder.length == 3 -> {
                builder.substring(builder.length - 3, builder.length - 2).toLong()
            }
            else -> {
                0
            }
        }
    }

    private fun getHours(): Long {
        return when {
            builder.length >= 6 -> {
                builder.substring(builder.length - 6, builder.length - 4).toLong()
            }
            builder.length == 5 -> {
                builder.substring(builder.length - 5, builder.length - 4).toLong()
            }
            else -> {
                0
            }
        }
    }

    fun getString(separator: String = ":"): String {
        return "%02d".format(getHours()) + separator +
            "%02d".format(getMinutes()) + separator +
            "%02d".format(getSeconds())
    }
}
