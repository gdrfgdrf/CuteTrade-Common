package io.github.cutetrade.common.extension

import java.util.*

fun generateTradeId(): String {
    var randomId = UUID.randomUUID().toString()
        .lowercase()
        .replace("-", "")
    randomId += Math.random().toString()
        .replace(".", "")
    return randomId
}