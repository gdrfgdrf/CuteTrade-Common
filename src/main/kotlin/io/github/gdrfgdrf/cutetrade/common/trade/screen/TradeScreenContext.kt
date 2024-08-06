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

package io.github.gdrfgdrf.cutetrade.common.trade.screen

import io.github.gdrfgdrf.cutetrade.common.proxy.PlayerProxy
import io.github.gdrfgdrf.cutetrade.common.trade.TradeContext

class TradeScreenContext private constructor(
    val context: TradeContext,
    private val redPlayer: PlayerProxy,
    private val bluePlayer: PlayerProxy
) {
    private var initialized = false

    private lateinit var screenPresenter: TradeScreenPresenter

    fun initialize() {
        screenPresenter = TradeScreenPresenter.create(
            this,
            redPlayer,
            bluePlayer
        )

        initialized = true
    }

    fun syncTradeInventory() {
        screenPresenter.syncTradeInventory()
    }

    fun start() {
        if (!initialized) {
            throw IllegalStateException("Trade is not initialized")
        }

        screenPresenter.openTradeScreen()
    }

    fun end() {
        if (!initialized) {
            throw IllegalStateException("Trade is not initialized")
        }
        screenPresenter.closeTradeScreen()
    }

    companion object {
        fun create(
            context: TradeContext,
            redPlayer: PlayerProxy,
            bluePlayer: PlayerProxy
        ): TradeScreenContext = TradeScreenContext(context, redPlayer, bluePlayer)
    }
}