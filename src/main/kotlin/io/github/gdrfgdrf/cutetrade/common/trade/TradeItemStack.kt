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

package io.github.gdrfgdrf.cutetrade.common.trade

import io.github.gdrfgdrf.cutetrade.common.extension.translationScope
import io.github.gdrfgdrf.cutetrade.common.proxy.ItemStackProxy
import io.github.gdrfgdrf.cutetrade.common.proxy.PlayerProxy

class TradeItemStack private constructor(val playerProxy: PlayerProxy) {
    var itemArray = arrayOfNulls<TradeItem>(9)

    fun size(): Int = itemArray.size

    fun get(index: Int): TradeItem? {
        if (index >= 9) {
            return null
        }
        return itemArray[index]
    }

    fun setTradeItem(
        index: Int,
        itemStack: ItemStackProxy
    ) {
        if (index >= 9 || index < 0) {
            playerProxy.translationScope {
                toScreenTranslation("trade_item_limited")
                    .send()
            }
            return
        }
        itemArray[index] = TradeItem(itemStack)
    }

    fun removeTradeItem(
        index: Int
    ) {
        itemArray[index] = null
    }

    fun removeAll() {
        itemArray = arrayOfNulls(9)
    }

    fun returnAll() {
        itemArray.forEach {
            it?.let {
                playerProxy.offerOrDrop(it.itemStack)
            }
        }
    }

    fun moveTo(targetPlayerProxy: PlayerProxy) {
        itemArray.forEachIndexed { _, it ->
            it?.let {
                targetPlayerProxy.offerOrDrop(it.itemStack)
            }
        }
    }

    fun copy(): TradeItemStack {
        val tradeItemStack = TradeItemStack(playerProxy)
        itemArray.forEachIndexed { index, it ->
            it?.let {
                if (!it.itemStack.isEmpty()) {
                    tradeItemStack.setTradeItem(index, it.itemStack.copy())
                }
            }
        }
        return tradeItemStack
    }

    class TradeItem(var itemStack: ItemStackProxy)

    companion object {
        fun create(playerEntity: PlayerProxy) = TradeItemStack(playerEntity)
    }
}