package io.github.gdrfgdrf.cutetrade.common.network.interfaces

import io.github.gdrfgdrf.cutetrade.common.proxy.ItemStackProxy

interface PacketAdapter : Writeable {
    fun getOperatorName_(): String

    fun getStringArgs_(): Array<String?>?
    fun setStringArgs_(args: Array<String?>?)

    fun getIntArgs_(): Array<Int?>?
    fun setIntArgs_(args: Array<Int?>?)

    fun getItemStackArgs_(): Array<ItemStackProxy?>?
    fun setItemStackArgs_(args: Array<ItemStackProxy?>?)
}