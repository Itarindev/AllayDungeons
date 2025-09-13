package allayplugins.dev.itarin.listeners.phase;

import allayplugins.dev.itarin.AllayDungeons;
import allayplugins.dev.itarin.inventories.phase.PhaseEditorMenu;
import allayplugins.dev.itarin.manager.dungeon.DungeonManager;
import allayplugins.dev.itarin.manager.phase.EditPhaseMenuManager;
import allayplugins.dev.itarin.utils.ChatAction;
import allayplugins.dev.itarin.utils.PlayerUtils;
import lombok.val;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class EditPhaseListener implements Listener {

    private final ChatAction chatAction;

    private final DungeonManager dungeonManager;

    public EditPhaseListener(AllayDungeons main) {
        Bukkit.getPluginManager().registerEvents(this, main);

        dungeonManager = main.getDungeonManager();

        chatAction = main.getChatAction();
    }

    @EventHandler
    void onEdit(InventoryClickEvent event) {
        val title = event.getView().getTitle();
        if (!title.equals("§8Editar Fase: §7")) return;
        event.setCancelled(true);

        val player = (Player) event.getWhoClicked();

        val menu = EditPhaseMenuManager.getMenu(player);
        if (menu == null) return;

        val phase = menu.getPhase();

        val slot = event.getSlot();

        switch (slot) {
            case 10:
                player.closeInventory();
                player.sendMessage("§aDigite o novo nome da fase no chat ou §ccancelar§a para cancelar.");
                chatAction.addAction(player, (p, message) -> {
                    if (message.equalsIgnoreCase("cancelar")) {
                        p.sendMessage("§6[AllayDungeons] §c✘ Edição cancelada! §7Nenhuma modificação foi feita.");
                        return;
                    }

                    val finalMessage = message.replace("&", "§");

                    phase.setName(finalMessage);
                    p.sendMessage("§6[AllayDungeons] §a✔ Nome definido como: §f" + finalMessage);
                    menu.open(player);
                });
                break;
            case 11:
                phase.setMobs(nextMob(phase.getMobs()));
                player.sendMessage("§6[AllayDungeons] §a✔ Mob principal definido como: §f" + phase.getMobs().name());
                menu.open(player);
                break;

            case 12:
                phase.setBoss(nextMob(phase.getBoss()));
                menu.open(player);
                player.sendMessage("§6[AllayDungeons] §a✔ Boss definido como: §f" + phase.getBoss().name());
                break;
            case 13:
                player.closeInventory();
                player.sendMessage("§aDigite a quantidade de mobs no chat ou §ccancelar§a.");
                chatAction.addAction(player, (p, message) -> {
                    if (message.equalsIgnoreCase("cancelar")) {
                        p.sendMessage("§6[AllayDungeons] §c✘ Edição cancelada! §7Nenhuma modificação foi feita.");
                        return;
                    }

                    if (!PlayerUtils.isNumber(player, message)) return;

                    val amount = Integer.parseInt(message);

                    phase.setAmount(amount);
                    player.sendMessage("§6[AllayDungeons] §a✔ A quantidade de mobs desta fase foi definida para: §f" + amount);
                    menu.open(player);
                });
                break;
            case 14:
                phase.setHasBoss(!phase.isHasBoss());
                menu.open(player);
                break;
            case 15:
                player.closeInventory();
                player.sendMessage("§aDigite o novo nome do boss no chat ou §ccancelar§a para cancelar.");
                chatAction.addAction(player, (sender, message) -> {
                    if (message.equalsIgnoreCase("cancelar")) {
                        sender.sendMessage("§6[AllayDungeons] §c✘ Edição cancelada! §7Nenhuma modificação foi feita.");
                        return;
                    }

                    val finalMessage = message.replace("&", "§");

                    phase.setBossName(finalMessage);
                    sender.sendMessage("§6[AllayDungeons] §a✔ Nome definido como: §f" + finalMessage);
                    menu.open(player);
                });
                break;
            case 16:
                phase.getSpawnLocations().add(player.getLocation());
                player.closeInventory();
                player.sendMessage("§6[AllayDungeons] §a✔ Local de spawn adicionado com sucesso!");
                break;
            case 21:
                player.closeInventory();
                player.sendMessage("§aDigite a vida dos mobs no chat ou §ccancelar§a para cancelar.");
                chatAction.addAction(player, (sender, message) -> {
                    if (message.equalsIgnoreCase("cancelar")) {
                        sender.sendMessage("§6[AllayDungeons] §c✘ Edição cancelada! §7Nenhuma modificação foi feita.");
                        return;
                    }

                    if (!PlayerUtils.isNumber(player, message)) return;

                    val life = Double.parseDouble(message);

                    phase.setMobLife(life);
                    sender.sendMessage("§6[AllayDungeons] §a✔ Vida do mob definida para: §f" + message);
                    menu.open(player);
                });
                break;
            case 23:
                player.closeInventory();
                player.sendMessage("§aDigite a vida do boss no chat ou §ccancelar§a para cancelar.");
                chatAction.addAction(player, (sender, message) -> {
                    if (message.equalsIgnoreCase("cancelar")) {
                        sender.sendMessage("§6[AllayDungeons] §c✘ Edição cancelada! §7Nenhuma modificação foi feita.");
                        return;
                    }

                    if (!PlayerUtils.isNumber(player, message)) return;

                    val life = Double.parseDouble(message);

                    phase.setBossLife(life);
                    sender.sendMessage("§6[AllayDungeons] §a✔ Vida do boss definida para: §f" + message);
                    menu.open(player);
                });
                break;
            case 45:
                new PhaseEditorMenu(menu.getDungeon()).open(player);
                break;
            case 40:
                if (phase.getSpawnLocations().isEmpty()) {
                    player.sendMessage("§6[AllayDungeons] §c✘ Você precisa adicionar pelo menos um local de spawn antes de salvar.");
                    return;
                }

                if (phase.getBossName() == null && phase.isHasBoss()) {
                    player.sendMessage("§6[AllayDungeons] §c✘ Você precisa definir o nome do boss antes de salvar.");
                    return;
                }

                if (phase.getBossLife() <= 0 && phase.isHasBoss()) {
                    player.sendMessage("§6[AllayDungeons] §c✘ Valores inválidos! A vida do boss precisa ser maior que zero.");
                    return;
                }

                if (phase.getMobLife() <= 0) {
                    player.sendMessage("§6[AllayDungeons] §c✘ Valores inválidos! A vida dos mobs precisa ser maior que zero.");
                    return;
                }

                dungeonManager.savePhaseWithDungeon(menu.getDungeon());
                player.closeInventory();
                player.sendMessage("§6[AllayDungeons] §a✔ Fase editada e salva com sucesso!");
                break;
        }
    }

    private static final List<EntityType> VALID_MOBS = Arrays.stream(EntityType.values())
            .filter(type -> type.isAlive() && type.isSpawnable())
            .collect(Collectors.toList());

    public static EntityType nextMob(EntityType current) {
        int index = VALID_MOBS.indexOf(current);
        if (index == -1 || index + 1 >= VALID_MOBS.size()) {
            return VALID_MOBS.get(0);
        }
        return VALID_MOBS.get(index + 1);
    }
}