//
// Written by DerGruenkohl
// See LICENSE for copyright and license notices.
//

package me.partlysanestudios.partlysaneskies.features.chat

import me.partlysanestudios.partlysaneskies.PartlySaneSkies
import me.partlysanestudios.partlysaneskies.PartlySaneSkies.Companion.config
import org.polyfrost.oneconfig.api.event.v1.events.ChatEvent
import org.polyfrost.oneconfig.api.event.v1.invoke.impl.Subscribe

// Currently only using to owoify send chat messages (meow)
object ChatTransformer {
    var lastmsg = ""

    @Subscribe
    fun onChat(event: ChatEvent.Send) {
        // dont mess with chats that dont want to
        if (!doChatTransform()) {
            return
        }

        val msg = event.message

        // dont break commands :)
        if (msg.startsWith("/")) {
            return
        }

        event.cancelled = true

        // Do not go into recursion (bad)
        if (lastmsg == msg) {
            event.cancelled = false
            return
        }

        val player = PartlySaneSkies.minecraft.thePlayer
        var transformedmsg = ""

        if (config.transformOWO) {
            transformedmsg = OwO.owoify(msg)
        }

        if (transformedmsg.isEmpty()) {
            return
        }

        // Putting this in a thread is needed so the recursive call doesn't wipe/duplicate the message
        Thread {
            lastmsg = transformedmsg
            player.sendChatMessage(transformedmsg)
            lastmsg = ""
        }.start()
    }

    private fun doChatTransform(): Boolean = config.transformOWO
}
