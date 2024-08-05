package io.github.cutetrade.common.proxy

abstract class Nbt(
    val nbt: Any
) {
    abstract fun writeNbt()

}