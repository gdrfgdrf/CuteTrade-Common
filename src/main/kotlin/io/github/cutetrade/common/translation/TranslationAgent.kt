package io.github.cutetrade.common.translation

import io.github.cutetrade.common.CommonProxySet
import io.github.cutetrade.common.pool.CommonProxyPool
import io.github.cutetrade.common.proxy.PlayerProxy

class TranslationAgent(
    val cuteTranslation: Any,
    private val playerProxy: PlayerProxy
) {
    fun append(translationTextAgent: TranslationTextAgent): TranslationAgent {
        val functions =
            CommonProxyPool.getProxy<CommonProxySet.TranslationFunctions>(CommonProxySet.TranslationFunctions::class.java)
        return functions.append(this, translationTextAgent)
    }

    fun format0(vararg any: Any): TranslationAgent {
        val functions =
            CommonProxyPool.getProxy<CommonProxySet.TranslationFunctions>(CommonProxySet.TranslationFunctions::class.java)
        return functions.format(this, 0, *any)
    }

    fun send(customPrefix: String) {
        val prefix = TranslationTextAgent.of(customPrefix)
        playerProxy.send(prefix, this)
    }

    fun send(customPrefix: TranslationTextAgent? = null) {
        if (customPrefix != null) {
            playerProxy.send(customPrefix, this)
            return
        }
        playerProxy.send(this)
    }
}