package allayplugins.dev.itarin.inventories.reward;

import allayplugins.dev.itarin.model.Dungeon;
import lombok.val;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class DungeonRewardEditMenu {

    private final Inventory inventory;

    public DungeonRewardEditMenu(Dungeon dungeon) {
        this.inventory = Bukkit.createInventory(null, 27, "§8Editar Recompensas #" + dungeon.getId());

        if (dungeon.getRewards() != null) {
            for (val item : dungeon.getRewards()) {
                if (item != null)
                    inventory.addItem(item);
            }
        }
    }

    public void open(Player player) {
        player.openInventory(inventory);
    }

    public Inventory getInventory() {
        return inventory;
    }
}