package io.github.cutetrade.common.proxy

abstract class TextProxy(
    var text: Any
) {
    abstract fun string(): String
}