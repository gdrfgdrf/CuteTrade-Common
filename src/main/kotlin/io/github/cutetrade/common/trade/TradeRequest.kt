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

package io.github.cutetrade.common.trade

import io.github.cutetrade.common.extension.removeTradeRequest
import io.github.cutetrade.common.extension.translationScope
import io.github.cutetrade.common.manager.TradeManager
import io.github.cutetrade.common.proxy.PlayerProxy
import io.github.cutetrade.common.utils.CountdownWorker
import java.util.concurrent.TimeUnit

class TradeRequest private constructor(
    private val redPlayerProxy: PlayerProxy,
    val bluePlayerProxy: PlayerProxy
) {
    private val task = CountdownWorker.CountdownTask(30000L, TimeUnit.MILLISECONDS) {
        redPlayerProxy.translationScope {
            toCommandTranslation("request_timeout_for_red")
                .format0(bluePlayerProxy.playerName)
                .send()
        }
        bluePlayerProxy.translationScope {
            toCommandTranslation("request_timeout_for_blue")
                .format0(redPlayerProxy.playerName)
                .send()
        }

        timeout()
    }

    fun send() {
        redPlayerProxy.translationScope {
            toCommandTranslation("request_sent_for_red")
                .format0(bluePlayerProxy.playerName)
                .send()
        }
        bluePlayerProxy.translationScope {
            val acceptMessage = toCommandText("click_to_accept")
                .runCommand("/trade-public accept ${redPlayerProxy.playerName}")
            val declineMessage = toCommandText("click_to_decline")
                .runCommand("/trade-public decline ${redPlayerProxy.playerName}")

            toCommandTranslation("request_received_for_blue")
                .format0(redPlayerProxy.playerName)
                .append(acceptMessage)
                .append(declineMessage)
                .send()
        }

        CountdownWorker.add(task)
    }

    fun accept() {
        end()

        redPlayerProxy.translationScope {
            toCommandTranslation("accept_request_for_red")
                .format0(bluePlayerProxy.playerName)
                .send()
        }
        bluePlayerProxy.translationScope {
            toCommandTranslation("accept_request_for_blue")
                .format0(redPlayerProxy.playerName)
                .send()
        }

        TradeManager.createTrade(redPlayerProxy, bluePlayerProxy)
    }

    fun decline() {
        end()

        redPlayerProxy.translationScope {
            toCommandTranslation("decline_request_for_red")
                .format0(bluePlayerProxy.playerName)
                .send()
        }
        bluePlayerProxy.translationScope {
            toCommandTranslation("decline_request_for_blue")
                .format0(redPlayerProxy.playerName)
                .send()
        }
    }

    fun timeout() {
        end()
    }

    fun end() {
        task.end = true
        bluePlayerProxy.removeTradeRequest(redPlayerProxy)
    }

    companion object {
        fun create(redPlayerProxy: PlayerProxy, bluePlayerProxy: PlayerProxy) =
            TradeRequest(redPlayerProxy, bluePlayerProxy)
    }

}