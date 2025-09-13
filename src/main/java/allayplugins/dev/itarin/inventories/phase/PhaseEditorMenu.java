package allayplugins.dev.itarin.inventories.phase;

import allayplugins.dev.itarin.model.Dungeon;
import lombok.val;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import allayplugins.dev.itarin.utils.ItemBuilder;

public class PhaseEditorMenu {

    private final Dungeon dungeon;

    public PhaseEditorMenu(Dungeon dungeon) {
        this.dungeon = dungeon;
    }
    public void open(Player player) {
        val inventory = Bukkit.createInventory(null, 45, "§8Editar Fases: §7#" + dungeon.getId());

        int startSlot = 10;
        int maxSlotForPhases = 34;

        int i = 0;
        for (val phase : dungeon.getPhases()) {
            int slot = startSlot + i;
            if (slot > maxSlotForPhases) break;

            inventory.setItem(slot, new ItemBuilder(Material.BOOK)
                    .setName("§a" + phase.getName())
                    .setLore(
                            "§7ID: §f" + phase.getId(),
                            "§7Mobs: §f" + (phase.getMobs() != null ? phase.getMobs().name() : "§cNão definido"),
                            "§7Boss: §f" + (phase.getBoss() != null ? phase.getBoss().name() : "§cNão definido"),
                            "§7Quantidade: §f" + phase.getAmount(),
                            "",
                            "§eClique para editar"
                    ).build());

            i++;
        }

        inventory.setItem(36, new ItemBuilder(Material.ARROW)
                .setName("§cVoltar")
                .setLore("§7Voltar ao menu anterior")
                .build());

        inventory.setItem(40, new ItemBuilder(Material.EMERALD_BLOCK)
                .setName("§aCriar Nova Fase")
                .setLore("§7Clique para adicionar uma nova fase")
                .build());

        player.openInventory(inventory);
    }
}