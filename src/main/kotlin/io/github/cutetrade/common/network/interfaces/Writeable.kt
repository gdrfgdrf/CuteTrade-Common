package io.github.cutetrade.common.network.interfaces

import io.github.cutetrade.common.proxy.PacketByteBufProxy

interface Writeable {
    fun write(packetByteBufProxy: PacketByteBufProxy)
}