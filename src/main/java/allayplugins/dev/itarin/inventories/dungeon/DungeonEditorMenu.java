package allayplugins.dev.itarin.inventories.dungeon;

import allayplugins.dev.itarin.model.Dungeon;
import lombok.val;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import allayplugins.dev.itarin.utils.ItemBuilder;

public class DungeonEditorMenu {

    private final Dungeon dungeon;

    public DungeonEditorMenu(Dungeon dungeon) {
        this.dungeon = dungeon;
    }

    public void open(Player player) {
        val inventory = Bukkit.createInventory(null, 54, "§8Editar Dungeon: §7#" + dungeon.getId());

        inventory.setItem(10, new ItemBuilder(Material.NAME_TAG)
                .setName("§aDefinir Nome")
                .setLore("§7Nome atual: §f" + dungeon.getName())
                .build());

        inventory.setItem(11, new ItemBuilder(Material.BOOK)
                .setName("§aEditar Fases")
                .setLore("§7Clique para editar as fases da dungeon")
                .build());

        inventory.setItem(12, new ItemBuilder(Material.ENDER_PORTAL_FRAME)
                .setName("§aDefinir Entrada")
                .setLore("§7Local atual: §f" + locationToString(dungeon.getEntry()))
                .build());

        inventory.setItem(13, new ItemBuilder(Material.EYE_OF_ENDER)
                .setName("§aDefinir Saída")
                .setLore("§7Local atual: §f" + locationToString(dungeon.getExit()))
                .build());

        inventory.setItem(14, new ItemBuilder(Material.LEASH)
                .setName("§aDefinir Minimo de Jogadores")
                .setLore("§7Min players: §f" + dungeon.getMinPlayers())
                .build());

        inventory.setItem(15, new ItemBuilder(Material.LEASH)
                .setName("§aDefinir Máximo de Jogadores")
                .setLore("§7Max players: §f" + dungeon.getMaxPlayers())
                .build());

        inventory.setItem(16, new ItemBuilder(Material.LEVER)
                .setName(dungeon.isEnabled() ? "§aDungeon Habilitada" : "§cDungeon Desabilitada")
                .setLore("§7Clique para alternar o status")
                .build());

        inventory.setItem(22, new ItemBuilder(Material.CHEST)
                .setName("§6Recompensas da Dungeon")
                .setLore(
                        "§7Clique aqui para editar as",
                        "§7recompensas que serão entregues",
                        "§7ao jogador que completar a dungeon.",
                        "",
                        "§eVocê poderá adicionar ou remover",
                        "§eitens conforme desejar!"
                )
                .build());

        inventory.setItem(40, new ItemBuilder(Material.EMERALD_BLOCK)
                .setName("§aSalvar Dungeon")
                .build());

        player.openInventory(inventory);
    }

    private String locationToString(Location location) {
        if (location == null) return "§cNão definido";
        return "X: " + location.getBlockX() + " Y: " + location.getBlockY() + " Z: " + location.getBlockZ();
    }
}