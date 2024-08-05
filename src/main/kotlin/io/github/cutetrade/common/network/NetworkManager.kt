package io.github.cutetrade.common.network

import io.github.cutetrade.common.CommonFunctions
import io.github.cutetrade.common.pool.CommonFunctionsPool
import io.github.cutetrade.common.proxy.PacketByteBufProxy
import io.github.cutetrade.common.proxy.PlayerProxy

object NetworkManager {
    private val MAIN_FUNCTIONS =
        CommonFunctionsPool.getFunctions<CommonFunctions.MainFunctions>(CommonFunctions.MainFunctions::class.java)
    private val NETWORK_FUNCTIONS =
        CommonFunctionsPool.getFunctions<CommonFunctions.NetworkFunctions>(CommonFunctions.NetworkFunctions::class.java)

    val S2C_OPERATION: Any = MAIN_FUNCTIONS.createIdentifier("cutetrade_networking", "s2c_operation")
    val C2S_OPERATION: Any = MAIN_FUNCTIONS.createIdentifier("cutetrade_networking", "c2s_operation")

    fun initialize(
        registerPacketInterface: RegisterPacketInterface
    ) {
        registerPacketInterface.register(
            S2C_OPERATION,
            S2COperationPacket::class.java,
            S2COperationPacket::write,
            S2COperationPacket::read,
            S2COperationPacket::handle
        )

        registerPacketInterface.register(
            C2S_OPERATION,
            C2SOperationPacket::class.java,
            C2SOperationPacket::write,
            C2SOperationPacket::read,
            C2SOperationPacket::handle
        )
    }

    fun sendToServer(writer: (PacketByteBufProxy) -> Unit) {
        NETWORK_FUNCTIONS.sendToServer(writer)
    }

    fun sendToClient(identifier: Any, playerProxy: PlayerProxy, writer: (PacketByteBufProxy) -> Unit) {
        NETWORK_FUNCTIONS.sendToClient(identifier, playerProxy, writer)
    }

    interface RegisterPacketInterface {
        fun <T> register(
            packetIdentifier: Any,
            messageType: Class<out T>,
            encoder: (T, PacketByteBufProxy) -> Unit,
            decoder: (PacketByteBufProxy) -> T,
            handler: (PacketContext<T>) -> Unit
        )
    }

}