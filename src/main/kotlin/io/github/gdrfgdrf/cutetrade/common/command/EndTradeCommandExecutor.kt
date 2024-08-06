package io.github.gdrfgdrf.cutetrade.common.command

import io.github.gdrfgdrf.cutetrade.common.extension.currentTrade
import io.github.gdrfgdrf.cutetrade.common.extension.translationScope
import io.github.gdrfgdrf.cutetrade.common.proxy.PlayerProxy

object EndTradeCommandExecutor {
    fun execute(playerProxy: PlayerProxy) {
        playerProxy.translationScope {
            val currentTrade = playerProxy.currentTrade()
            if (currentTrade == null) {
                toCommandTranslation("no_transaction_in_progress")
                    .send()
                return@translationScope
            }

            currentTrade.terminate()
        }
    }
}