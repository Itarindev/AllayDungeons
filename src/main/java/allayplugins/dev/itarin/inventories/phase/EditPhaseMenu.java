package allayplugins.dev.itarin.inventories.phase;

import allayplugins.dev.itarin.manager.phase.EditPhaseMenuManager;
import allayplugins.dev.itarin.model.Dungeon;
import allayplugins.dev.itarin.model.phase.Phase;
import allayplugins.dev.itarin.utils.ItemBuilder;
import lombok.AllArgsConstructor;
import lombok.val;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

@AllArgsConstructor
public class EditPhaseMenu {

    private final Dungeon dungeon;
    private final Phase phase;

    public void open(Player player) {
        val inventory = Bukkit.createInventory(null, 54, "§8Editar Fase: §7");

        val zombieHead = new ItemStack(Material.SKULL_ITEM, 1, (short) 2);
        val witherSkull = new ItemStack(Material.SKULL_ITEM, 1, (short) 1);

        inventory.setItem(10, new ItemBuilder(Material.NAME_TAG)
                .setName("§aEditar Nome")
                .setLore("§7Nome atual: §f" + (phase.getName() != null ? phase.getName() : "§cNão definido"))
                .build());

        inventory.setItem(11, new ItemBuilder(zombieHead)
                .setName("§aEditar Mob")
                .setLore("§7Mob atual: §f" + phase.getMobs().name())
                .build());

        inventory.setItem(12, new ItemBuilder(witherSkull)
                .setName("§aEditar Boss")
                .setLore("§7Boss atual: §f" + phase.getBoss().name())
                .build());

        inventory.setItem(13, new ItemBuilder(Material.PAPER)
                .setName("§aEditar Quantidade")
                .setLore("§7Quantidade atual: §f" + phase.getAmount())
                .build());

        inventory.setItem(14, new ItemBuilder(phase.isHasBoss() ? Material.NETHER_STAR : Material.BARRIER)
                .setName(phase.isHasBoss() ? "§aBoss Ativado" : "§cBoss Desativado")
                .setLore("§7Clique para " + (phase.isHasBoss() ? "§cdesativar" : "§aativar"))
                .build());

        inventory.setItem(15, new ItemBuilder(Material.NAME_TAG)
                .setName("§aEditar Nome do Boss")
                .setLore("§7Nome atual: §f" + (phase.getBossName() != null ? phase.getBossName() : "§cNão definido"))
                .build());

        inventory.setItem(16, new ItemBuilder(Material.BLAZE_ROD)
                .setName("§aEditar Locais de Spawn")
                .setLore(
                        "§7Total de locais: §f" + (phase.getSpawnLocations() != null ? phase.getSpawnLocations().size() : 0),
                        "§7Clique para adicionar sua localização"
                )
                .build());

        inventory.setItem(21, new ItemBuilder(Material.REDSTONE)
                .setName("§aDefinir Vida dos Mobs")
                .setLore("§7Clique para alterar a quantidade de vida dos mobs comuns.")
                .build());

        inventory.setItem(23, new ItemBuilder(Material.NETHER_STAR)
                .setName("§aDefinir Vida do Boss")
                .setLore("§7Clique para alterar a quantidade de vida do boss desta fase.")
                .build());

        inventory.setItem(40, new ItemBuilder(Material.EMERALD_BLOCK)
                .setName("§aSalvar Alterações")
                .build());

        inventory.setItem(45, new ItemBuilder(Material.ARROW)
                .setName("§cVoltar")
                .build());

        EditPhaseMenuManager.setMenu(player, this);
        player.openInventory(inventory);
    }

    public Phase getPhase() { return phase; }

    public Dungeon getDungeon() { return dungeon; }
}
