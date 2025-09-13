package allayplugins.dev.itarin.listeners.dungeon;

import allayplugins.dev.itarin.AllayDungeons;
import allayplugins.dev.itarin.configuration.InventoryConfiguration;
import allayplugins.dev.itarin.configuration.MessagesConfiguration;
import allayplugins.dev.itarin.dao.dungeon.DungeonDAO;
import allayplugins.dev.itarin.utils.PlayerUtils;
import lombok.val;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class SelectDungeonListener implements Listener {

    private final MessagesConfiguration messagesConfiguration;

    private final InventoryConfiguration inventoryConfiguration;

    public SelectDungeonListener(AllayDungeons main) {
        Bukkit.getPluginManager().registerEvents(this, main);

        messagesConfiguration = main.getMessagesConfiguration();

        inventoryConfiguration = main.getInventoryConfiguration();
    }

    @EventHandler
    void onClick(InventoryClickEvent event) {
        val title = event.getView().getTitle();
        if (!title.equals(inventoryConfiguration.getDungeonInventoryName().replace("&", "§"))) return;
        event.setCancelled(true);

        val item = event.getCurrentItem();
        if (item == null || item.getType() == Material.AIR) return;

        val dungeon = DungeonDAO.findDungeonByIcon(item);
        if (dungeon == null) return;

        val player = (Player)event.getWhoClicked();

        if (dungeon.getAllPlayers().size() >= dungeon.getMaxPlayers()) {
            PlayerUtils.sendMessage(player, messagesConfiguration.getMaxLimit());
            return;
        }

        dungeon.getAllPlayers().add(player.getName());

        player.closeInventory();

        for (val message : messagesConfiguration.getInLine()) {
            PlayerUtils.sendMessage(player, message.replace("{dungeon_name}", dungeon.getName()));
        }
    }
}