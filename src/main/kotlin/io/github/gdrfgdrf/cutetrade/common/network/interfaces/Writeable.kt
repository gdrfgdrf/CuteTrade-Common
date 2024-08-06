package io.github.gdrfgdrf.cutetrade.common.network.interfaces

import io.github.gdrfgdrf.cutetrade.common.proxy.PacketByteBufProxy

interface Writeable {
    fun write(packetByteBufProxy: PacketByteBufProxy)
}