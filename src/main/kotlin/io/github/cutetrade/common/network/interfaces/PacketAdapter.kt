package io.github.cutetrade.common.network.interfaces

import io.github.cutetrade.common.proxy.ItemStackProxy

interface PacketAdapter : Writeable {
    fun getOperatorName(): String

    fun getStringArgs(): Array<String?>?
    fun setStringArgs(args: Array<String?>?)

    fun getIntArgs(): Array<Int?>?
    fun setIntArgs(args: Array<Int?>?)

    fun getItemStackArgs(): Array<ItemStackProxy?>?
    fun setItemStackArgs(args: Array<ItemStackProxy?>?)
}