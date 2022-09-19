package sandarukasa.minisort;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenKeyboardEvents;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;
import sandarukasa.minisort.sorters.InventorySorter;

@Environment(EnvType.CLIENT)
public class Minisort implements ClientModInitializer {
    private KeyBinding keyBinding;

    @Override
    public void onInitializeClient() {
        keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                // The translation key of the keybinding's name
                "key.minisort.sort",
                // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
                InputUtil.Type.KEYSYM,
                // The keycode of the default key
                GLFW.GLFW_KEY_R,
                // The translation key of the keybinding's category.
                "category.minisort"));


        // When a new screen appears,
        ScreenEvents.AFTER_INIT.register((client, screen, _width, _height) -> {
            // try to get a sorter that can sort it.
            if (screen instanceof HandledScreen<?> handledScreen) {
                final var optionalSorter = InventorySorter.getSorter(client, handledScreen);
                // If such sorter exists,
                optionalSorter.ifPresent(sorter -> {
                    // wait for a key to get pressed,
                    ScreenKeyboardEvents.afterKeyPress(screen).register((screenToSort, keyCode, scanCode, _modifiers) -> {
                        assert screenToSort == handledScreen;
                        // and then, if it's the bind key,
                        if (keyBinding.matchesKey(keyCode, scanCode)) {
                            // sort the screen.
                            sorter.sort();
                        }
                    });
                });
            }
        });
        // I'm genuinely sorry for the lambda mess above,
        // but that's callbacks for ya.
    }
}
