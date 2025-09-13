package allayplugins.dev.itarin.manager.phase;

import allayplugins.dev.itarin.inventories.phase.EditPhaseMenu;
import org.bukkit.entity.Player;
import java.util.HashMap;
import java.util.Map;

public class EditPhaseMenuManager {
    private static final Map<Player, EditPhaseMenu> MENUS = new HashMap<>();

    public static void setMenu(Player player, EditPhaseMenu menu) {
        MENUS.put(player, menu);
    }

    public static EditPhaseMenu getMenu(Player player) {
        return MENUS.get(player);
    }

    public static void removeMenu(Player player) {
        MENUS.remove(player);
    }
}