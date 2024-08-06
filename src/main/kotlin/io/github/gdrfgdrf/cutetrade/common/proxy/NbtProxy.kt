package io.github.gdrfgdrf.cutetrade.common.proxy

abstract class NbtProxy(
    var nbt: Any
) {
    abstract fun asString(): String

}