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

import junit.framework.Assert.assertEquals
import org.junit.Test
import java.time.Duration

class TimerKeypadValueTest {
    @Test
    fun testStringRepresentation() {
        val timer = TimerKeypadValue(6)

        timer.onClear()
        assertEquals("", timer.getString())

        timer.onNumAdd(1)
        assertEquals("1", timer.getString())

        timer.onDelete()
        assertEquals("", timer.getString())

        timer.onNumAdd(1)
        assertEquals("1", timer.getString())

        timer.onNumAdd(2)
        assertEquals("12", timer.getString())

        timer.onNumAdd(3)
        assertEquals("123", timer.getString())

        timer.onDelete()
        assertEquals("12", timer.getString())

        timer.onDelete()
        assertEquals("1", timer.getString())

        timer.onDelete()
        assertEquals("", timer.getString())

        timer.onDelete()
        assertEquals("", timer.getString())

        timer.onClear()
        assertEquals("", timer.getString())

        timer.onNumAdd(99)
        assertEquals("", timer.getString())

        timer.onNumAdd(-112)
        assertEquals("", timer.getString())

        timer.onNumAdd(4)
        assertEquals("4", timer.getString())

        timer.onNumAdd(-2)
        assertEquals("4", timer.getString())

        timer.onDelete()
        assertEquals("", timer.getString())

        timer.onNumAdd(1)
        timer.onNumAdd(1)
        timer.onNumAdd(1)
        timer.onNumAdd(1)
        timer.onNumAdd(1)
        timer.onNumAdd(1)
        timer.onNumAdd(1)
        assertEquals("111111", timer.getString())

        timer.onDelete()
        assertEquals("11111", timer.getString())

        timer.onClear()
        assertEquals("", timer.getString())
    }

    @Test
    fun testDuration() {
        val timer = TimerKeypadValue(6)

        timer.onClear()
        assertEquals(Duration.ZERO, timer.getDuration())

        timer.onNumAdd(1)
        assertEquals(Duration.ofSeconds(1), timer.getDuration())

        timer.onDelete()
        assertEquals(Duration.ZERO, timer.getDuration())

        timer.onNumAdd(1)
        assertEquals(Duration.ofSeconds(1), timer.getDuration())

        timer.onNumAdd(2)
        assertEquals(Duration.ofSeconds(12), timer.getDuration())

        timer.onNumAdd(3)
        assertEquals(Duration.ofMinutes(1).plusSeconds(23), timer.getDuration())

        timer.onDelete()
        assertEquals(Duration.ofSeconds(12), timer.getDuration())

        timer.onDelete()
        assertEquals(Duration.ofSeconds(1), timer.getDuration())

        timer.onDelete()
        assertEquals(Duration.ofSeconds(0), timer.getDuration())

        timer.onDelete()
        assertEquals(Duration.ofSeconds(0), timer.getDuration())

        timer.onClear()
        assertEquals(Duration.ofSeconds(0), timer.getDuration())

        timer.onNumAdd(99)
        assertEquals(Duration.ofSeconds(0), timer.getDuration())

        timer.onNumAdd(-112)
        assertEquals(Duration.ofSeconds(0), timer.getDuration())

        timer.onNumAdd(4)
        assertEquals(Duration.ofSeconds(4), timer.getDuration())

        timer.onNumAdd(-2)
        assertEquals(Duration.ofSeconds(4), timer.getDuration())

        timer.onDelete()
        assertEquals(Duration.ofSeconds(0), timer.getDuration())

        timer.onNumAdd(1)
        timer.onNumAdd(1)
        timer.onNumAdd(1)
        timer.onNumAdd(1)
        timer.onNumAdd(1)
        timer.onNumAdd(1)
        timer.onNumAdd(1)
        assertEquals(
            Duration.ofHours(11)
                .plusMinutes(11)
                .plusSeconds(11),
            timer.getDuration()
        )

        timer.onDelete()
        assertEquals(
            Duration.ofHours(1)
                .plusMinutes(11)
                .plusSeconds(11),
            timer.getDuration()
        )

        timer.onClear()
        assertEquals(Duration.ZERO, timer.getDuration())

        timer.onNumAdd(9)
        timer.onNumAdd(9)
        timer.onNumAdd(9)
        timer.onNumAdd(9)
        timer.onNumAdd(9)
        timer.onNumAdd(9)
        assertEquals(
            Duration.ofHours(99)
                .plusMinutes(99)
                .plusSeconds(99),
            timer.getDuration()
        )
    }
}
