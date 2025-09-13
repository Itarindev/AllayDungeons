package allayplugins.dev.itarin.inventories.dungeon;

import allayplugins.dev.itarin.AllayDungeons;
import allayplugins.dev.itarin.configuration.InventoryConfiguration;
import allayplugins.dev.itarin.dao.dungeon.DungeonDAO;
import allayplugins.dev.itarin.utils.ItemBuilder;
import allayplugins.dev.itarin.utils.ScrollerBuilder;
import lombok.val;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ViewDungeonsMenu {

    private final InventoryConfiguration inventoryConfiguration;

    public ViewDungeonsMenu(AllayDungeons main) {
        inventoryConfiguration = main.getInventoryConfiguration();
    }

    public void open(Player player) {
        val items = new ArrayList<ItemStack>();

        DungeonDAO.getDungeons().forEach(dungeon -> {

            val title = inventoryConfiguration.getDungeonInventoryItemTitle().replace("{dungeon_name}", dungeon.getName());

            val enabled = dungeon.isEnabled() ? "§aAtiva" : "§cDesativada";

            val state = dungeon.getDungeonState() == null ? "A explorar" : dungeon.getDungeonState().getDisplayName();

            List<String> lore = inventoryConfiguration.getDungeonInventoryItemLore();
            lore = lore.stream().map(l -> l.replace("{dungeon_min_players}", "" + dungeon.getMinPlayers())
                    .replace("{dungeon_phases}", "" + dungeon.getPhases().size())
                    .replace("{dungeon_state}", state)
                    .replace("{dungeon_status}", enabled))
                    .collect(Collectors.toList());

            val icon = new ItemBuilder(dungeon.getIcon()).setName(title).setLore(lore).build();
            items.add(icon);
        });

        val inventoryName = ChatColor.translateAlternateColorCodes('&', inventoryConfiguration.getDungeonInventoryName());

        new ScrollerBuilder().withInventoryName(inventoryName)
                .withInventorySize(inventoryConfiguration.getDungeonInventorySize())
                .withSlots(inventoryConfiguration.getDungeonInventorySlots())
                .withItems(items).open(player, 0);

    }
}