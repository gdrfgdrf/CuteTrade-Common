package io.github.cutetrade.common.extension

import io.github.cutetrade.common.proxy.PlayerProxy
import io.github.cutetrade.common.translation.TranslationScopeAgent

fun <R> PlayerProxy.translationScope(block: TranslationScopeAgent.() -> R): R {
    val translationScopeAgent = TranslationScopeAgent(this)
    translationScopeAgent.apply {
        val result = block()
        return result
    }
}