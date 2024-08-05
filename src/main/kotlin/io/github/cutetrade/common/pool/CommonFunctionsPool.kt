package io.github.cutetrade.common.pool

import java.util.concurrent.ConcurrentHashMap

object CommonFunctionsPool {
    private val FUNCTIONS_POOL = ConcurrentHashMap<Class<*>, Any>()

    @Suppress("UNCHECKED_CAST")
    fun <T> getFunctions(clazz: Class<*>): T {
        return this.FUNCTIONS_POOL[clazz] as T
    }

    fun addFunctions(any: Any) {
        this.FUNCTIONS_POOL[any.javaClass] = any
    }

    fun removeFunctions(clazz: Class<*>) {
        this.FUNCTIONS_POOL.remove(clazz)
    }
}