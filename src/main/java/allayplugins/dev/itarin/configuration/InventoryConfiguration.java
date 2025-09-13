package allayplugins.dev.itarin.configuration;

import allayplugins.dev.itarin.AllayDungeons;
import allayplugins.dev.itarin.utils.MultipleFiles;
import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

@Getter
public class InventoryConfiguration {

    private final FileConfiguration configuration;

    public InventoryConfiguration(AllayDungeons main) {
        configuration = MultipleFiles.getConfig(main, "inventories");
    }

    private String dungeonInventoryName;
    private int dungeonInventorySize;
    private List<Integer> dungeonInventorySlots;
    private String dungeonInventoryItemTitle;
    private List<String> dungeonInventoryItemLore;

    public void loadInventories() {

        dungeonInventoryName = configuration.getString("Inventories.Dungeon.Name");
        dungeonInventorySize = configuration.getInt("Inventories.Dungeon.Size");
        dungeonInventorySlots = configuration.getIntegerList("Inventories.Dungeon.Slots");
        dungeonInventoryItemTitle = configuration.getString("Inventories.Dungeon.Item.Title");
        dungeonInventoryItemLore = configuration.getStringList("Inventories.Dungeon.Item.Lore");

    }
}