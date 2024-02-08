//
// Written by Su386 & J10a1n15.
// See LICENSE for copyright and license notices.
//

package me.partlysanestudios.partlysaneskies.features.gui.hud.rngdropbanner

import gg.essential.elementa.ElementaVersion
import gg.essential.elementa.components.UIWrappedText
import gg.essential.elementa.components.Window
import gg.essential.elementa.constraints.CenterConstraint
import gg.essential.elementa.constraints.PixelConstraint
import gg.essential.universal.UMatrixStack
import me.partlysanestudios.partlysaneskies.PartlySaneSkies.Companion.config
import me.partlysanestudios.partlysaneskies.PartlySaneSkies.Companion.minecraft
import me.partlysanestudios.partlysaneskies.PartlySaneSkies.Companion.time
import me.partlysanestudios.partlysaneskies.utils.ChatUtils
import me.partlysanestudios.partlysaneskies.utils.MathUtils.onCooldown
import me.partlysanestudios.partlysaneskies.utils.StringUtils.colorCodeToColor
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import java.awt.Color

object DropBannerDisplay {
    var drop: Drop? = null

    // Whenever someone updates the regex, please replace the regex in the following link:
    // Regex: https://regex101.com/r/lPUJeH/3
    val RARE_DROP_REGEX =
        "(?<dropTitle>(?:§.)+[\\w\\s]*[CD]ROP!) (?:§.)+(?:\\()?(?:§.)*(?:\\d+x )?(?:§.)*(?<dropColor>§.)(?<dropName>◆?[\\s\\w]+)(?:§.)+\\)? ?(?:(?:§.)+)?(?:\\((?:\\+(?:§.)*(?<mf>\\d+)% (?:§.)+✯ Magic Find(?:§.)*|[\\w\\s]+)\\))?".toRegex()


    private const val SMALL_TEXT_SCALE = 2.5f
    private const val BIG_TEXT_SCALE = 5f
    private const val BANNER_HEIGHT_FACTOR = 0.333f
    private const val TEXT_SPACING_FACTOR = 0.05f
    private const val TEXT_BLINK_START_FACTOR = 1f / 3f
    private const val TEXT_BLINK_END_FACTOR = 10f / 12f

    var window = Window(ElementaVersion.V2)

    private var topString = ""
    private var dropNameString = ""
    private var magicFindString = ""

    private var topText = UIWrappedText(dropNameString, true, Color(0, 0, 0, 0), true).apply {
        setTextScale(PixelConstraint(BIG_TEXT_SCALE / 672 * window.getHeight() * config.bannerSize))
        setWidth(PixelConstraint(window.getWidth()))
        setX(CenterConstraint())
        setY(PixelConstraint(window.getHeight() * BANNER_HEIGHT_FACTOR))
        setChildOf(window)
    }
    private var dropNameText = UIWrappedText(dropNameString, true, Color(0, 0, 0, 0), true).apply {
        setTextScale(PixelConstraint(SMALL_TEXT_SCALE / 672 * window.getHeight() * config.bannerSize))
        setWidth(PixelConstraint(window.getWidth()))
        setX(CenterConstraint())
        setY(PixelConstraint(topText.getBottom() + window.getHeight() * TEXT_SPACING_FACTOR))
        setChildOf(window)
    }

    @SubscribeEvent
    fun onChatMessage(event: ClientChatReceivedEvent) {
        val formattedMessage = event.message.formattedText

        val match = RARE_DROP_REGEX.find(formattedMessage) ?: return
        val (dropTitle, dropColor, dropName, mf) = match.destructured


        // TODO: add check for blocked drop

        // TODO: add check for disallowed rarity of drop

        if (config.rareDropBannerSound) {
            minecraft.thePlayer.playSound("partlysaneskies:rngdropjingle", 100f, 1f)
        }

        if (config.rareDropBanner) {
            val dropCategoryHex = colorCodeToColor(dropColor)
            drop = Drop(dropName, dropTitle, dropCategoryHex, mf.toInt(), time)
        }
    }

    @SubscribeEvent
    fun renderText(event: RenderGameOverlayEvent.Text) {
        if (drop == null) {
            dropNameString = ""
            topString = ""
            magicFindString = ""

            return
        }

        var categoryColor = drop!!.dropCategoryColor
        dropNameString = drop!!.name + (if (drop!!.magicFind > 0) " §b(+${drop!!.magicFind}% ✯ Magic Find)" else "")
        topString = drop!!.dropCategory

        if ((time - drop!!.timeDropped > TEXT_BLINK_START_FACTOR * config.rareDropBannerTime * 1000)
            && (time - drop!!.timeDropped < TEXT_BLINK_END_FACTOR * config.rareDropBannerTime * 1000)
        ) {
            categoryColor = if (Math.round((drop!!.timeDropped - time) / 1000f * 4) % 2 == 0) {
                Color.white
            } else {
                drop!!.dropCategoryColor
            }
        }

        if (!onCooldown(drop!!.timeDropped, (config.rareDropBannerTime * 1000).toLong())) {
            drop = null
            return
        }

        if (topText.getText().isEmpty() && topString.isEmpty() && dropNameText.getText()
                .isEmpty() && dropNameString.isEmpty()
        ) {
            return
        }

        val scale = config.bannerSize
        topText
            .setText(topString)
            .setTextScale(PixelConstraint((BIG_TEXT_SCALE / 672) * window.getHeight() * scale))
            .setWidth(PixelConstraint(window.getWidth()))
            .setX(CenterConstraint())
            .setY(PixelConstraint(window.getHeight() * BANNER_HEIGHT_FACTOR))
            .setColor(categoryColor)

        dropNameText
            .setText(dropNameString)
            .setTextScale(PixelConstraint((SMALL_TEXT_SCALE / 672) * window.getHeight() * scale))
            .setWidth(PixelConstraint(window.getWidth()))
            .setX(CenterConstraint())
            .setY(PixelConstraint(topText.getBottom() + window.getHeight() * (TEXT_SPACING_FACTOR * scale)))

        window.draw(UMatrixStack())
    }
}