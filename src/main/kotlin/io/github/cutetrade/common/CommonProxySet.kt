package io.github.cutetrade.common

import io.github.cutetrade.common.log.LogType
import io.github.cutetrade.common.proxy.ItemStackProxy
import io.github.cutetrade.common.proxy.PacketByteBufProxy
import io.github.cutetrade.common.proxy.PlayerProxy
import io.github.cutetrade.common.proxy.SlotProxy
import io.github.cutetrade.common.trade.screen.handler.ScreenHandlerAgent
import io.github.cutetrade.common.translation.TranslationAgent
import io.github.cutetrade.common.translation.TranslationTextAgent

object CommonProxySet {
    interface MainFunctions {
        fun createIdentifier(namespace: String, path: String): Any
    }

    interface NetworkFunctions {
        fun sendToServer(writer: (PacketByteBufProxy) -> Unit)
        fun sendToClient(identifier: Any, playerProxy: PlayerProxy, writer: (PacketByteBufProxy) -> Unit)
    }

    interface LoggerFunctions {
        fun log(logType: LogType, message: String)
    }

    interface TranslationTextCreateHoverActionValueFunction {
        fun createShowText(value: String): Any
        fun createShowItem(itemStack: ItemStackProxy): Any
        fun createShowEntity(entity: Any): Any
    }

    interface TranslationTextBuildFunction {
        fun build(translationTextAgent: TranslationTextAgent): Any
    }

    interface TranslationFunctions {
        fun append(translationAgent: TranslationAgent, translationTextAgent: TranslationTextAgent): TranslationAgent
        fun format(translationAgent: TranslationAgent, index: Int, vararg any: Any): TranslationAgent
    }

    interface SoundGetter {
        fun getTradeStartSound(): Any
        fun getStatePositiveSound(): Any
        fun getStateNegativeSound(): Any
        fun getAddItemSound(): Any
        fun getRemoveItemSound(): Any
        fun getTerminateSound(): Any
        fun getFinishSound(): Any
    }

    interface TradeScreenHandlerFactoryGetter {
        fun create(title: String): Any
    }

    interface ScreenHandlerFunctions {
        fun setStackInSlot(
            screenHandlerAgent: ScreenHandlerAgent,
            slot: Int,
            revision: Int,
            stack: ItemStackProxy
        )
        fun getSlot(
            screenHandlerAgent: ScreenHandlerAgent,
            slotId: Int,
        ): SlotProxy
        fun addListener(
            screenHandlerAgent: ScreenHandlerAgent,
            listener: (ScreenHandlerAgent, Int, ItemStackProxy) -> Unit,
        )
        fun nextRevision(screenHandlerAgent: ScreenHandlerAgent): Int
    }
}