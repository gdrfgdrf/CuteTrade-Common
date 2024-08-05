package io.github.cutetrade.common.pool

import java.util.concurrent.ConcurrentHashMap

object CommonFunctionsPool {
    private val PROXY_POOL = ConcurrentHashMap<Class<*>, Any>()

    @Suppress("UNCHECKED_CAST")
    fun <T> getProxy(clazz: Class<*>): T {
        return this.PROXY_POOL[clazz] as T
    }

    fun addProxy(any: Any) {
        this.PROXY_POOL[any.javaClass] = any
    }

    fun removeProxy(clazz: Class<*>) {
        this.PROXY_POOL.remove(clazz)
    }
}