package io.github.cutetrade.common.command

import io.github.cutetrade.common.extension.isOnline
import io.github.cutetrade.common.extension.isTrading
import io.github.cutetrade.common.extension.translationScope
import io.github.cutetrade.common.manager.ProtobufPlayerManager
import io.github.cutetrade.common.manager.TradeRequestManager
import io.github.cutetrade.common.pool.PlayerProxyPool
import io.github.cutetrade.common.proxy.PlayerProxy

object RequestTradeCommandExecutor {
    fun execute(playerProxy: PlayerProxy, providedBlueName: String) {
        playerProxy.translationScope {
            if (providedBlueName == playerProxy.playerName) {
                toCommandTranslation("trade_with_oneself")
                    .send()
                return@translationScope
            }

            val blueProtobufPlayer = ProtobufPlayerManager.findPlayer(providedBlueName)
            if (blueProtobufPlayer == null) {
                toCommandTranslation("not_found_player")
                    .format0(providedBlueName)
                    .send()
                return@translationScope
            }

            if (!blueProtobufPlayer.isOnline()) {
                toCommandTranslation("player_offline")
                    .format0(providedBlueName)
                    .send()
                return@translationScope
            }

            val bluePlayerProxy = PlayerProxyPool.getPlayerProxy(providedBlueName)
            if (bluePlayerProxy!!.isTrading()) {
                toCommandTranslation("player_is_trading_oneself")
                    .format0(providedBlueName)
                    .send()
                return@translationScope
            }

            TradeRequestManager.request(playerProxy, bluePlayerProxy)
        }
    }
}