package allayplugins.dev.itarin.manager.scoreboard;

import allayplugins.dev.itarin.AllayDungeons;
import allayplugins.dev.itarin.model.Dungeon;
import allayplugins.dev.itarin.utils.MultipleFiles;
import lombok.val;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;

import java.util.List;

public class ScoreboardManager {

    private final FileConfiguration configuration;

    public ScoreboardManager(AllayDungeons main) {

        configuration = MultipleFiles.getConfig(main, "scoreboard");
    }

    public void updateScoreboard(Player player, Dungeon dungeon) {
        val sb = Bukkit.getScoreboardManager().getNewScoreboard();
        val objective = sb.registerNewObjective("dungeon", "dummy");
        String title = ChatColor.translateAlternateColorCodes('&',
                configuration.getString("Scoreboard.Title", "&6&lDungeon"));

        objective.setDisplayName(title);
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        List<String> lines = configuration.getStringList("Scoreboard.Lines");
        int index = lines.size();

        val currentPhase = dungeon.getCurrentPhase();
        int aliveMobs = currentPhase != null && currentPhase.getLivingMobs() != null
                ? currentPhase.getLivingMobs().size() : 0;
        int totalMobs = currentPhase != null ? currentPhase.getAmount() : 0;

        int mobsKilledByPlayer = dungeon.getKilledMobs().getOrDefault(player, 0);

        for (String rawLine : lines) {
            String line = ChatColor.translateAlternateColorCodes('&', rawLine)
                    .replace("{dungeon_name}", dungeon.getName())
                    .replace("{current_phase}", String.valueOf(dungeon.getCurrentPhaseIndex() + 1))
                    .replace("{total_phases}", String.valueOf(dungeon.getPhases().size()))
                    .replace("{players_alive}", String.valueOf(dungeon.getAllPlayers().size()))
                    .replace("{alive_mobs}", String.valueOf(aliveMobs))
                    .replace("{total_mobs}", String.valueOf(totalMobs))
                    .replace("{killed_mobs}", String.valueOf(mobsKilledByPlayer))
                    .replace("{dungeon_state}", dungeon.getDungeonState().getDisplayName());
            objective.getScore(line).setScore(index--);
        }

        player.setScoreboard(sb);
    }
}