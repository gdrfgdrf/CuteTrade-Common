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

import io.github.cutetrade.common.CommonFunctions
import io.github.cutetrade.common.enums.TraderState
import io.github.cutetrade.common.extension.translationScope
import io.github.cutetrade.common.network.NetworkManager
import io.github.cutetrade.common.network.interfaces.PacketAdapter
import io.github.cutetrade.common.operation.server.Operators
import io.github.cutetrade.common.pool.CommonFunctionsPool
import io.github.cutetrade.common.proxy.ItemStackProxy
import io.github.cutetrade.common.translation.TranslationAgent

class TradePresenter private constructor(
    private val tradeContext: TradeContext
) {
    private val networkFunctions =
        CommonFunctionsPool.getFunctions<CommonFunctions.NetworkFunctions>(CommonFunctions.NetworkFunctions::class.java)

    fun initialize() {
        val s2COperationPacketCommon = networkFunctions.createS2C(Operators.CLIENT_INITIALIZE_TRADE)
        s2COperationPacketCommon.setStringArgs(arrayOf(
            tradeContext.tradeId,
            tradeContext.redPlayer.playerName,
            tradeContext.bluePlayer.playerName
        ))
        broadcastOperation(s2COperationPacketCommon)
    }

    fun broadcastRedInitialized() {
        broadcastTradeMessage("player_trade_initialized") {
            it.format0(tradeContext.redPlayer.playerName)
        }
    }

    fun broadcastBlueInitialized() {
        broadcastTradeMessage("player_trade_initialized") {
            it.format0(tradeContext.bluePlayer.playerName)
        }
    }

    fun start() {
        val s2COperationPacketCommon = networkFunctions.createS2C(Operators.CLIENT_TRADE_START)
        broadcastOperation(s2COperationPacketCommon)

        broadcastTradeMessage("trade_start")
    }

    fun updateState(
        redState: TraderState,
        blueState: TraderState
    ) {
        val stringStates: Array<String?> = arrayOf(redState.name, blueState.name)
        val s2COperationPacketCommon = networkFunctions.createS2C(Operators.CLIENT_UPDATE_TRADER_STATE)
        s2COperationPacketCommon.setStringArgs(stringStates)

        broadcastOperation(s2COperationPacketCommon)
    }

    fun broadcastRedStateChange(
        redState: TraderState
    ) {
        val rootOneself = if (redState == TraderState.CHECKED) {
            "state_checked_oneself"
        } else {
            "state_unchecked_oneself"
        }
        val rootOther = if (redState == TraderState.CHECKED) {
            "state_checked_other"
        } else {
            "state_unchecked_other"
        }

        tradeContext.redPlayer.translationScope {
            val prefix = toTradeText("presenter_prefix")
            val checkStateMessage = toTradeTranslation(rootOneself)

            checkStateMessage.send(prefix)
        }
        tradeContext.bluePlayer.translationScope {
            val prefix = toTradeText("presenter_prefix")
            val checkStateMessage = toTradeTranslation(rootOther)
                .format0(tradeContext.redPlayer.playerName)

            checkStateMessage.send(prefix)
        }
    }

    fun broadcastBlueStateChange(
        blueState: TraderState
    ) {
        val rootOneself = if (blueState == TraderState.CHECKED) {
            "state_checked_oneself"
        } else {
            "state_unchecked_oneself"
        }
        val rootOther = if (blueState == TraderState.CHECKED) {
            "state_checked_other"
        } else {
            "state_unchecked_other"
        }

        tradeContext.redPlayer.translationScope {
            val prefix = toTradeText("presenter_prefix")
            val checkStateMessage = toTradeTranslation(rootOther)
                .format0(tradeContext.bluePlayer.playerName)

            checkStateMessage.send(prefix)
        }
        tradeContext.bluePlayer.translationScope {
            val prefix = toTradeText("presenter_prefix")
            val checkStateMessage = toTradeTranslation(rootOneself)

            checkStateMessage.send(prefix)
        }
    }

    fun playStartSound() {
        val soundEvent = getSoundGetter().getTradeStartSound()
        tradeContext.redPlayer.playSound(soundEvent, 100F, 1F)
        tradeContext.bluePlayer.playSound(soundEvent, 100F, 1F)
    }

    fun playStatePositiveSound() {
        val soundEvent = getSoundGetter().getStatePositiveSound()
        tradeContext.redPlayer.playSound(soundEvent, 100F, 1F)
        tradeContext.bluePlayer.playSound(soundEvent, 100F, 1F)
    }

    fun playStateNegativeSound() {
        val soundEvent = getSoundGetter().getStateNegativeSound()
        tradeContext.redPlayer.playSound(soundEvent, 100F, 1F)
        tradeContext.bluePlayer.playSound(soundEvent, 100F, 1F)
    }

    fun broadcastRedAddItemMessage(
        itemStack: ItemStackProxy
    ) {
        tradeContext.redPlayer.translationScope {
            val prefix = toTradeText("presenter_prefix")
            val addItemMessage = toTradeTranslation("add_item_oneself")
            val itemMessage = toTradeText("item")
                .format(itemStack.name().string(), itemStack.count())
                .showItem(itemStack)

            addItemMessage
                .append(itemMessage)

            addItemMessage.send(prefix)
        }
        tradeContext.bluePlayer.translationScope {
            val prefix = toTradeText("presenter_prefix")
            val addItemMessage = toTradeTranslation("add_item_other")
                .format0(tradeContext.redPlayer.playerName)
            val itemMessage = toTradeText("item")
                .format(itemStack.name().string(), itemStack.count())
                .showItem(itemStack)

            addItemMessage.append(itemMessage)

            addItemMessage.send(prefix)
        }
    }

    fun broadcastRedRemoveItemMessage(
        itemStack: ItemStackProxy
    ) {
        tradeContext.redPlayer.translationScope {
            val prefix = toTradeText("presenter_prefix")
            val removeItemMessage = toTradeTranslation("remove_item_oneself")

            removeItemMessage.send(prefix)
        }
        tradeContext.bluePlayer.translationScope {
            val prefix = toTradeText("presenter_prefix")
            val removeItemMessage = toTradeTranslation("remove_item_other")
                .format0(tradeContext.redPlayer.playerName)

            removeItemMessage.send(prefix)
        }
    }

    fun playAddItemSound() {
        val soundEvent = getSoundGetter().getAddItemSound()
        tradeContext.redPlayer.playSound(soundEvent, 100F, 1F)
        tradeContext.bluePlayer.playSound(soundEvent, 100F, 1F)
    }

    fun broadcastBlueAddItem(
        itemStack: ItemStackProxy
    ) {
        tradeContext.redPlayer.translationScope {
            val prefix = toTradeText("presenter_prefix")
            val addItemMessage = toTradeTranslation("add_item_other")
                .format0(tradeContext.bluePlayer.playerName)
            val itemMessage = toTradeText("item")
                .format(itemStack.name().string(), itemStack.count())
                .showItem(itemStack)

            addItemMessage.append(itemMessage)

            addItemMessage.send(prefix)
        }
        tradeContext.bluePlayer.translationScope {
            val prefix = toTradeText("presenter_prefix")
            val addItemMessage = toTradeTranslation("add_item_oneself")
            val itemMessage = toTradeText("item")
                .format(itemStack.name().string(), itemStack.count())
                .showItem(itemStack)

            addItemMessage.append(itemMessage)

            addItemMessage.send(prefix)
        }
    }

    fun broadcastBlueRemoveItemMessage(
        itemStack: ItemStackProxy
    ) {
        tradeContext.redPlayer.translationScope {
            val prefix = toTradeText("presenter_prefix")
            val removeItemMessage = toTradeTranslation("remove_item_other")
                .format0(tradeContext.bluePlayer.playerName)

            removeItemMessage.send(prefix)
        }
        tradeContext.bluePlayer.translationScope {
            val prefix = toTradeText("presenter_prefix")
            val removeItemMessage = toTradeTranslation("remove_item_oneself")

            removeItemMessage.send(prefix)
        }
    }

    fun playRemoveItemSound() {
        val soundEvent = getSoundGetter().getRemoveItemSound()
        tradeContext.redPlayer.playSound(soundEvent, 100F, 1F)
        tradeContext.bluePlayer.playSound(soundEvent, 100F, 1F)
    }

    fun playTerminateSound() {
        val soundEvent = getSoundGetter().getTerminateSound()
        tradeContext.redPlayer.playSound(soundEvent, 100F, 1F)
        tradeContext.bluePlayer.playSound(soundEvent, 100F, 1F)
    }

    fun playFinishSound() {
        val soundEvent = getSoundGetter().getFinishSound()
        tradeContext.redPlayer.playSound(soundEvent, 100F, 1F)
        tradeContext.bluePlayer.playSound(soundEvent, 100F, 1F)
    }

    fun broadcastFinishMessage() {
        broadcastTradeMessage("trade_end")
    }

    fun broadcastTerminateMessage() {
        broadcastTradeMessage("trade_terminate")
    }

    fun end() {
        val s2COperationPacketCommon = networkFunctions.createS2C(Operators.CLIENT_TRADE_END)
        broadcastOperation(s2COperationPacketCommon)
    }

    private fun broadcastTradeMessage(key: String, processor: (TranslationAgent) -> Unit = {}) {
        tradeContext.redPlayer.translationScope {
            val tradeTranslation = toTradeTranslation(key)
            processor(tradeTranslation)
            tradeTranslation.send()
        }
        tradeContext.bluePlayer.translationScope {
            val tradeTranslation = toTradeTranslation(key)
            processor(tradeTranslation)
            tradeTranslation.send()
        }
    }

    private fun broadcastOperation(
        packetAdapter: PacketAdapter
    ) {
        if (!tradeContext.redPlayer.isDisconnected()) {
            NetworkManager.sendToClient(
                NetworkManager.S2C_OPERATION,
                tradeContext.redPlayer,
                packetAdapter
            )
        }
        if (!tradeContext.bluePlayer.isDisconnected()) {
            NetworkManager.sendToClient(
                NetworkManager.S2C_OPERATION,
                tradeContext.bluePlayer,
                packetAdapter
            )
        }
    }

    private fun getSoundGetter(): CommonFunctions.SoundGetter {
        return CommonFunctionsPool.getFunctions(CommonFunctions.SoundGetter::class.java)
    }

    companion object {
        fun create(tradeContext: TradeContext): TradePresenter = TradePresenter(tradeContext)
    }

}