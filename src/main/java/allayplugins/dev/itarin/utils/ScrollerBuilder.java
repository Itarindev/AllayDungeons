package allayplugins.dev.itarin.utils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class ScrollerBuilder {

    private String inventoryName = "Menu";
    private int inventorySize = 54;
    private List<ItemStack> itemList = new ArrayList<>();
    private List<Integer> slots = new ArrayList<>();

    private int backSlot = -1;
    private Consumer<Player> backAction = null;

    private int nextPageSlot = -1;
    private int previousPageSlot = -1;
    private Consumer<Integer> onPageChange = null;

    public ScrollerBuilder withInventoryName(String name) {
        this.inventoryName = name;
        return this;
    }

    public ScrollerBuilder withInventorySize(int size) {
        this.inventorySize = size;
        return this;
    }

    public ScrollerBuilder withItems(List<ItemStack> items) {
        this.itemList = items != null ? items : Collections.emptyList();
        return this;
    }

    public ScrollerBuilder withSlots(List<Integer> slots) {
        if (slots == null || slots.isEmpty()) {
            this.slots = new ArrayList<>();
        } else {
            for (int slot : slots) {
                if (slot < 0 || slot >= inventorySize) {
                    throw new IllegalArgumentException("Slot inválido: " + slot);
                }
            }
            this.slots = new ArrayList<>(slots);
        }
        return this;
    }

    public ScrollerBuilder withBackSlot(int slot, Consumer<Player> action) {
        validateSlot(slot);
        this.backSlot = slot;
        this.backAction = action;
        return this;
    }

    public ScrollerBuilder withPageControls(int previousSlot, int nextSlot, Consumer<Integer> pageChangeAction) {
        validateSlot(previousSlot);
        validateSlot(nextSlot);
        this.previousPageSlot = previousSlot;
        this.nextPageSlot = nextSlot;
        this.onPageChange = pageChangeAction;
        return this;
    }

    public void open(Player player, int page) {
        Inventory inv = Bukkit.createInventory(null, inventorySize, inventoryName);

        int totalItems = itemList.size();
        int itemsPerPage = slots.isEmpty() ? inventorySize : slots.size();
        int maxPages = (int) Math.ceil((double) totalItems / itemsPerPage);

        page = Math.max(0, Math.min(page, maxPages - 1));
        int startIndex = page * itemsPerPage;
        int endIndex = Math.min(startIndex + itemsPerPage, totalItems);

        int itemIndex = startIndex;

        if (!slots.isEmpty()) {
            for (int slot : slots) {
                if (itemIndex >= endIndex) break;
                inv.setItem(slot, itemList.get(itemIndex++));
            }
        } else {
            for (int i = itemIndex; i < endIndex; i++) {
                inv.addItem(itemList.get(i));
            }
        }

        if (backSlot >= 0 && backAction != null) {
            inv.setItem(backSlot, createItem(Material.ARROW, "§cVoltar"));
        }

        if (previousPageSlot >= 0 && page > 0) {
            inv.setItem(previousPageSlot, createItem(Material.PAPER, "§ePágina Anterior"));
        }

        if (nextPageSlot >= 0 && page < maxPages - 1) {
            inv.setItem(nextPageSlot, createItem(Material.PAPER, "§ePróxima Página"));
        }

        player.openInventory(inv);
    }

    public void handleClick(Player player, InventoryClickEvent e, int currentPage) {
        int clicked = e.getRawSlot();
        if (clicked == backSlot && backAction != null) {
            backAction.accept(player);
            e.setCancelled(true);
        }

        if (clicked == previousPageSlot && currentPage > 0 && onPageChange != null) {
            onPageChange.accept(currentPage - 1);
            e.setCancelled(true);
        }

        if (clicked == nextPageSlot && onPageChange != null) {
            onPageChange.accept(currentPage + 1);
            e.setCancelled(true);
        }
    }

    private void validateSlot(int slot) {
        if (slot < 0 || slot >= inventorySize) {
            throw new IllegalArgumentException("Slot inválido: " + slot);
        }
    }

    private ItemStack createItem(Material material, String name) {
        return new ItemBuilder(material).setName(name).build();
    }
}
