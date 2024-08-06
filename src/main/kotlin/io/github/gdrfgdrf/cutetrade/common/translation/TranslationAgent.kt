package io.github.gdrfgdrf.cutetrade.common.translation

import io.github.gdrfgdrf.cutetrade.common.CommonFunctions
import io.github.gdrfgdrf.cutetrade.common.pool.CommonFunctionsPool
import io.github.gdrfgdrf.cutetrade.common.proxy.PlayerProxy
import io.github.gdrfgdrf.cutetrade.common.proxy.TextProxy

class TranslationAgent(
    val cuteTranslation: Any,
    private val playerProxy: PlayerProxy?
) {
    fun get0(): TranslationTextAgent {
        return functions!!.get(this, 0)
    }

    fun append(translationTextAgent: TranslationTextAgent): TranslationAgent {
        return functions!!.append(this, translationTextAgent)
    }

    fun insert(index: Int, translationTextAgent: TranslationTextAgent): TranslationAgent {
        return functions!!.insert(this, index, translationTextAgent)
    }

    fun format0(vararg any: Any): TranslationAgent {
        return functions!!.format(this, 0, *any)
    }

    fun build(): TextProxy {
        return functions!!.build(this)
    }

    fun send(customPrefix: String) {
        if (playerProxy == null) {
            val consoleFunctions = CommonFunctionsPool.getFunctions<CommonFunctions.ConsoleTranslationScopeFunctions>(
                CommonFunctions.ConsoleTranslationScopeFunctions::class.java
            )
            consoleFunctions.send(this)
            return
        }
        val prefix = TranslationTextAgent.of(customPrefix)
        playerProxy.send(prefix, this)
    }

    fun send(customPrefix: TranslationTextAgent? = null) {
        if (playerProxy == null) {
            val consoleFunctions = CommonFunctionsPool.getFunctions<CommonFunctions.ConsoleTranslationScopeFunctions>(
                CommonFunctions.ConsoleTranslationScopeFunctions::class.java
            )
            consoleFunctions.send(this)
            return
        }
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
                    field = CommonFunctionsPool.getFunctions<CommonFunctions.TranslationFunctions>(CommonFunctions.TranslationFunctions::class.java)
                }
                return field
            }
    }
}