package io.github.gdrfgdrf.cutetrade.common.pool

import java.util.concurrent.ConcurrentHashMap

object CommonFunctionsPool {
    private val FUNCTIONS_POOL = ConcurrentHashMap<Class<*>, Any>()

    @Suppress("UNCHECKED_CAST")
    fun <T> getFunctions(clazz: Class<*>): T {
        return this.FUNCTIONS_POOL[clazz] as T
    }

    fun addFunctions(any: Any) {
        val interfaces = any.javaClass.interfaces
        if (interfaces.isNotEmpty()) {
            val clazz = interfaces[0]
            this.FUNCTIONS_POOL[clazz] = any
        } else {
            throw IllegalArgumentException("the interface array of ${any.javaClass} is empty")
        }
    }

    fun removeFunctions(clazz: Class<*>) {
        this.FUNCTIONS_POOL.remove(clazz)
    }
}