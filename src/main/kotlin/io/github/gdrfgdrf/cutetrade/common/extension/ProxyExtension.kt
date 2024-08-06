package io.github.gdrfgdrf.cutetrade.common.extension

import io.github.gdrfgdrf.cutetrade.common.CommonFunctions
import io.github.gdrfgdrf.cutetrade.common.pool.CommonFunctionsPool

fun getProxyFactory(): CommonFunctions.ProxyFactory {
    val factory = CommonFunctionsPool.getFunctions<CommonFunctions.ProxyFactory>(CommonFunctions.ProxyFactory::class.java)
    return factory
}