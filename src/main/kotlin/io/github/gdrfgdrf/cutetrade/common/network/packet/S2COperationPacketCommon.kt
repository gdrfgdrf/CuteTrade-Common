package io.github.gdrfgdrf.cutetrade.common.network.packet

import io.github.gdrfgdrf.cutetrade.common.network.PacketContext
import io.github.gdrfgdrf.cutetrade.common.network.interfaces.PacketAdapter
import io.github.gdrfgdrf.cutetrade.common.network.interfaces.Writeable
import io.github.gdrfgdrf.cutetrade.common.operation.OperationDispatcher
import io.github.gdrfgdrf.cutetrade.common.proxy.ItemStackProxy
import io.github.gdrfgdrf.cutetrade.common.proxy.PacketByteBufProxy

class S2COperationPacketCommon : PacketAdapter {
    val operatorName: String
    var stringArgsLength: Int = -1
    var stringArgs: Array<String?>? = null
        set(value) {
            field = value
            stringArgsLength = stringArgs?.size!!
        }

    var intArgsLength: Int = -1
    var intArgs: Array<Int?>? = null
        set(value) {
            field = value
            intArgsLength = intArgs?.size!!
        }

    var itemStackArgsLength: Int = -1
    var itemStackArgs: Array<ItemStackProxy?>? = null
        set(value) {
            field = value
            itemStackArgsLength = itemStackArgs?.size!!
        }

    constructor(operatorName: String) {
        this.operatorName = operatorName
    }

    constructor(byteBuf: PacketByteBufProxy) {
        this.operatorName = byteBuf.readString()
        this.stringArgsLength = byteBuf.readInt()
        this.intArgsLength = byteBuf.readInt()
        this.itemStackArgsLength = byteBuf.readInt()

        if (stringArgsLength > 0) {
            stringArgs = arrayOfNulls(stringArgsLength)
            for (i in 0 until stringArgsLength) {
                stringArgs!![i] = byteBuf.readString()
            }
        }
        if (intArgsLength > 0) {
            intArgs = arrayOfNulls(intArgsLength)
            for (i in 0 until intArgsLength) {
                intArgs!![i] = byteBuf.readInt()
            }
        }
        if (itemStackArgsLength > 0) {
            itemStackArgs = arrayOfNulls(itemStackArgsLength)
            for (i in 0 until itemStackArgsLength) {
                itemStackArgs!![i] = byteBuf.readItemStack()
            }
        }
    }

    override fun getOperatorName(): String = operatorName

    override fun getStringArgs(): Array<String?>? = stringArgs

    override fun setStringArgs(args: Array<String?>?) {
        this.stringArgs = args
    }

    override fun getIntArgs(): Array<Int?>? = intArgs

    override fun setIntArgs(args: Array<Int?>?) {
        this.intArgs = args
    }

    override fun getItemStackArgs(): Array<ItemStackProxy?>? = itemStackArgs

    override fun setItemStackArgs(args: Array<ItemStackProxy?>?) {
        this.itemStackArgs = args
    }

    override fun write(byteBuf: PacketByteBufProxy) {
        byteBuf.writeString(operatorName)
        byteBuf.writeInt(stringArgsLength)
        byteBuf.writeInt(intArgsLength)
        byteBuf.writeInt(itemStackArgsLength)

        if (stringArgsLength > 0) {
            stringArgs!!.forEach {
                it ?: return@forEach
                byteBuf.writeString(it)
            }
        }
        if (intArgsLength > 0) {
            intArgs!!.forEach {
                it?.let {
                    byteBuf.writeInt(it)
                }
            }
        }
        if (itemStackArgsLength > 0) {
            itemStackArgs!!.forEach {
                it ?: return@forEach
                byteBuf.writeItemStack(it)
            }
        }
    }

    companion object {
        fun read(byteBuf: PacketByteBufProxy): S2COperationPacketCommon = S2COperationPacketCommon(byteBuf)

        fun handle(context: PacketContext<S2COperationPacketCommon>) {
            val packet = context.message
            val operatorName = packet.operatorName
            val stringArgsLength = packet.stringArgsLength
            val intArgsLength = packet.intArgsLength
            val itemStackArgsLength = packet.itemStackArgsLength

            if (stringArgsLength > 0 && intArgsLength > 0 && itemStackArgsLength > 0) {
                OperationDispatcher.dispatch(operatorName, context, arrayOf(packet.stringArgs, packet.intArgs, packet.itemStackArgs))
                return
            }
            if (stringArgsLength > 0 && intArgsLength > 0 && itemStackArgsLength <= 0) {
                OperationDispatcher.dispatch(operatorName, context, arrayOf(packet.stringArgs, packet.intArgs))
                return
            }
            if (stringArgsLength > 0 && intArgsLength <= 0 && itemStackArgsLength > 0) {
                OperationDispatcher.dispatch(operatorName, context, arrayOf(packet.stringArgs, packet.itemStackArgs))
                return
            }
            if (stringArgsLength > 0 && intArgsLength <= 0 && itemStackArgsLength <= 0) {
                OperationDispatcher.dispatch(operatorName, context, packet.stringArgs)
                return
            }

            if (stringArgsLength <= 0 && intArgsLength > 0 && itemStackArgsLength > 0) {
                OperationDispatcher.dispatch(operatorName, context, arrayOf(packet.intArgs, packet.itemStackArgs))
                return
            }
            if (stringArgsLength <= 0 && intArgsLength > 0 && itemStackArgsLength <= 0) {
                OperationDispatcher.dispatch(operatorName, context, packet.intArgs)
            }
            if (stringArgsLength <= 0 && intArgsLength <= 0 && itemStackArgsLength > 0) {
                OperationDispatcher.dispatch(operatorName, context, packet.itemStackArgs)
            }
            if (stringArgsLength <= 0 && intArgsLength <= 0 && itemStackArgsLength <= 0) {
                OperationDispatcher.dispatch(operatorName, context, null)
            }
        }
    }
}