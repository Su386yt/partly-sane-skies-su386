package me.partlysanestudios.partlysaneskies;

import org.lwjgl.input.Keyboard;

import me.partlysanestudios.partlysaneskies.dungeons.partymanager.PartyManager;
import me.partlysanestudios.partlysaneskies.help.Help;
import me.partlysanestudios.partlysaneskies.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;

public final class KeyInit {
    

    private final static String PSS_CATEGORY = "Partly Sane Skies";

    public static KeyBinding debugKey;
    public static KeyBinding configKey;
    public static KeyBinding partyManagerKey;
    public static KeyBinding helpKey;
    public static KeyBinding wardrobeKeybind;
    public static KeyBinding petKeybind;
    public static KeyBinding craftKeybind;
    public static KeyBinding storageKeybind;



    public static void init() {
        debugKey = registerKey("Debug", PSS_CATEGORY, Keyboard.KEY_F4);
        configKey = registerKey("Config", PSS_CATEGORY, Keyboard.KEY_F7);
        partyManagerKey = registerKey("Party Manager", PSS_CATEGORY, Keyboard.KEY_P);
        helpKey = registerKey("Help", PSS_CATEGORY, Keyboard.KEY_H);
        wardrobeKeybind = registerKey("Open Wardrobe", PSS_CATEGORY, Keyboard.CHAR_NONE);
        petKeybind = registerKey("Open Pets Menu", PSS_CATEGORY, Keyboard.CHAR_NONE);
        craftKeybind = registerKey("Open Crafting Table", PSS_CATEGORY, Keyboard.CHAR_NONE);
        storageKeybind = registerKey("Open Storage Menu", PSS_CATEGORY, Keyboard.CHAR_NONE);
    }


    private static KeyBinding registerKey(String name, String category, int keycode) {
        final KeyBinding key = new KeyBinding(name, keycode, category);
        ClientRegistry.registerKeyBinding(key);
        return key;
    }

    @SubscribeEvent
    public void checkKeyBinds(KeyInputEvent event) {
        if(debugKey.isPressed()) {
            Main.isDebugMode = !Main.isDebugMode;
            Utils.visPrint("Debug mode: " + Main.isDebugMode);
            Utils.visPrint(Main.getRegionName());
            Main.locationBannerDisplay.lastLocationTime = Minecraft.getSystemTime();
        }
        if(configKey.isPressed()) {
            Main.minecraft.displayGuiScreen(Main.config.gui());
        }
        if(partyManagerKey.isPressed()) {
            PartyManager.startPartyManager();
        }
        if(helpKey.isPressed()) {
            Help.printHelpMessage();
        }
        if(craftKeybind.isPressed()) {
            Main.minecraft.thePlayer.sendChatMessage("/craft");
        }
        if(petKeybind.isPressed()) {
            Main.minecraft.thePlayer.sendChatMessage("/pets");
        }
        if(wardrobeKeybind.isPressed()) {
            Main.minecraft.thePlayer.sendChatMessage("/wardrobe");
        }
        if(storageKeybind.isPressed()) {
            Main.minecraft.thePlayer.sendChatMessage("/storage");
        }
    } 
}