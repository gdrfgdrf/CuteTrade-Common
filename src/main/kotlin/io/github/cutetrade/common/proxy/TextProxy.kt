package io.github.cutetrade.common.proxy

abstract class TextProxy(
    val text: Any
) {
    abstract fun string(): String
}