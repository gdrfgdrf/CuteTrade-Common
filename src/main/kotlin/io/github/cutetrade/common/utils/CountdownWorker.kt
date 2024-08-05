/*
 * Copyright 2024 CuteTrade's contributors
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package io.github.cutetrade.common.utils

import io.github.cutetrade.common.extension.launchIO
import io.github.cutetrade.common.extension.logError
import io.github.cutetrade.common.extension.logInfo
import io.github.cutetrade.common.extension.sleepSafety
import io.github.cutetrade.common.utils.thread.ThreadPoolService
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit

object CountdownWorker {
    private var stop = false
    private val tasks = ConcurrentHashMap<CountdownTask, Long>()

    fun start() {
        "Starting countdown worker".logInfo()

        stop = false
        ThreadPoolService.newTask(Worker)
    }

    fun reset() {
        stop = true
        tasks.clear()

        "Reset countdown worker".logInfo()
    }

    fun add(task: CountdownTask) {
        tasks[task] = System.currentTimeMillis()
    }

    object Worker : Runnable {
        @OptIn(DelicateCoroutinesApi::class)
        override fun run() {
            while (!stop) {
                runCatching {
                    val now = System.currentTimeMillis()

                    tasks.forEach { (task, startTime) ->
                        if (task.end) {
                            tasks.remove(task)
                            return@forEach
                        }

                        val timeout = TimeUnit.MILLISECONDS.convert(task.timeout, task.timeUnit)

                        if (now - startTime >= timeout) {
                            tasks.remove(task)
                            GlobalScope.launchIO {
                                task.endRun()
                            }
                        }
                    }

                }.onFailure {
                    "Error on countdown worker".logError(it)
                }

                sleepSafety(100)
            }
            stop = false

            "Countdown worker has been terminated".logInfo()
        }
    }

    class CountdownTask(
        val timeout: Long,
        val timeUnit: TimeUnit,
        val endRun: () -> Unit
    ) {
        var end: Boolean = false

        companion object {
            fun create(
                timeout: Long,
                timeUnit: TimeUnit,
                endRun: () -> Unit
            ) = CountdownTask(timeout, timeUnit, endRun)
        }
    }

}