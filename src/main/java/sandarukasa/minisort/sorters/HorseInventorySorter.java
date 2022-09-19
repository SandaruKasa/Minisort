package sandarukasa.minisort.sorters;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.screen.slot.Slot;
import org.apache.commons.lang3.NotImplementedException;

public final class HorseInventorySorter extends InventorySorter {
    HorseInventorySorter(MinecraftClient client, HandledScreen<?> screen) {
        super(client, screen);
    }

    @Override
    protected Slot[] getSlotsToSort() {
        throw new NotImplementedException(); // TODO
    }
}
