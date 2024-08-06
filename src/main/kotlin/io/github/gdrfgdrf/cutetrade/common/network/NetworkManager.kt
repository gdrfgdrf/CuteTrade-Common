package io.github.gdrfgdrf.cutetrade.common.network

import io.github.gdrfgdrf.cutetrade.common.CommonFunctions
import io.github.gdrfgdrf.cutetrade.common.network.interfaces.PacketAdapter
import io.github.gdrfgdrf.cutetrade.common.network.interfaces.Writeable
import io.github.gdrfgdrf.cutetrade.common.network.packet.C2SOperationPacketCommon
import io.github.gdrfgdrf.cutetrade.common.network.packet.S2COperationPacketCommon
import io.github.gdrfgdrf.cutetrade.common.pool.CommonFunctionsPool
import io.github.gdrfgdrf.cutetrade.common.proxy.PacketByteBufProxy
import io.github.gdrfgdrf.cutetrade.common.proxy.PlayerProxy

object NetworkManager {
    private val NETWORK_FUNCTIONS =
        CommonFunctionsPool.getFunctions<CommonFunctions.NetworkFunctions>(CommonFunctions.NetworkFunctions::class.java)

    fun initialize(
        registerPacketInterface: RegisterPacketInterface
    ) {
        registerPacketInterface.register(
            NETWORK_FUNCTIONS.getOrCreateS2CIdentifier(),
            S2COperationPacketCommon::class.java,
            S2COperationPacketCommon::write,
            S2COperationPacketCommon::read,
            S2COperationPacketCommon::handle
        )

        registerPacketInterface.register(
            NETWORK_FUNCTIONS.getOrCreateC2SIdentifier(),
            C2SOperationPacketCommon::class.java,
            C2SOperationPacketCommon::write,
            C2SOperationPacketCommon::read,
            C2SOperationPacketCommon::handle
        )
    }

    fun sendToServer(packetAdapter: PacketAdapter) {
        NETWORK_FUNCTIONS.sendToServer(packetAdapter)
    }

    fun sendToClient(identifier: Any, playerProxy: PlayerProxy, packetAdapter: PacketAdapter) {
        NETWORK_FUNCTIONS.sendToClient(identifier, playerProxy, packetAdapter)
    }

    interface RegisterPacketInterface {
        fun <T> register(
            packetIdentifier: Any,
            messageType: Class<out Writeable>,
            encoder: (T, PacketByteBufProxy) -> Unit,
            decoder: (PacketByteBufProxy) -> T,
            handler: (PacketContext<T>) -> Unit
        )
    }

}