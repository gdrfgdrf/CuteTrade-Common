package io.github.cutetrade.common.proxy

import io.github.cutetrade.common.translation.TranslationAgent
import io.github.cutetrade.common.enums.TranslationType
import io.github.cutetrade.common.trade.screen.handler.ScreenHandlerAgent
import io.github.cutetrade.common.translation.TranslationTextAgent

abstract class PlayerProxy(
    val playerName: String,
    val serverPlayerEntity: Any
) {
    fun send(message: TranslationAgent) {
        val prefix = getText(TranslationType.COMMAND, "prefix")
        send(prefix, message)
    }
    abstract fun getTranslation(translationType: TranslationType, key: String): TranslationAgent
    abstract fun getText(translationType: TranslationType, key: String): TranslationTextAgent

    abstract fun send(prefix: TranslationTextAgent, message: TranslationAgent)

    abstract fun offerOrDrop(itemStack: ItemStackProxy)
    abstract fun openHandledScreen(factory: Any)
    abstract fun closeHandledScreen()
    abstract fun playSound(any: Any, volume: Float, pitch: Float)
    abstract fun currentScreenHandler(): ScreenHandlerAgent
    abstract fun isDisconnected(): Boolean
}