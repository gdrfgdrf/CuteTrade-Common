package io.github.gdrfgdrf.cutetrade.common.command

import io.github.gdrfgdrf.cutetrade.common.extension.findProtobufPlayer
import io.github.gdrfgdrf.cutetrade.common.extension.printInformation
import io.github.gdrfgdrf.cutetrade.common.extension.toItemStack
import io.github.gdrfgdrf.cutetrade.common.extension.translationScope
import io.github.gdrfgdrf.cutetrade.common.manager.TradeManager
import io.github.gdrfgdrf.cutetrade.common.page.Pageable
import io.github.gdrfgdrf.cutetrade.common.proxy.PlayerProxy

object HistoryCommandExecutor {
    fun execute(playerProxy: PlayerProxy, playerName: String) {
        playerProxy.translationScope {
            val protobufPlayer = playerName.findProtobufPlayer()
            if (protobufPlayer == null) {
                toCommandTranslation("not_found_player")
                    .format0(playerName)
                    .send()
                return@translationScope
            }
            val tradeIdsList = protobufPlayer.tradeIdsList
            if (tradeIdsList == null || (tradeIdsList as List<String>).isEmpty()) {
                if (protobufPlayer.name != playerProxy.playerName) {
                    toCommandTranslation("no_transaction_history_other")
                        .format0(playerName)
                        .send()
                    return@translationScope
                }

                toCommandTranslation("no_transaction_history")
                    .send()
                return@translationScope
            }

            val pageable = Pageable()
            pageable.openScreen(toScreenTranslation("history_title")
                .format0(playerName)
                .build(), playerProxy)

            val tradeMap = TradeManager.tradeProtobuf?.message?.tradeIdToTradeMap
            tradeIdsList.forEach { tradeId ->
                val trade = tradeMap?.get(tradeId) ?: return@forEach
                val itemStack = trade.toItemStack(playerProxy)

                pageable.addItemStack(itemStack)
            }
            pageable.fullNavigationBar(playerProxy)

            pageable.navigator?.show(0)

            pageable.setOnItemClick { index ->
                if (index >= tradeIdsList.size || index < 0) {
                    return@setOnItemClick
                }
                val tradeId = tradeIdsList[index]
                val trade = tradeMap!![tradeId] ?: return@setOnItemClick

                trade.printInformation(playerProxy)
            }
        }
    }
}