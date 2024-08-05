package io.github.cutetrade.common.command

import io.github.cutetrade.common.extension.getTradeRequest
import io.github.cutetrade.common.extension.isOnline
import io.github.cutetrade.common.extension.isTrading
import io.github.cutetrade.common.extension.translationScope
import io.github.cutetrade.common.manager.ProtobufPlayerManager
import io.github.cutetrade.common.pool.PlayerProxyPool
import io.github.cutetrade.common.proxy.PlayerProxy

object AcceptTradeResultCommandExecutor {
    fun execute(playerProxy: PlayerProxy, providedRedName: String) {
        playerProxy.translationScope {
            if (providedRedName == playerProxy.playerName) {
                toCommandTranslation("accept_request_from_oneself")
                    .send()
                return@translationScope
            }

            val redProtobufPlayer = ProtobufPlayerManager.findPlayer(providedRedName)
            if (redProtobufPlayer == null) {
                toCommandTranslation("not_found_player")
                    .format0(providedRedName)
                    .send()
                return@translationScope
            }

            if (!redProtobufPlayer.isOnline()) {
                toCommandTranslation("player_offline")
                    .format0(providedRedName)
                    .send()
                return@translationScope
            }

            val redPlayer = PlayerProxyPool.getPlayerProxy(providedRedName)
            val tradeRequest = playerProxy.getTradeRequest(redPlayer!!)
            if (tradeRequest == null) {
                toCommandTranslation("not_found_request")
                    .format0(providedRedName)
                    .send()
                return@translationScope
            }

            if (redPlayer.isTrading()) {
                toCommandTranslation("player_is_trading")
                    .format0(providedRedName)
                    .send()
                return@translationScope
            }

            tradeRequest.accept()
        }
    }
}