package io.github.cutetrade.common.proxy

abstract class SlotProxy(
    val slot: Any
) {
    abstract fun hasStack(): Boolean
    abstract fun stack(): ItemStackProxy
}