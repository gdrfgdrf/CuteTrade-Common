package io.github.gdrfgdrf.cutetrade.common.operation.server

import io.github.gdrfgdrf.cutetrade.common.extension.currentTrade
import io.github.gdrfgdrf.cutetrade.common.extension.isRed
import io.github.gdrfgdrf.cutetrade.common.extension.translationScope
import io.github.gdrfgdrf.cutetrade.common.network.PacketContext
import io.github.gdrfgdrf.cutetrade.common.operation.base.Operator
import io.github.gdrfgdrf.cutetrade.common.proxy.PlayerProxy

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