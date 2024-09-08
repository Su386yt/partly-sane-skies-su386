package me.partlysanestudios.partlysaneskies.data.hypixeldata

import me.partlysanestudios.partlysaneskies.PartlySaneSkies.Companion.minecraft
import me.partlysanestudios.partlysaneskies.data.skyblockdata.SkyblockDataManager
import me.partlysanestudios.partlysaneskies.utils.ChatUtils
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

object FriendsData {
    private val pageRegex = "Friends \\(Page (\\d+) of (\\d+)\\)".toRegex()
    private val friendOfflineRegex = "(?:§.)?(\\w+) (?:§.)?is currently offline".toRegex()
    private val friendOnlineRegex = "(?:§.)?(\\w+) (?:§.)?is in \\w+(?:\\s\\w+)*".toRegex()

    private val guildMemberJoinRegex = "(?:§.)?Guild > (?:§.)?(?:§.)?(\\w+) (?:§.)?(?:§.)?joined\\.".toRegex()
    private val guildMemberLeaveRegex = "(?:§.)?Guild > (?:§.)?(?:§.)?(\\w+) (?:§.)?(?:§.)?left\\.".toRegex()

    private val friendJoinRegex = "(?:§.)?Friend > (?:§.)?(?:§.)?(\\w+) (?:§.)?(?:§.)?joined\\.".toRegex()
    private val friendLeaveRegex = "(?:§.)?Friend > (?:§.)?(?:§.)?(\\w+) (?:§.)?(?:§.)?left\\.".toRegex()
    private var updatingList = false
    private var friendsCache = HashMap<String, Friend>()

    fun updateFriendsCache() {
        updatingList = true
        sendFreindsCommand()
    }

    @SubscribeEvent
    fun onChatEvent(event: ClientChatReceivedEvent) {
        val message = event.message.formattedText

        if (message.contains(pageRegex)) {
            val friends = getFriendsFromMessage(message)

            for (friend in friends) {
                friendsCache[friend.name] = friend
            }
            Thread {
                for (friend in friends) {
                    try {
                        SkyblockDataManager.getPlayer(friend.name) // loads the data for all of the friends
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }.start()

            if (updatingList) {
                event.isCanceled = true
                val (currentPage, maxPage) = pageRegex.find(message)?.destructured ?: return

                if (currentPage != maxPage) {
                    sendFreindsCommand((currentPage.toIntOrNull() ?: 1) + 1)
                } else {
                    updatingList = false
                }
            }

        }

        else if (message.contains(friendJoinRegex)) {
            val username = friendJoinRegex.find(message)?.groups?.get(1)?.value ?: return
            friendsCache[username] = Friend(username, true)

            Thread {
                try {
                    SkyblockDataManager.getPlayer(username) // loads the data for the friend
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }.start()
        } else if (message.contains(friendLeaveRegex)) {
            val username = friendLeaveRegex.find(message)?.groups?.get(1)?.value ?: return
            friendsCache[username] = Friend(username, false)

            Thread {
                try {
                    SkyblockDataManager.getPlayer(username) // loads the data for the friend
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }.start()
        }

        else if (message.contains(guildMemberJoinRegex)) {
            val username = guildMemberJoinRegex.find(message)?.groups?.get(1)?.value ?: return
            if (friendsCache.containsKey(username)) { // not all guild members are friends
                friendsCache[username] = Friend(username, true)

                Thread {
                    try {
                        SkyblockDataManager.getPlayer(username) // loads the data for the friend
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }.start()
            }
        } else if (message.contains(guildMemberLeaveRegex)) {
            val username = guildMemberLeaveRegex.find(message)?.groups?.get(1)?.value ?: return
            if (friendsCache.containsKey(username)) { // not all guild members are friends
                friendsCache[username] = Friend(username, false)

                Thread {
                    try {
                        SkyblockDataManager.getPlayer(username) // loads the data for the friend
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }.start()
            }
        }
    }

    private fun sendFreindsCommand(page: Int = 1) {
        minecraft.thePlayer.sendChatMessage("/friends list $page")
    }

    private fun getFriendsFromMessage(message: String): List<Friend> {
        val friends = ArrayList<Friend>()

        val lines = message.split("\n")

        for (line in lines) {
            ChatUtils.sendClientMessage("test")
            if (friendOfflineRegex.containsMatchIn(line)) {
                val username = friendOfflineRegex.find(line)?.groups?.get(1)?.value ?: continue
                friends.add(Friend(username, false))
            }
            else if (friendOnlineRegex.containsMatchIn(line)) {
                val username = friendOnlineRegex.find(line)?.groups?.get(1)?.value ?: continue
                friends.add(Friend(username, true))
            }
        }

        return friends
    }

    data class Friend(val name: String, var online: Boolean)
}
