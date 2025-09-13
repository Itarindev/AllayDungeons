package allayplugins.dev.itarin.listeners.phase;

import allayplugins.dev.itarin.AllayDungeons;
import allayplugins.dev.itarin.dao.dungeon.DungeonDAO;
import allayplugins.dev.itarin.inventories.dungeon.DungeonEditorMenu;
import allayplugins.dev.itarin.inventories.phase.EditPhaseMenu;
import allayplugins.dev.itarin.inventories.phase.NewPhaseMenu;
import lombok.val;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class PhaseEditListener implements Listener {

    public PhaseEditListener(AllayDungeons main) {
        Bukkit.getPluginManager().registerEvents(this, main);
    }

    @EventHandler
    void onClick(InventoryClickEvent event) {
        val title = event.getView().getTitle();
        if (!title.startsWith("§8Editar Fases: §7#")) return;
        event.setCancelled(true);

        String[] parts = title.split("#");
        if (parts.length < 2) return;

        val dungeonID = parts[1];
        val dungeon = DungeonDAO.findDungeonById(dungeonID);
        if (dungeon == null) return;

        val slot = event.getSlot();
        val player = (Player) event.getWhoClicked();

        if (slot >= 10 && slot < 10 + dungeon.getPhases().size()) {
            val clickedPhase = dungeon.getPhases().get(slot - 10);
            new EditPhaseMenu(dungeon, clickedPhase).open(player);
        } else if (slot == 40) {
            new NewPhaseMenu(dungeon).open(player);
        } else if (slot == 36) {
            new DungeonEditorMenu(dungeon).open(player);
        }
    }
}