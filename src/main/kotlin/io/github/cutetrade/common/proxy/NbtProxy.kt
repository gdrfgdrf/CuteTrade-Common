package io.github.cutetrade.common.proxy

abstract class NbtProxy(
    val nbt: Any
) {
    abstract fun asString(): String

}