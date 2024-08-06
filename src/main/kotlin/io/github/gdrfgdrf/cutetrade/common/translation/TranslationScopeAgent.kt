package io.github.gdrfgdrf.cutetrade.common.translation

import io.github.gdrfgdrf.cutetrade.common.enums.TranslationType
import io.github.gdrfgdrf.cutetrade.common.proxy.PlayerProxy

class TranslationScopeAgent(
    private val playerProxy: PlayerProxy,
) {
    fun toCommandTranslation(key: String): TranslationAgent =
        playerProxy.getTranslation(TranslationType.COMMAND, key)
    fun toCommandText(key: String): TranslationTextAgent =
        playerProxy.getText(TranslationType.COMMAND, key)

    fun toScreenTranslation(key: String): TranslationAgent =
        playerProxy.getTranslation(TranslationType.SCREEN, key)
    fun toScreenText(key: String): TranslationTextAgent =
        playerProxy.getText(TranslationType.SCREEN, key)

    fun toTradeTranslation(key: String): TranslationAgent =
        playerProxy.getTranslation(TranslationType.TRADE, key)
    fun toTradeText(key: String): TranslationTextAgent =
        playerProxy.getText(TranslationType.TRADE, key)

    fun toInformationTranslation(key: String): TranslationAgent =
        playerProxy.getTranslation(TranslationType.INFORMATION, key)
    fun toInformationText(key: String): TranslationTextAgent =
        playerProxy.getText(TranslationType.INFORMATION, key)

}