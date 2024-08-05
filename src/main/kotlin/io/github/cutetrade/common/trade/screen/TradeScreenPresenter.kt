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

package io.github.cutetrade.common.trade.screen

import io.github.cutetrade.common.CommonProxySet
import io.github.cutetrade.common.pool.CommonProxyPool
import io.github.cutetrade.common.proxy.PlayerProxy
import io.github.cutetrade.common.trade.screen.handler.ScreenHandlerAgent

class TradeScreenPresenter private constructor(
    private val tradeScreenContext: TradeScreenContext,
    private val redPlayer: PlayerProxy,
    private val bluePlayer: PlayerProxy,
) {
    private var redScreenHandlerAgent: ScreenHandlerAgent? = null
    private var blueScreenHandlerAgent: ScreenHandlerAgent? = null

    fun openTradeScreen() {
        val factory =
            CommonProxyPool.getProxy<CommonProxySet.TradeScreenHandlerFactoryGetter>(
                CommonProxySet.TradeScreenHandlerFactoryGetter::class.java
            ).create(tradeScreenContext.context.tradeId)
        redPlayer.openHandledScreen(factory)
        bluePlayer.openHandledScreen(factory)

        redScreenHandlerAgent = redPlayer.currentScreenHandler()
        blueScreenHandlerAgent = bluePlayer.currentScreenHandler()

        redScreenHandlerAgent!!.addListener { handler, slotId, _ ->
            if (slotId !in 0 .. 8) {
                return@addListener
            }
            val slot = handler.getSlot(slotId)
            if (slot.hasStack()) {
                tradeScreenContext.context.redSetTradeItem(slotId, slot.stack())
            } else {
                tradeScreenContext.context.redRemoveTradeItem(slotId)
            }
        }

        blueScreenHandlerAgent!!.addListener { handler, slotId, _ ->
            if (slotId !in 0 .. 8) {
                return@addListener
            }
            val slot = handler.getSlot(slotId)
            if (slot.hasStack()) {
                tradeScreenContext.context.blueSetTradeItem(slotId, slot.stack())
            } else {
                tradeScreenContext.context.blueRemoveTradeItem(slotId)
            }
        }
    }

    fun syncTradeInventory() {
        if (redScreenHandlerAgent == null || blueScreenHandlerAgent == null) {
            throw IllegalStateException("screen handler is not initialized")
        }

        val redTradeInventory = tradeScreenContext.context.redTradeItemStack
        val blueTradeInventory = tradeScreenContext.context.blueTradeItemStack

        redTradeInventory.itemArray.forEachIndexed { index, tradeItem ->
            tradeItem?.let {
                val redRevision = redScreenHandlerAgent!!.nextRevision()
                redScreenHandlerAgent!!.setStackInSlot(index, redRevision, tradeItem.itemStack)

                val blueRevision = blueScreenHandlerAgent!!.nextRevision()
                blueScreenHandlerAgent!!.setStackInSlot(index + 9, blueRevision, tradeItem.itemStack)
            }
        }
        blueTradeInventory.itemArray.forEachIndexed { index, tradeItem ->
            tradeItem?.let {
                val redRevision = redScreenHandlerAgent!!.nextRevision()
                redScreenHandlerAgent!!.setStackInSlot(index + 9, redRevision, tradeItem.itemStack)

                val blueRevision = blueScreenHandlerAgent!!.nextRevision()
                blueScreenHandlerAgent!!.setStackInSlot(index, blueRevision, tradeItem.itemStack)
            }
        }
    }

    fun closeTradeScreen() {
        redPlayer.closeHandledScreen()
        bluePlayer.closeHandledScreen()
    }

    companion object {
        fun create(
            tradeScreenContext: TradeScreenContext,
            redPlayer: PlayerProxy,
            bluePlayer: PlayerProxy,
        ): TradeScreenPresenter = TradeScreenPresenter(
            tradeScreenContext,
            redPlayer,
            bluePlayer
        )
    }

}