package io.github.cutetrade.common.proxy

import cutetrade.protobuf.CommonProto.TradeItem
import io.github.cutetrade.common.trade.TradeItemStack

abstract class ItemStackProxy(
    val itemStack: Any
) {
    abstract fun name(): String
    abstract fun count(): Int

    abstract fun isEmpty(): Boolean
    abstract fun copy(): ItemStackProxy
    abstract fun toProtobufTradeItem(addByName: String): TradeItem
}