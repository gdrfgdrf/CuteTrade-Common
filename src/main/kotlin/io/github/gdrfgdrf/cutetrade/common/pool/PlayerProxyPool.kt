package io.github.gdrfgdrf.cutetrade.common.pool

import io.github.gdrfgdrf.cutetrade.common.proxy.PlayerProxy
import java.util.concurrent.ConcurrentHashMap

object PlayerProxyPool {
    private val PLAYER_MAP = ConcurrentHashMap<String, PlayerProxy>()

    fun getPlayerProxy(playerName: String): PlayerProxy? {
        return PLAYER_MAP[playerName]
    }

    fun contains(playerName: String): Boolean = PLAYER_MAP.containsKey(playerName)

    fun addPlayerProxy(playerProxy: PlayerProxy) {
        PLAYER_MAP[playerProxy.playerName] = playerProxy
    }

    fun removePlayerProxy(playerName: String) {
        PLAYER_MAP.remove(playerName)
    }
}