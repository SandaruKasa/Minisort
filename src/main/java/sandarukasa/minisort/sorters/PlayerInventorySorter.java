package sandarukasa.minisort.sorters;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.slot.Slot;

public final class PlayerInventorySorter extends InventorySorter {
    PlayerInventorySorter(MinecraftClient client, HandledScreen<?> screen) {
        super(client, screen);
    }

    @Override
    protected Slot[] getSlotsToSort() {
        assert client.player != null;
        final var playerInventory = client.player.getInventory();
        return handler.slots.stream().filter(slot -> {
            if (!slot.inventory.equals(playerInventory)) {
                // not a crafting slot
                return false;
            }
            final var index = slot.getIndex();
            // is in main area (not offhand / armor) and isn't in the hotbar
            return index < PlayerInventory.MAIN_SIZE && !PlayerInventory.isValidHotbarIndex(index);
        }).toArray(Slot[]::new);
    }
}
