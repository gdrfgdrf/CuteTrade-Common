package io.github.cutetrade.common.enums

enum class TranslationType(
    val rootKey: String
) {
    COMMAND("command.cutetrade."),
    SCREEN("screen.cutetrade."),
    TRADE("trade.cutetrade."),
    INFORMATION("information.cutetrade.");
}