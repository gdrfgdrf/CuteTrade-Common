package io.github.gdrfgdrf.cutetrade.common.command

import io.github.gdrfgdrf.cutetrade.common.extension.getTradeRequest
import io.github.gdrfgdrf.cutetrade.common.extension.isOnline
import io.github.gdrfgdrf.cutetrade.common.extension.translationScope
import io.github.gdrfgdrf.cutetrade.common.manager.ProtobufPlayerManager
import io.github.gdrfgdrf.cutetrade.common.pool.PlayerProxyPool
import io.github.gdrfgdrf.cutetrade.common.proxy.PlayerProxy

object DeclineTradeResultCommandExecutor {
    fun execute(playerProxy: PlayerProxy, providedRedName: String) {
        playerProxy.translationScope {
            if (providedRedName == playerProxy.playerName) {
                toCommandTranslation("decline_request_from_oneself")
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
                    .format0()
                    .send()
                return@translationScope
            }

            tradeRequest.decline()
        }
    }
}