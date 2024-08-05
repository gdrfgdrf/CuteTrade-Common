package io.github.cutetrade.common.translation

import io.github.cutetrade.common.CommonFunctions
import io.github.cutetrade.common.pool.CommonFunctionsPool
import io.github.cutetrade.common.proxy.PlayerProxy
import io.github.cutetrade.common.proxy.TextProxy

class TranslationAgent(
    val cuteTranslation: Any,
    private val playerProxy: PlayerProxy
) {
    fun get0(): TranslationTextAgent {
        return functions!!.get(this, 0)
    }

    fun append(translationTextAgent: TranslationTextAgent): TranslationAgent {
        return functions!!.append(this, translationTextAgent)
    }

    fun format0(vararg any: Any): TranslationAgent {
        return functions!!.format(this, 0, *any)
    }

    fun build(): TextProxy {
        return functions!!.build(this)
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

    companion object {
        var functions: CommonFunctions.TranslationFunctions? = null
            get() {
                if (field == null) {
                    field = CommonFunctionsPool.getProxy<CommonFunctions.TranslationFunctions>(CommonFunctions.TranslationFunctions::class.java)
                }
                return field
            }
    }
}