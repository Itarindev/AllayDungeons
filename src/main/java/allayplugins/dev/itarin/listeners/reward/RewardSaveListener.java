package allayplugins.dev.itarin.listeners.reward;

import allayplugins.dev.itarin.AllayDungeons;
import allayplugins.dev.itarin.dao.dungeon.DungeonDAO;
import allayplugins.dev.itarin.manager.dungeon.DungeonManager;
import lombok.val;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class RewardSaveListener implements Listener {

    private final DungeonManager dungeonManager;

    public RewardSaveListener(AllayDungeons main) {
        Bukkit.getPluginManager().registerEvents(this, main);

        dungeonManager = main.getDungeonManager();
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        val title = event.getView().getTitle();
        if (!title.startsWith("§8Editar Recompensas #")) return;

        val dungeonID = title.substring("§8Editar Recompensas #".length());
        val dungeon = DungeonDAO.findDungeonById(dungeonID);
        if (dungeon == null) return;

        val player = (Player) event.getPlayer();
        val inv = event.getInventory();

        val rewards = new ArrayList<ItemStack>();

        for (int i = 0; i < 26; i++) {
            val item = inv.getItem(i);
            if (item != null && item.getType() != Material.AIR) {
                rewards.add(item);
            }
        }

        dungeon.setRewards(rewards);
        dungeonManager.saveRewards(dungeon);

        player.sendMessage("§6[AllayDungeons] §a✔ Recompensas salvas com sucesso!");
    }
}