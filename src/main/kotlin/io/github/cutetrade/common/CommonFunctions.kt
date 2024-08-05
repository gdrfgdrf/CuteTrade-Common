package io.github.cutetrade.common

import io.github.cutetrade.common.log.LogType
import io.github.cutetrade.common.proxy.*
import io.github.cutetrade.common.trade.screen.handler.ScreenHandlerAgent
import io.github.cutetrade.common.translation.TranslationAgent
import io.github.cutetrade.common.translation.TranslationTextAgent

object CommonFunctions {
    interface ProxyFactory {
        fun createItemStackProxyFromNbt(nbt: Nbt): ItemStackProxy

        fun newNbt(): Nbt
        fun parseNbt(string: String): Nbt

        fun createTextProxy(string: String): TextProxy
    }

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

    interface ItemStackFunctions {
        fun create(itemProxy: ItemProxy): ItemStackProxy
        fun getEmptyItemStack(): ItemStackProxy
    }

    interface ItemFunctions {
        fun get(fieldName: String): ItemProxy
    }

    interface TranslationTextFunctions {
        fun createShowText(value: String): Any
        fun createShowItem(itemStack: ItemStackProxy): Any
        fun createShowEntity(entity: Any): Any
        fun build(translationTextAgent: TranslationTextAgent): TextProxy
    }

    interface TranslationFunctions {
        fun get(translationAgent: TranslationAgent, index: Int): TranslationTextAgent
        fun append(translationAgent: TranslationAgent, translationTextAgent: TranslationTextAgent): TranslationAgent
        fun format(translationAgent: TranslationAgent, index: Int, vararg any: Any): TranslationAgent
        fun build(translationAgent: TranslationAgent): TextProxy
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

    interface PageableScreenHandlerFactoryGetter {
        fun create(title: TextProxy): Any
    }

    interface ScreenHandlerFunctions {
        fun getInventory(
            screenHandlerAgent: ScreenHandlerAgent
        ): Any
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

    interface PageableScreenHandlerFunctions {
        fun setOnItemClick(onItemClick: (Int) -> Unit)
    }

    interface InventoryFunctions {
        fun setStack(inventory: Any, index: Int, stack: ItemStackProxy)
    }
}