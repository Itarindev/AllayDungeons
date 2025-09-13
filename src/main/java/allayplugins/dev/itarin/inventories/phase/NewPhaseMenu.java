package allayplugins.dev.itarin.inventories.phase;

import allayplugins.dev.itarin.manager.phase.NewPhaseMenuManager;
import allayplugins.dev.itarin.model.Dungeon;
import allayplugins.dev.itarin.model.phase.Phase;
import allayplugins.dev.itarin.utils.ItemBuilder;
import lombok.val;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class NewPhaseMenu {

    private final Dungeon dungeon;
    private final Phase tempPhase;

    public NewPhaseMenu(Dungeon dungeon) {
        this.dungeon = dungeon;
        this.tempPhase = new Phase("phase_1", "Nova Fase",  EntityType.ZOMBIE, EntityType.ZOMBIE, null, 0, false, 0.0, 0.0);
    }

    public void open(Player player) {
        val inventory = Bukkit.createInventory(null, 54, "§8Criar Nova Fase");

        val zombieHead = new ItemStack(Material.SKULL_ITEM, 1, (short) 2);
        val witherSkull = new ItemStack(Material.SKULL_ITEM, 1, (short) 1);

        inventory.setItem(11, new ItemBuilder(Material.NAME_TAG)
                .setName("§aDefinir Nome")
                .setLore("§7Nome atual: §f" + (tempPhase.getName() != null ? tempPhase.getName() : "§cNão definido"))
                .build());

        inventory.setItem(12, new ItemBuilder(zombieHead)
                .setName("§aEscolher Mob")
                .setLore("§7Mob atual: §f" + (tempPhase.getMobs() != null ? tempPhase.getMobs().name() : "§cNão definido"))
                .build());

        inventory.setItem(13, new ItemBuilder(witherSkull)
                .setName("§aEscolher Boss")
                .setLore("§7Boss atual: §f" + (tempPhase.getBoss() != null ? tempPhase.getBoss().name() : "§cNão definido"))
                .build());

        inventory.setItem(14, new ItemBuilder(Material.PAPER)
                .setName("§aDefinir Quantidade")
                .setLore("§7Quantidade atual: §f" + tempPhase.getAmount())
                .build());

        inventory.setItem(15, new ItemBuilder(tempPhase.isHasBoss() ? Material.NETHER_STAR : Material.BARRIER)
                .setName(tempPhase.isHasBoss() ? "§aBoss Ativado" : "§cBoss Desativado")
                .setLore("§7Clique para " + (tempPhase.isHasBoss() ? "§cdesativar" : "§aativar") + "§7 o boss.")
                .build());

        inventory.setItem(45, new ItemBuilder(Material.ARROW)
                .setName("§cVoltar")
                .build());

        inventory.setItem(40, new ItemBuilder(Material.EMERALD_BLOCK)
                .setName("§aConfirmar e Criar")
                .build());

        NewPhaseMenuManager.setMenu(player, this);
        player.openInventory(inventory);
    }

    public Phase getTempPhase() {
        return tempPhase;
    }

    public Dungeon getDungeon() {
        return dungeon;
    }
}