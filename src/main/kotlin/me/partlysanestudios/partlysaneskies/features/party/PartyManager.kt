package me.partlysanestudios.partlysaneskies.features.party

import gg.essential.elementa.ElementaVersion
import gg.essential.elementa.WindowScreen
import gg.essential.elementa.components.UIWrappedText
import gg.essential.elementa.constraints.CramSiblingConstraint
import gg.essential.elementa.constraints.SiblingConstraint
import gg.essential.elementa.dsl.childOf
import gg.essential.elementa.dsl.constrain
import gg.essential.elementa.dsl.percent
import gg.essential.elementa.dsl.plus
import me.partlysanestudios.partlysaneskies.PartlySaneSkies.Companion.minecraft
import me.partlysanestudios.partlysaneskies.commands.PSSCommand
import me.partlysanestudios.partlysaneskies.data.skyblockdata.SkyblockDataManager
import me.partlysanestudios.partlysaneskies.features.themes.ThemeManager.currentBackgroundUIImage
import me.partlysanestudios.partlysaneskies.render.gui.constraints.TextScaledPixelConstraint.Companion.textScaledPixels
import me.partlysanestudios.partlysaneskies.utils.MinecraftUtils

class PartyManager: WindowScreen(ElementaVersion.V5) {
    companion object {
        fun registerCommand() {
            PSSCommand("partymanager")
                .addAlias("pm", "partypm")
                .setDescription("Opens the Party Manager")
                .setRunnable { _, ->
                    MinecraftUtils.displayGuiScreen(PartyManager())
                }
                .register()
        }
    }
    val userPanel = currentBackgroundUIImage.constrain {
        x = 7.5.percent
        y = 10.percent
        width = 20.percent
        height = 80.percent
    } childOf window

    val currentPartyPreview = currentBackgroundUIImage.constrain {
        x = 20.percent + 7.5.percent + 5.percent
        y = 10.percent
        width = 60.percent
        height = 37.5.percent
    } childOf window

    val permPartyPreview = currentBackgroundUIImage.constrain {
        x = 20.percent + 7.5.percent + 5.percent
        y = 10.percent + 37.5.percent + 5.percent
        width = 60.percent
        height = 37.5.percent
    } childOf window

    init {
        displayUserData()
    }

    fun displayUserData() {
        val username = UIWrappedText(minecraft.session.username).constrain {
            x = 3.percent
            y = 3.percent
            width = 100.percent
            textScale = 2.textScaledPixels
        } childOf userPanel

        val normalFloors = UIWrappedText().constrain {
            x = 3.percent
            y = SiblingConstraint() + 1.percent
            textScale = 1.textScaledPixels
            width = 45.percent
        } childOf userPanel

        val masterFloors = UIWrappedText().constrain {
            x = 3.percent + 45.percent + 3.percent
            y = CramSiblingConstraint()
            textScale = 1.textScaledPixels
            width = 45.percent
        } childOf userPanel

        Thread() {
            val player = SkyblockDataManager.getPlayer(minecraft.session.username)


            var normalRuns = "FE: ${player.normalRunCount[0]}"
            for (i in 1..<player.normalRunCount.size) {
                normalRuns += "\nF$i: ${player.normalRunCount[i]}"
            }

            normalFloors.setText(normalRuns)

            var masterRuns = "ME: ${player.masterModeRunCount[0]}"
            for (i in 1..<player.masterModeRunCount.size) {
                masterRuns += "\nM$i: ${player.masterModeRunCount[i]}"
            }

            masterFloors.setText(masterRuns)
        }.start()
    }
}
