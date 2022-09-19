package sandarukasa.minisort.sorters;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.screen.slot.Slot;

public final class ContainerSorter extends InventorySorter {
    ContainerSorter(MinecraftClient client, HandledScreen<?> screen) {
        super(client, screen);
    }

    @Override
    protected Slot[] getSlotsToSort() {
        assert client.player != null;
        final var playerInventory = client.player.getInventory();
        return handler.slots.stream()
                .filter(slot -> !slot.inventory.equals(playerInventory))
                .toArray(Slot[]::new);
    }
}
