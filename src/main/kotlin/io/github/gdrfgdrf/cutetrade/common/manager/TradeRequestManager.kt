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

package io.github.gdrfgdrf.cutetrade.common.manager

import io.github.gdrfgdrf.cutetrade.common.extension.getTradeRequest
import io.github.gdrfgdrf.cutetrade.common.extension.translationScope
import io.github.gdrfgdrf.cutetrade.common.proxy.PlayerProxy
import io.github.gdrfgdrf.cutetrade.common.trade.TradeRequest
import java.util.concurrent.ConcurrentHashMap

object TradeRequestManager {
    private val tradeRequests = ConcurrentHashMap<PlayerProxy, ArrayList<TradeRequest>>()
    private val bluePlayerToRedPlayer = ConcurrentHashMap<PlayerProxy, ArrayList<PlayerProxy>>()

    fun request(
        redPlayerProxy: PlayerProxy,
        bluePlayerProxy: PlayerProxy
    ) {
        val redSentToBlue = redPlayerProxy.getTradeRequest(bluePlayerProxy)
        val blueSentToRed = bluePlayerProxy.getTradeRequest(redPlayerProxy)

        if (redSentToBlue != null || blueSentToRed != null) {
            redPlayerProxy.translationScope {
                toCommandTranslation("request_exist")
                    .format0(bluePlayerProxy.playerName)
                    .send()
            }
            return
        }

        val tradeRequest = TradeRequest.create(redPlayerProxy, bluePlayerProxy)
        addRequest(redPlayerProxy, tradeRequest)

        tradeRequest.send()
    }

    fun getRedPlayersByBluePlayer(
        bluePlayerProxy: PlayerProxy
    ): List<PlayerProxy>? {
        return bluePlayerToRedPlayer[bluePlayerProxy]
    }

    fun getRequests(
        redPlayerProxy: PlayerProxy,
    ): List<TradeRequest>? {
        return tradeRequests[redPlayerProxy]
    }

    fun addRequest(
        redPlayerProxy: PlayerProxy,
        tradeRequest: TradeRequest
    ) {
        val redList = tradeRequests.computeIfAbsent(redPlayerProxy) { _ ->
            ArrayList()
        }
        redList.add(tradeRequest)

        val computeIfAbsent = bluePlayerToRedPlayer.computeIfAbsent(tradeRequest.bluePlayerProxy) { _ ->
            ArrayList()
        }
        computeIfAbsent.add(redPlayerProxy)
    }

    fun removeRequest(
        redPlayerProxy: PlayerProxy,
        tradeRequest: TradeRequest
    ) {
        val redList = tradeRequests.computeIfAbsent(redPlayerProxy) { _ ->
            ArrayList()
        }
        redList.remove(tradeRequest)

        val computeIfAbsent = bluePlayerToRedPlayer.computeIfAbsent(tradeRequest.bluePlayerProxy) { _ ->
            ArrayList()
        }
        computeIfAbsent.remove(redPlayerProxy)
    }
}