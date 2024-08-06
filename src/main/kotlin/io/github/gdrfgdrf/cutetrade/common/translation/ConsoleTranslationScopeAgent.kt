package io.github.gdrfgdrf.cutetrade.common.translation

import io.github.gdrfgdrf.cutetrade.common.CommonFunctions
import io.github.gdrfgdrf.cutetrade.common.enums.TranslationType
import io.github.gdrfgdrf.cutetrade.common.pool.CommonFunctionsPool

object ConsoleTranslationScopeAgent {
    var FUNCTIONS: CommonFunctions.ConsoleTranslationScopeFunctions? = null
        get() {
            if (field == null) {
                FUNCTIONS = CommonFunctionsPool.getFunctions(CommonFunctions.ConsoleTranslationScopeFunctions::class.java)
            }
            return field
        }

    fun toCommandTranslation(key: String): TranslationAgent =
        FUNCTIONS!!.getTranslation(TranslationType.COMMAND, key)
    fun toCommandText(key: String): TranslationTextAgent =
        FUNCTIONS!!.getText(TranslationType.COMMAND, key)

    fun toScreenTranslation(key: String): TranslationAgent =
        FUNCTIONS!!.getTranslation(TranslationType.SCREEN, key)
    fun toScreenText(key: String): TranslationTextAgent =
        FUNCTIONS!!.getText(TranslationType.SCREEN, key)

    fun toTradeTranslation(key: String): TranslationAgent =
        FUNCTIONS!!.getTranslation(TranslationType.TRADE, key)
    fun toTradeText(key: String): TranslationTextAgent =
        FUNCTIONS!!.getText(TranslationType.TRADE, key)

    fun toInformationTranslation(key: String): TranslationAgent =
        FUNCTIONS!!.getTranslation(TranslationType.INFORMATION, key)
    fun toInformationText(key: String): TranslationTextAgent =
        FUNCTIONS!!.getText(TranslationType.INFORMATION, key)


}