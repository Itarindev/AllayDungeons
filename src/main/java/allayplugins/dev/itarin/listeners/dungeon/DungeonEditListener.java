package allayplugins.dev.itarin.listeners.dungeon;

import allayplugins.dev.itarin.AllayDungeons;
import allayplugins.dev.itarin.dao.dungeon.DungeonDAO;
import allayplugins.dev.itarin.inventories.dungeon.DungeonEditorMenu;
import allayplugins.dev.itarin.inventories.phase.PhaseEditorMenu;
import allayplugins.dev.itarin.inventories.reward.DungeonRewardEditMenu;
import allayplugins.dev.itarin.manager.dungeon.DungeonManager;
import allayplugins.dev.itarin.utils.ChatAction;
import allayplugins.dev.itarin.utils.PlayerUtils;
import lombok.val;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class DungeonEditListener implements Listener {

    private final DungeonManager dungeonManager;

    private final ChatAction chatAction;

    public DungeonEditListener(AllayDungeons main) {
        Bukkit.getPluginManager().registerEvents(this, main);

        dungeonManager = main.getDungeonManager();

        chatAction = main.getChatAction();
    }

    @EventHandler
    void onCLick(InventoryClickEvent event) {
        val title = event.getView().getTitle();
        if (!title.startsWith("§8Editar Dungeon: §7#")) return;
        event.setCancelled(true);

        String[] parts = title.split("#");
        if (parts.length < 2) return;

        val dungeonID = parts[1];
        val dungeon = DungeonDAO.findDungeonById(dungeonID);
        if (dungeon == null) return;

        val slot = event.getSlot();

        val player = (Player)event.getWhoClicked();

        switch (slot) {
            case 10:
                player.closeInventory();
                player.sendMessage("§eDigite o novo nome no chat ou §ccancele §epara sair.");

                chatAction.addAction(player, (sender, message) -> {
                    if (message.equalsIgnoreCase("cancelar") || message.equalsIgnoreCase("cancel")) {
                        sender.sendMessage("§6[AllayDungeons] §c✘ Edição cancelada! §7Nenhuma modificação foi feita.");
                        new DungeonEditorMenu(dungeon).open(player);
                        return;
                    }

                    val finalMessage = message.replace("&", "§");

                    dungeon.setName(finalMessage);
                    sender.sendMessage("§6[AllayDungeons] §aNome da dungeon alterado para §f" + finalMessage);
                    new DungeonEditorMenu(dungeon).open(player);
                });
                break;
            case 11:
                new PhaseEditorMenu(dungeon).open(player);
                break;
            case 12:
                dungeon.setEntry(player.getLocation());
                new DungeonEditorMenu(dungeon).open(player);
                break;
            case 13:
                dungeon.setExit(player.getLocation());
                new DungeonEditorMenu(dungeon).open(player);
                break;
            case 14:
                player.closeInventory();
                player.sendMessage("§eDigite a quantidade no chat ou §ccancele §epara sair.");

                chatAction.addAction(player, (sender, message) -> {
                    if (message.equalsIgnoreCase("cancelar") || message.equalsIgnoreCase("cancel")) {
                        sender.sendMessage("§6[AllayDungeons] §c✘ Edição cancelada! §7Nenhuma modificação foi feita.");
                        new DungeonEditorMenu(dungeon).open(player);
                        return;
                    }

                    if (!NumberUtils.isNumber(message)) return;

                    val amount = Integer.parseInt(message);

                    dungeon.setMinPlayers(amount);
                    sender.sendMessage("§6[AllayDungeons] §a✔ Mínimo de jogadores da dungeon atualizado para: §f" + amount);
                    new DungeonEditorMenu(dungeon).open(player);
                });
                break;
            case 15:
                player.closeInventory();
                player.sendMessage("§eDigite a quantidade no chat ou §ccancele §epara sair.");

                chatAction.addAction(player, (sender, message) -> {
                    if (message.equalsIgnoreCase("cancelar") || message.equalsIgnoreCase("cancel")) {
                        sender.sendMessage("§6[AllayDungeons] §c✘ Edição cancelada! §7Nenhuma modificação foi feita.");
                        new DungeonEditorMenu(dungeon).open(player);
                        return;
                    }

                    if (!PlayerUtils.isNumber(player, message)) return;

                    val amount = Integer.parseInt(message);

                    dungeon.setMaxPlayers(amount);
                    sender.sendMessage("§6[AllayDungeons] §a✔ Limite máximo de jogadores atualizado para: §f" + amount);
                    new DungeonEditorMenu(dungeon).open(player);
                });
                break;
            case 16:
                dungeon.setEnabled(!dungeon.isEnabled());
                new DungeonEditorMenu(dungeon).open(player);
                break;
            case 22:
                new DungeonRewardEditMenu(dungeon).open(player);
                break;
            case 40:
                if (dungeon.getEntry() == null || dungeon.getExit() == null) {
                    player.sendMessage("§6[AllayDungeons] §c✘ A entrada e/ou saída da dungeon não estão definidas! Configure antes de continuar.");
                    return;
                }

                dungeonManager.save(dungeon);
                player.sendMessage("§6[AllayDungeons] §a✔ Dungeon editada e salva com sucesso!");
                break;
        }
    }
}