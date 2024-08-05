package io.github.cutetrade.common.operation.server

import io.github.cutetrade.common.extension.currentTrade
import io.github.cutetrade.common.extension.isRed
import io.github.cutetrade.common.extension.translationScope
import io.github.cutetrade.common.network.PacketContext
import io.github.cutetrade.common.operation.base.Operator
import io.github.cutetrade.common.proxy.PlayerProxy

object ClientInitializedOperator : Operator {
    override fun run(context: PacketContext<*>, args: Array<*>?) {
        val sender = context.sender as PlayerProxy

        val currentTrade = sender.currentTrade()
        if (currentTrade == null) {
            sender.translationScope {
                toCommandTranslation("no_transaction_in_progress")
                    .send()
            }
            return
        }

        if (sender.isRed()) {
            currentTrade.redPlayerInitialized()
        } else {
            currentTrade.bluePlayerInitialized()
        }
    }

    override fun getName(): String = Operators.SERVER_CLIENT_INITIALIZED
}