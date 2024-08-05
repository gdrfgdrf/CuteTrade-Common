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

package io.github.cutetrade.common.manager

import cutetrade.protobuf.CommonProto.Trade
import cutetrade.protobuf.CommonProto.TradeResult
import cutetrade.protobuf.StorableProto.TradeStore
import io.github.cutetrade.common.enums.TradeStatus
import io.github.cutetrade.common.extension.findProtobufPlayer
import io.github.cutetrade.common.extension.runSyncTask
import io.github.cutetrade.common.extension.toTimestamp
import io.github.cutetrade.common.proxy.PlayerProxy
import io.github.cutetrade.common.trade.TradeContext
import io.github.cutetrade.common.utils.Protobuf
import java.util.concurrent.ConcurrentHashMap

object TradeManager {
    var tradeProtobuf: Protobuf<TradeStore>? = null
    var trades = ConcurrentHashMap<PlayerProxy, TradeContext>()

    fun createTrade(
        redPlayerProxy: PlayerProxy,
        bluePlayerProxy: PlayerProxy
    ) {
        val tradeContext = TradeContext.create(redPlayerProxy, bluePlayerProxy)
        tradeContext.initialize()
    }

    fun tradeInitialized(
        tradeContext: TradeContext
    ) {
        val redPlayer = tradeContext.redPlayer
        val bluePlayer = tradeContext.bluePlayer
        trades[redPlayer] = tradeContext
        trades[bluePlayer] = tradeContext
    }

    fun tradeEnd(
        tradeContext: TradeContext
    ) {
        val redPlayer = tradeContext.redPlayer
        val bluePlayer = tradeContext.bluePlayer
        trades.remove(redPlayer)
        trades.remove(bluePlayer)
    }

    fun recordTrade(
        tradeContext: TradeContext
    ) = runSyncTask(tradeProtobuf!!) {
        val builder = Trade.newBuilder()
            .setRedName(tradeContext.redPlayer.playerName)
            .setBlueName(tradeContext.bluePlayer.playerName)
            .setId(tradeContext.tradeId)

        if (tradeContext.startTime != null) {
            builder.setStartTime(tradeContext.startTime!!.toTimestamp())
        }
        if (tradeContext.endTime != null) {
            builder.setEndTime(tradeContext.endTime!!.toTimestamp())
        }
        if (tradeContext.status != TradeStatus.FINISHED) {
            builder.setTradeResult(TradeResult.TRADE_RESULT_TERMINATED)
        } else {
            builder.setTradeResult(TradeResult.TRADE_RESULT_FINISHED)
        }

        if (tradeContext.status == TradeStatus.FINISHED) {
            val redAddBy = tradeContext.redTradeItemStack.playerProxy
            val blueAddBy = tradeContext.blueTradeItemStack.playerProxy

            tradeContext.redTradeItemStack.itemArray.forEach { tradeItem ->
                tradeItem?.let {
                    if (!it.itemStack.isEmpty()) {
                        val protobufTradeItem = it.itemStack.toProtobufTradeItem(redAddBy.playerName)
                        builder.addRedItemResult(protobufTradeItem)
                    }
                }
            }
            tradeContext.blueTradeItemStack.itemArray.forEach { tradeItem ->
                tradeItem?.let {
                    if (!it.itemStack.isEmpty()) {
                        val protobufTradeItem = it.itemStack.toProtobufTradeItem(blueAddBy.playerName)
                        builder.addBlueItemResult(protobufTradeItem)
                    }
                }
            }
        }

        val redProtobufPlayer = tradeContext.redPlayer.findProtobufPlayer()
        val blueProtobufPlayer = tradeContext.bluePlayer.findProtobufPlayer()
        if (redProtobufPlayer != null) {
            ProtobufPlayerManager.recordTrade(redProtobufPlayer, tradeContext.tradeId)
        }
        if (blueProtobufPlayer != null) {
            ProtobufPlayerManager.recordTrade(blueProtobufPlayer, tradeContext.tradeId)
        }

        tradeProtobuf!!.rebuild { tradeStore ->
            val tradeStoreBuilder = tradeStore!!.toBuilder()
                .putTradeIdToTrade(tradeContext.tradeId, builder.build())
            if (redProtobufPlayer != null) {
                tradeStoreBuilder.putTradeIdToPlayerName(
                    tradeContext.tradeId,
                    redProtobufPlayer.name
                )
            }
            if (blueProtobufPlayer != null) {
                tradeStoreBuilder.putTradeIdToPlayerName(
                    tradeContext.tradeId,
                    blueProtobufPlayer.name
                )
            }
            tradeStoreBuilder.build()
        }
        tradeProtobuf!!.save()
    }
}