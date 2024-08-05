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

package io.github.cutetrade.common.extension

import cutetrade.protobuf.CommonProto.Player
import io.github.cutetrade.common.manager.ProtobufPlayerManager
import io.github.cutetrade.common.manager.TradeManager
import io.github.cutetrade.common.proxy.PlayerProxy
import io.github.cutetrade.common.trade.TradeContext
import io.github.cutetrade.common.manager.TradeRequestManager
import io.github.cutetrade.common.pool.PlayerProxyPool
import io.github.cutetrade.common.trade.TradeRequest

fun Player.isOnline(): Boolean {
    return PlayerProxyPool.contains(this.name)
}

fun PlayerProxy.findProtobufPlayer(): Player? {
    return ProtobufPlayerManager.findPlayer(this.playerName)
}

fun PlayerProxy.getTradeRequest(redPlayerProxy: PlayerProxy): TradeRequest? {
    val requests = TradeRequestManager.getRequests(redPlayerProxy)
    return requests?.stream()
        ?.filter { tradeRequest ->
            tradeRequest.bluePlayerProxy == this@getTradeRequest
        }
        ?.findAny()
        ?.orElse(null)
}

fun PlayerProxy.removeTradeRequest(redPlayerEntity: PlayerProxy) {
    val tradeRequest = getTradeRequest(redPlayerEntity)
    tradeRequest?.let {
        TradeRequestManager.removeRequest(redPlayerEntity, tradeRequest)
    }
}

fun PlayerProxy.isTrading(): Boolean {
    return TradeManager.trades.contains(this)
}

fun PlayerProxy.currentTrade(): TradeContext? {
    return TradeManager.trades[this]
}

fun PlayerProxy.checkInTrade(): Boolean {
    val currentTrade = currentTrade()
    if (currentTrade == null) {
        this.translationScope {
            toCommandTranslation("no_transaction_in_progress")
                .send()
        }
        return false
    }
    return true
}

fun PlayerProxy.isRed(): Boolean {
    val currentTrade = currentTrade()
    return currentTrade?.redPlayer?.playerName == this.playerName
}