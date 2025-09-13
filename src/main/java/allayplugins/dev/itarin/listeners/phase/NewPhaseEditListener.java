package allayplugins.dev.itarin.listeners.phase;

import allayplugins.dev.itarin.AllayDungeons;
import allayplugins.dev.itarin.inventories.phase.PhaseEditorMenu;
import allayplugins.dev.itarin.manager.phase.NewPhaseMenuManager;
import allayplugins.dev.itarin.utils.ChatAction;
import lombok.val;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class NewPhaseEditListener implements Listener {

    private final ChatAction chatAction;

    public NewPhaseEditListener(AllayDungeons main) {
        Bukkit.getPluginManager().registerEvents(this, main);

        chatAction = main.getChatAction();
    }

    @EventHandler
    void onEdit(InventoryClickEvent event) {
        val title = event.getView().getTitle();
        if (!title.equals("§8Criar Nova Fase")) return;
        event.setCancelled(true);

        val player = (Player) event.getWhoClicked();
        val slot = event.getSlot();

        val menu = NewPhaseMenuManager.getMenu(player);
        val tempPhase = menu.getTempPhase();

        switch (slot) {
            case 11:
                player.closeInventory();
                player.sendMessage("§aDigite o novo nome da fase no chat ou §ccancelar§a para cancelar.");
                chatAction.addAction(player, (p, message) -> {
                    if (message.equalsIgnoreCase("cancelar")) {
                        p.sendMessage("§c✘ Edição cancelada! §7Nenhuma modificação foi feita.");
                        return;
                    }

                    val finalMessage = message.replace("&", "§");

                    tempPhase.setName(finalMessage);
                    p.sendMessage("§6[AllayDungeons] §a✔ Nome definido como: §f" + finalMessage);
                    menu.open(player);
                });
                break;
            case 12:
                tempPhase.setMobs(nextMob(tempPhase.getMobs()));
                player.sendMessage("§6[AllayDungeons] §a✔ Mob principal definido como: §f" + tempPhase.getMobs().name());
                menu.open(player);
                break;

            case 13:
                tempPhase.setBoss(nextMob(tempPhase.getBoss()));
                player.sendMessage("§6[AllayDungeons] §a✔ Boss definido como: §f" + tempPhase.getBoss().name());
                menu.open(player);
                break;
            case 14:
                player.closeInventory();
                player.sendMessage("§aDigite a quantidade de mobs no chat ou §ccancelar§a.");
                chatAction.addAction(player, (p, message) -> {
                    if (message.equalsIgnoreCase("cancelar")) {
                        p.sendMessage("§6[AllayDungeons] §c✘ Edição cancelada! §7Nenhuma modificação foi feita.");
                        return;
                    }

                    if (!NumberUtils.isNumber(message)) return;

                    val amount = Integer.parseInt(message);

                    tempPhase.setAmount(amount);
                    player.sendMessage("§6[AllayDungeons] §a✔ A quantidade de mobs desta fase foi definida para: §f" + amount);
                    menu.open(player);
                });
                break;
            case 15:
                tempPhase.setHasBoss(!tempPhase.isHasBoss());
                menu.open(player);
                break;
            case 40:
                val validationError = tempPhase.validate();
                if (validationError == null) {
                    val dungeon = menu.getDungeon();
                    tempPhase.setId("phase_" + (dungeon.getPhases().size() + 1));
                    dungeon.getPhases().add(tempPhase);
                    player.sendMessage("§6[AllayDungeons] §a✅ Fase criada! §7Ela já está pronta para ser configurada.");
                    new PhaseEditorMenu(dungeon).open(player);
                } else {
                    player.sendMessage(validationError);
                    menu.open(player);
                }
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