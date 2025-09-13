package allayplugins.dev.itarin.manager.phase;

import allayplugins.dev.itarin.inventories.phase.NewPhaseMenu;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class NewPhaseMenuManager {

    private static final Map<Player, NewPhaseMenu> MENUS = new HashMap<>();

    public static void setMenu(Player player, NewPhaseMenu menu) {
        MENUS.put(player, menu);
    }

    public static NewPhaseMenu getMenu(Player player) {
        return MENUS.get(player);
    }

    public static void removeMenu(Player player) {
        MENUS.remove(player);
    }

    public static boolean hasMenu(Player player) {
        return MENUS.containsKey(player);
    }
}