package io.github.cutetrade.common.extension

import io.github.cutetrade.common.CommonFunctions
import io.github.cutetrade.common.pool.CommonFunctionsPool

fun getProxyFactory(): CommonFunctions.ProxyFactory {
    val factory = CommonFunctionsPool.getProxy<CommonFunctions.ProxyFactory>(CommonFunctions.ProxyFactory::class.java)
    return factory
}