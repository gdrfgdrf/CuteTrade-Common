package io.github.cutetrade.common.proxy

import cutetrade.protobuf.CommonProto.TradeItem
import io.github.cutetrade.common.CommonFunctions
import io.github.cutetrade.common.extension.getProxyFactory
import io.github.cutetrade.common.pool.CommonFunctionsPool

abstract class ItemStackProxy(
    val itemStack: Any
) {
    abstract fun name(): String
    abstract fun count(): Int

    abstract fun isEmpty(): Boolean
    abstract fun copy(): ItemStackProxy

    abstract fun setCustomName(textProxy: TextProxy)

    abstract fun writeNbt(nbtProxy: NbtProxy)
    fun toProtobufTradeItem(addByName: String): TradeItem {
        val nbt = getProxyFactory().newNbt()
        this.writeNbt(nbt)

        return TradeItem.newBuilder()
            .setNbt(nbt.asString())
            .setAddByName(addByName)
            .build()
    }

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