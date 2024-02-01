//
// Written by DerGruenkohl
// See LICENSE for copyright and license notices.
//


package me.partlysanestudios.partlysaneskies.features.chat

import cc.polyfrost.oneconfig.events.event.ChatSendEvent
import cc.polyfrost.oneconfig.libs.eventbus.Subscribe
import me.partlysanestudios.partlysaneskies.PartlySaneSkies
import me.partlysanestudios.partlysaneskies.PartlySaneSkies.Companion.config


//Currently only using to owoify send chat messages (meow)
object ChatTransformer {
    var lastmsg = ""

    @Subscribe
    fun onChat(event: ChatSendEvent) {
        //dont mess with chats that dont want to
        if (!doChatTransform()) return

        event.isCancelled = true
        val msg = event.message
        //Do not go into recursion (bad)
        if (lastmsg == msg) {
            event.isCancelled = false
            return
        }
        //dont break commands :)
        if (msg.startsWith("/")) return

        val player = PartlySaneSkies.minecraft.thePlayer
        var transformedmsg: String? = null

        if (config.transformOWO) {
            transformedmsg = OwO.owoify(msg)
        }
        if (transformedmsg == null) return
        // I dont know why putting this in a thread fixes it, but I do not complain
        Thread {
            lastmsg = transformedmsg
            player.sendChatMessage(transformedmsg)
            lastmsg = ""
        }.start()
    }
    private fun doChatTransform(): Boolean {
        return config.transformOWO
    }
}