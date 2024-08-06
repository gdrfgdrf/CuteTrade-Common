package io.github.gdrfgdrf.cutetrade.common.extension

import cutetrade.protobuf.CommonProto
import cutetrade.protobuf.CommonProto.TradeItem
import io.github.gdrfgdrf.cutetrade.common.CommonFunctions
import io.github.gdrfgdrf.cutetrade.common.pool.CommonFunctionsPool
import io.github.gdrfgdrf.cutetrade.common.proxy.ItemStackProxy
import io.github.gdrfgdrf.cutetrade.common.proxy.PlayerProxy
import java.util.*

fun generateTradeId(): String {
    var randomId = UUID.randomUUID().toString()
        .lowercase()
        .replace("-", "")
    randomId += Math.random().toString()
        .replace(".", "")
    return randomId
}

fun TradeItem.toItemStack(): ItemStackProxy {
    if ("{}" == this.nbt) {
        return ItemStackProxy.EMPTY!!
    }

    val factory = getProxyFactory()
    val nbt = factory.parseNbt(this.nbt)
    return factory.createItemStackProxyFromNbt(nbt)
}

fun CommonProto.Trade.toItemStack(playerProxy: PlayerProxy): ItemStackProxy {
    val itemFunctions = CommonFunctionsPool.getFunctions<CommonFunctions.ItemFunctions>(CommonFunctions.ItemFunctions::class.java)
    val itemStackFunctions = CommonFunctionsPool.getFunctions<CommonFunctions.ItemStackFunctions>(CommonFunctions.ItemStackFunctions::class.java)

    val item = if (this.tradeResult == CommonProto.TradeResult.TRADE_RESULT_FINISHED) {
        itemFunctions.get("GOLD_BLOCK")
    } else {
        itemFunctions.get("REDSTONE_BLOCK")
    }

    return playerProxy.translationScope {
        val itemStack = itemStackFunctions.create(item)
        itemStack.setCustomName(getProxyFactory().createTextProxy(startTime.toInstant().formattedDate() +
                " | " +
                toScreenTranslation("click_view").build().string()))

        itemStack
    }
}

fun CommonProto.Trade.printInformation(playerProxy: PlayerProxy) {
    playerProxy.translationScope {
        toCommandTranslation("top")
            .send("")

        val finishedMessage = toTradeTranslation("finished_result")
        val terminatedMessage = toTradeTranslation("terminated_result")
        val resultMessage = if (tradeResult == CommonProto.TradeResult.TRADE_RESULT_FINISHED) {
            finishedMessage
        } else {
            terminatedMessage
        }

        toInformationTranslation("red_player_is")
            .format0(redName)
            .send("")
        toInformationTranslation("blue_player_is")
            .format0(blueName)
            .send("")
        toInformationTranslation("trade_result")
            .format0(resultMessage.build().string())
            .send("")

        val divider = toInformationTranslation("divider")

        if (tradeResult == CommonProto.TradeResult.TRADE_RESULT_FINISHED) {
            val nothing = toInformationTranslation("nothing")

            divider.send("")

            toInformationTranslation("red_player_final_trade_item")
                .format0(redName)
                .send("")

            val redItemResultList = redItemResultList
            var allEmpty = redItemResultList.stream().filter {
                it.nbt != "{}"
            }.findAny()
                .orElse(null)

            if (redItemResultList == null || redItemResultList.isEmpty() || allEmpty == null) {
                nothing.send("")
            } else {
                redItemResultList.forEach { tradeItem ->
                    val itemStack = tradeItem.toItemStack()
                    val message = toInformationTranslation("final_trade_item")

                    message.get0()
                        .showItem(itemStack)

                    message.format0(itemStack.name().string(), itemStack.count())
                        .send("")
                }
            }

            divider.send("")

            toInformationTranslation("blue_player_final_trade_item")
                .format0(blueName)
                .send("")

            val blueItemResultList = blueItemResultList

            allEmpty = blueItemResultList.stream().filter {
                it.nbt != "{}"
            }.findAny()
                .orElse(null)
            if (blueItemResultList == null || blueItemResultList.isEmpty() || allEmpty == null) {
                nothing.send("")
            } else {
                blueItemResultList.forEach { tradeItem ->
                    val itemStack = tradeItem.toItemStack()
                    val message = toInformationTranslation("final_trade_item")

                    message.get0()
                        .showItem(itemStack)

                    message.format0(itemStack.name().string(), itemStack.count())
                        .send("")
                }
            }

            divider.send("")

            toInformationTranslation("trade_id")
                .format0(id)
                .send("")
        }

        toCommandTranslation("bottom")
            .send("")
    }
}