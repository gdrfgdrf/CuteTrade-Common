package io.github.gdrfgdrf.cutetrade.common.proxy

abstract class PacketByteBufProxy(
    var packetByteBuf: Any
) {
    abstract fun readString(): String
    abstract fun readInt(): Int
    abstract fun readItemStack(): ItemStackProxy

    abstract fun writeString(str: String)
    abstract fun writeInt(i: Int)
    abstract fun writeItemStack(itemStackProxy: ItemStackProxy)
    abstract fun writeNbt(nbtProxy: NbtProxy)
}