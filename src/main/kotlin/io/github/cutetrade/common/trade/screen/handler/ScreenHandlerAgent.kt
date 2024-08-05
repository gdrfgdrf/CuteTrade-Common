package io.github.cutetrade.common.trade.screen.handler

import io.github.cutetrade.common.CommonFunctions
import io.github.cutetrade.common.pool.CommonFunctionsPool
import io.github.cutetrade.common.proxy.ItemStackProxy
import io.github.cutetrade.common.proxy.SlotProxy

class ScreenHandlerAgent(
    val raw: Any
) {
    private val functions = CommonFunctionsPool.getProxy<CommonFunctions.ScreenHandlerFunctions>(CommonFunctions.ScreenHandlerFunctions::class.java)

    fun getInventory(): Any {
        return functions.getInventory(this)
    }

    fun setStackInSlot(slot: Int, revision: Int, stack: ItemStackProxy) {
        functions.setStackInSlot(this, slot, revision, stack)
    }

    fun getSlot(slotId: Int): SlotProxy {
        return functions.getSlot(this, slotId)
    }

    fun addListener(listener: (ScreenHandlerAgent, Int, ItemStackProxy) -> Unit) {
        functions.addListener(this, listener)
    }

    fun nextRevision(): Int {
        return functions.nextRevision(this)
    }

}