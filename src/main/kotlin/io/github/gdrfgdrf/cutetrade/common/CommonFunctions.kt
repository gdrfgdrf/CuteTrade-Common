package io.github.gdrfgdrf.cutetrade.common

import io.github.gdrfgdrf.cutetrade.common.log.LogType
import io.github.gdrfgdrf.cutetrade.common.network.interfaces.PacketAdapter
import io.github.gdrfgdrf.cutetrade.common.network.interfaces.Writeable
import io.github.gdrfgdrf.cutetrade.common.proxy.*
import io.github.gdrfgdrf.cutetrade.common.trade.screen.handler.ScreenHandlerAgent
import io.github.gdrfgdrf.cutetrade.common.translation.ClickTextAction
import io.github.gdrfgdrf.cutetrade.common.translation.HoverTextAction
import io.github.gdrfgdrf.cutetrade.common.translation.TranslationAgent
import io.github.gdrfgdrf.cutetrade.common.translation.TranslationTextAgent

object CommonFunctions {
    interface ProxyFactory {
        fun createItemStackProxyFromNbt(nbtProxy: NbtProxy): ItemStackProxy

        fun newNbt(): NbtProxy
        fun parseNbt(string: String): NbtProxy

        fun createTextProxy(string: String): TextProxy
        fun createPacketByteBufProxy(packetByteBuf: Any): PacketByteBufProxy
    }

    interface MainFunctions {
        fun createIdentifier(namespace: String, path: String): Any
    }

    interface NetworkFunctions {
        fun createS2C(operatorName: String): PacketAdapter
        fun createC2S(operatorName: String): PacketAdapter
        fun sendToServer(writeable: PacketAdapter)
        fun sendToClient(identifier: Any, playerProxy: PlayerProxy, writeable: PacketAdapter)
    }

    interface LoggerFunctions {
        fun log(logType: LogType, message: String, vararg args: Any)
        fun error(message: String, throwable: Throwable)
    }

    interface ItemStackFunctions {
        fun create(itemProxy: ItemProxy): ItemStackProxy
        fun getEmptyItemStack(): ItemStackProxy
    }

    interface ItemFunctions {
        fun get(fieldName: String): ItemProxy
    }

    interface TranslationTextFunctions {
        fun create(value: String): TranslationTextAgent

        fun clickAction(translationTextAgent: TranslationTextAgent, clickTextAction: ClickTextAction, any: Any)
        fun hoverAction(translationTextAgent: TranslationTextAgent, hoverTextAction: HoverTextAction, any: Any)
        fun format(translationTextAgent: TranslationTextAgent, vararg any: Any)

        fun createShowText(value: String): TextProxy
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
        ): Any?
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
        fun setOnItemClick(screenHandlerAgent: ScreenHandlerAgent, onItemClick: (Int) -> Unit)
    }

    interface InventoryFunctions {
        fun setStack(inventory: Any, index: Int, stack: ItemStackProxy)
    }
}