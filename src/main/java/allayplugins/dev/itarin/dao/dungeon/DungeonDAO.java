package allayplugins.dev.itarin.dao.dungeon;

import allayplugins.dev.itarin.model.Dungeon;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class DungeonDAO {

    @Getter
    private static final List<Dungeon> dungeons = new ArrayList<>();

    public static Dungeon findDungeonById(String dungeonId) {
        return dungeons.stream().filter(dungeon -> dungeon.getId().equalsIgnoreCase(dungeonId)).findFirst().orElse(null);
    }

    public static Dungeon getDungeonWherePlayerIs(String playerName) {
        return dungeons.stream()
                .filter(dungeon -> dungeon.getAllPlayers().contains(playerName))
                .findFirst()
                .orElse(null);
    }

    public static Dungeon findDungeonByIcon(ItemStack icon) {
        return dungeons.stream().filter(dungeon -> dungeon.getIcon().isSimilar(icon)).findFirst().orElse(null);
    }
}