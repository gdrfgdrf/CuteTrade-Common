package io.github.cutetrade.common.proxy

import cutetrade.protobuf.CommonProto.TradeItem
import io.github.cutetrade.common.CommonFunctions
import io.github.cutetrade.common.pool.CommonFunctionsPool

abstract class ItemStackProxy(
    val itemStack: Any
) {
    abstract fun name(): String
    abstract fun count(): Int

    abstract fun isEmpty(): Boolean
    abstract fun copy(): ItemStackProxy

    abstract fun setCustomName(textProxy: TextProxy)

    abstract fun toProtobufTradeItem(addByName: String): TradeItem

    companion object {
        var EMPTY: ItemStackProxy? = null
            get() {
                if (field == null) {
                    val functions =
                        CommonFunctionsPool.getProxy<CommonFunctions.ItemStackFunctions>(CommonFunctions.ItemStackFunctions::class.java)
                    field = functions.getEmptyItemStack()
                }
                return field!!
            }
    }
}