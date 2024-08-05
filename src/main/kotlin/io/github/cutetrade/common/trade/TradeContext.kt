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

import cutetrade.protobuf.CommonProto.TradeResult
import io.github.cutetrade.common.enums.TradeStatus
import io.github.cutetrade.common.enums.TraderState
import io.github.cutetrade.common.extension.generateTradeId
import io.github.cutetrade.common.manager.TradeManager
import io.github.cutetrade.common.proxy.ItemStackProxy
import io.github.cutetrade.common.proxy.PlayerProxy
import io.github.cutetrade.common.trade.screen.TradeScreenContext
import java.time.Instant

class TradeContext private constructor(
    val redPlayer: PlayerProxy,
    val bluePlayer: PlayerProxy
) {
    private var initialized = false
    private var redPlayerInitialized = false
    private var bluePlayerInitialized = false

    lateinit var tradeId: String

    var startTime: Instant? = null
    var endTime: Instant? = null
    private lateinit var result: TradeResult
    lateinit var status: TradeStatus

    private lateinit var tradeScreenContext: TradeScreenContext

    private lateinit var presenter: TradePresenter

    var redState: TraderState = TraderState.UNCHECKED
    var blueState: TraderState = TraderState.UNCHECKED
    lateinit var redTradeItemStack: TradeItemStack
    lateinit var blueTradeItemStack: TradeItemStack

    fun check() {
        if (!initialized) {
            throw IllegalStateException("Trade is not initialized")
        }
    }

    fun initialize() {
        tradeId = generateTradeId()

        startTime = Instant.now()
        result = TradeResult.TRADE_RESULT_DEFAULT

        tradeScreenContext = TradeScreenContext.create(
            this,
            redPlayer,
            bluePlayer
        )
        tradeScreenContext.initialize()

        presenter = TradePresenter.create(this)
        presenter.initialize()

        redTradeItemStack = TradeItemStack.create(redPlayer)
        blueTradeItemStack = TradeItemStack.create(bluePlayer)

        initialized = true
        status = TradeStatus.INITIALIZED

        TradeManager.tradeInitialized(this)
    }

    fun redPlayerInitialized() {
        check()
        redPlayerInitialized = true
        presenter.broadcastRedInitialized()
        if (bluePlayerInitialized) {
            start()
        }
    }

    fun bluePlayerInitialized() {
        check()
        bluePlayerInitialized = true
        presenter.broadcastBlueInitialized()
        if (redPlayerInitialized) {
            start()
        }
    }

    @Synchronized
    fun start() {
        check()
        if (status == TradeStatus.STARTED) {
            return
        }

        presenter.start()
        tradeScreenContext.start()

        status = TradeStatus.STARTED

        updateRedState(
            TraderState.UNCHECKED,
            playSound = false,
            sendMessage = false
        )
        updateBlueState(
            TraderState.UNCHECKED,
            playSound = false,
            sendMessage = false
        )
        presenter.playStartSound()
    }

    fun updateRedState(
        redState: TraderState,
        playSound: Boolean = true,
        sendMessage: Boolean = true
    ) {
        if (redState == this.redState) {
            return
        }

        this.redState = redState
        presenter.updateState(redState, blueState)
        if (playSound) {
            if (redState == TraderState.CHECKED) {
                presenter.playStatePositiveSound()
            } else {
                presenter.playStateNegativeSound()
            }
        }
        if (sendMessage) {
            presenter.broadcastRedStateChange(redState)
        }
        checkState()
    }

    fun updateBlueState(
        blueState: TraderState,
        playSound: Boolean = true,
        sendMessage: Boolean = true
    ) {
        if (blueState == this.blueState) {
            return
        }

        this.blueState = blueState
        presenter.updateState(redState, blueState)
        if (playSound) {
            if (blueState == TraderState.CHECKED) {
                presenter.playStatePositiveSound()
            } else {
                presenter.playStateNegativeSound()
            }
        }
        if (sendMessage) {
            presenter.broadcastBlueStateChange(blueState)
        }
        checkState()
    }

    fun checkState() {
        if (redState == TraderState.CHECKED && blueState == TraderState.CHECKED) {
            finish()
        }
    }

    fun redSetTradeItem(
        index: Int,
        itemStack: ItemStackProxy,
        playSound: Boolean = true,
        updateState: Boolean = true,
        broadcastMessage: Boolean = true
    ) {
        redTradeItemStack.setTradeItem(index, itemStack)
        tradeScreenContext.syncTradeInventory()
        if (playSound) {
            presenter.playAddItemSound()
        }
        if (updateState) {
            updateRedState(TraderState.UNCHECKED)
            updateBlueState(TraderState.UNCHECKED)
        }
        if (broadcastMessage) {
            presenter.broadcastRedAddItemMessage(itemStack)
        }
    }

    fun blueSetTradeItem(
        index: Int,
        itemStack: ItemStackProxy,
        playSound: Boolean = true,
        updateState: Boolean = true,
        broadcastMessage: Boolean = true
    ) {
        blueTradeItemStack.setTradeItem(index, itemStack)
        tradeScreenContext.syncTradeInventory()
        if (playSound) {
            presenter.playAddItemSound()
        }
        if (updateState) {
            updateRedState(TraderState.UNCHECKED)
            updateBlueState(TraderState.UNCHECKED)
        }
        if (broadcastMessage) {
            presenter.broadcastBlueAddItem(itemStack)
        }
    }

    fun redRemoveTradeItem(
        index: Int
    ) {
        val itemStack = redTradeItemStack.get(index)
        itemStack?.let {
            presenter.broadcastRedRemoveItemMessage(it.itemStack)
        }

        redTradeItemStack.removeTradeItem(index)
        tradeScreenContext.syncTradeInventory()
        presenter.playRemoveItemSound()

        updateRedState(TraderState.UNCHECKED)
        updateBlueState(TraderState.UNCHECKED)
    }

    fun blueRemoveTradeItem(
        index: Int
    ) {
        val itemStack = blueTradeItemStack.get(index)
        itemStack?.let {
            presenter.broadcastBlueRemoveItemMessage(it.itemStack)
        }

        blueTradeItemStack.removeTradeItem(index)
        tradeScreenContext.syncTradeInventory()
        presenter.playRemoveItemSound()

        updateRedState(TraderState.UNCHECKED)
        updateBlueState(TraderState.UNCHECKED)
    }

    fun terminate() {
        status = TradeStatus.TERMINATED
        end(false)
        presenter.playTerminateSound()
        presenter.broadcastTerminateMessage()
    }

    fun finish() {
        status = TradeStatus.FINISHED
        end(true)

        presenter.playFinishSound()
        presenter.broadcastFinishMessage()
    }

    fun end(
        normal: Boolean
    ) {
        if (!initialized) {
            throw IllegalStateException("Trade is not initialized")
        }
        endTime = Instant.now()
        tradeScreenContext.end()

        if (!normal) {
            redTradeItemStack.returnAll()
            blueTradeItemStack.returnAll()
        } else {
            val redTradeItemStackCopied = redTradeItemStack.copy()
            val blueTradeItemStackCopied = blueTradeItemStack.copy()

            redTradeItemStackCopied.moveTo(bluePlayer)
            blueTradeItemStackCopied.moveTo(redPlayer)

            redTradeItemStackCopied.removeAll()
            blueTradeItemStackCopied.removeAll()
        }

        presenter.end()

        TradeManager.tradeEnd(this)
        TradeManager.recordTrade(this)
    }

    companion object {
        fun create(
            redPlayer: PlayerProxy,
            bluePlayer: PlayerProxy
        ): TradeContext = TradeContext(redPlayer, bluePlayer)
    }

}