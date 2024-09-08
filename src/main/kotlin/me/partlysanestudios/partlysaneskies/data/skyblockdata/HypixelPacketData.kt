// package me.partlysanestudios.partlysaneskies.data.skyblockdata
//
// import io.netty.buffer.ByteBuf
// import io.netty.buffer.Unpooled
// import net.minecraft.client.Minecraft
// import net.minecraft.network.PacketBuffer
// import net.minecraft.network.play.client.C17PacketCustomPayload
// import net.minecraft.network.play.server.S3FPacketCustomPayload
// import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientCustomPacketEvent
// import java.nio.Buffer
// import java.util.UUID
//
// object HypixelPacketData {
//
//     fun onPacketReceive(event: ClientCustomPacketEvent) {
//         val packet = event.packet
//         val channel = packet.channel()
//         val data = packet.bufferData
//
//         when (channel) {
//             "hypixel:party_info" -> {
//                 data.use {
//                     val successful = data.readBoolean()
//                     if (!successful) return@use
//                     data.readVarInt()
//                     if (data.readBoolean()) {
//                         val members = mutableMapOf<UUID, PartyRole>()
//                         repeat(data.readVarInt()) {
//                             members[data.readUuid()] = PartyRole.entries[data.readVarInt()]
//                         }
//                         onPartyInfo(true, members)
//                     } else {
//                         onPartyInfo(false)
//                     }
//                 }
//             }
//         }
//     }
//
//     fun onPartyInfo(inparty: Boolean, members: Map<UUID, PartyRole>? = null) {
//
//     }
//
//     fun requestPartyInfo() {
//         val buffer = PacketBuffer(Unpooled.buffer())
//         buffer.writeByte(2)
//         Minecraft.getMinecraft().netHandler.addToSendQueue(C17PacketCustomPayload("hypixel:party_info", buffer))
//     }
//
// }
//
// enum class PartyRole {
//     LEADER,
//     MOD,
//     MEMBER,
// }
//
// object ByteBufUtils {
//
//     fun <T : ByteBuf> T.use(block: T.() -> Unit) {
//         runCatching { block() }
//         resetReaderIndex()
//     }
//
//     fun ByteBuf.readVarInt(): Int {
//         var i = 0
//         var j = 0
//
//         var b: Byte
//         do {
//             b = this.readByte()
//             i = i or ((b.toInt() and 127) shl (j++ * 7))
//             if (j > 5) {
//                 throw RuntimeException("VarInt too big")
//             }
//         } while ((b.toInt() and 128) == 128)
//
//         return i
//     }
// }
