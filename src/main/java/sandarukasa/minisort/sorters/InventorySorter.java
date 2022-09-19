package sandarukasa.minisort.sorters;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;

import java.util.Arrays;
import java.util.Optional;

public abstract sealed class InventorySorter permits ContainerSorter, HorseInventorySorter, PlayerInventorySorter {
    public final MinecraftClient client;
    public final HandledScreen<?> screen;
    protected final ScreenHandler handler;

    protected InventorySorter(MinecraftClient client, HandledScreen<?> screen) {
        this.client = client;
        this.screen = screen;
        this.handler = screen.getScreenHandler();
    }

    public static Optional<InventorySorter> getSorter(MinecraftClient client, HandledScreen<?> screen) {
        if (screen instanceof InventoryScreen) {
            return Optional.of(new PlayerInventorySorter(client, screen));
        } else if (screen instanceof HorseScreen) {
            return Optional.of(new HorseInventorySorter(client, screen));
        } else if (screen instanceof GenericContainerScreen || //
                screen instanceof Generic3x3ContainerScreen || //
                screen instanceof ShulkerBoxScreen || //
                screen instanceof HopperScreen) {
            return Optional.of(new ContainerSorter(client, screen));
        } else {
            return Optional.empty();
        }
    }

    private static int nextGap(int gap) {
        return gap <= 1 ? 0 : (gap + 1) / 2;
    }

    private void click(Slot[] slots, int i) {
        assert client.interactionManager != null;
        client.interactionManager.clickSlot(handler.syncId, slots[i].id, 0, SlotActionType.PICKUP, client.player);
    }

    private void swap(Slot[] slots, int[] keys, int i, int j) {
        int t = keys[i];
        keys[i] = keys[j];
        keys[j] = t;

        click(slots, i);
        click(slots, j);
        click(slots, i);
    }

    private void inPlaceMerge(Slot[] slots, int[] keys, int l, int r) {
        int gap = r - l + 1;
        for (gap = nextGap(gap); gap > 0; gap = nextGap(gap)) {
            for (int i = l; i + gap <= r; i++) {
                int j = i + gap;
                if (keys[i] > keys[j]) {
                    swap(slots, keys, i, j);
                }
            }
        }
    }

    private void mergeSort(Slot[] slots, int[] keys, int l, int r) {
        if (l < r) {
            int mid = (l + r) / 2;
            mergeSort(slots, keys, l, mid);
            mergeSort(slots, keys, mid + 1, r);
            inPlaceMerge(slots, keys, l, r);
        }
    }

    private void mergeSort(Slot[] slots) {
        mergeSort(slots, Arrays.stream(slots).mapToInt(slot -> {
            ItemStack stack = slot.getStack();
            return stack.isEmpty() ? Integer.MAX_VALUE : Item.getRawId(stack.getItem());
        }).toArray(), 0, slots.length - 1);
    }

    public void sort() {
        // TODO: check if player holds any items (if so, refuse to sort)
        final var slots = getSlotsToSort();
        mergeSort(slots);
        // TODO: stack
    }

    protected abstract Slot[] getSlotsToSort();
}
