package allayplugins.dev.itarin.setup;

import allayplugins.dev.itarin.AllayDungeons;
import allayplugins.dev.itarin.dao.dungeon.DungeonDAO;
import allayplugins.dev.itarin.enuns.DungeonState;
import allayplugins.dev.itarin.model.Dungeon;
import allayplugins.dev.itarin.model.phase.Phase;
import allayplugins.dev.itarin.utils.ItemBuilder;
import allayplugins.dev.itarin.utils.ItemStackUtils;
import allayplugins.dev.itarin.utils.LocationUtils;
import allayplugins.dev.itarin.utils.MultipleFiles;
import lombok.val;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DungeonSetup {

    protected AllayDungeons main;

    private final FileConfiguration configuration;

    public DungeonSetup(AllayDungeons main) {
        this.main = main;

        configuration = MultipleFiles.getConfig(main, "dungeons");
        loadDungeons();
    }

    private void loadDungeons() {
        val section = configuration.getConfigurationSection("Dungeons");
        if (section == null) return;

        for (val path : section.getKeys(false)) {
            val key = configuration.getConfigurationSection("Dungeons." + path);
            if (key == null) continue;

            val name = key.getString("Name").replace("&", "§");

            val entry = LocationUtils.deserialize(key.getString("Entry"));
            val exit = LocationUtils.deserialize(key.getString("Exit"));

            val item = Material.getMaterial(key.getString("Icon"));

            val icon = new ItemBuilder(item).build();

            val maxPlayers = key.getInt("Max Players");
            val minPlayers = key.getInt("Min Players");
            val enabled = key.getBoolean("Enabled");

            String base64 = key.getString("Rewards");
            List<ItemStack> rewards = (base64 != null) ? ItemStackUtils.listFromBase64(base64) : new ArrayList<>();

            val phases = new ArrayList<Phase>();
            val phasesSection = key.getConfigurationSection("Phases");
            if (phasesSection != null) {
                for (val phaseId : phasesSection.getKeys(false)) {
                    val phaseKey = phasesSection.getConfigurationSection(phaseId);
                    if (phaseKey == null) continue;

                    val phaseName = phaseKey.getString("Name").replace("&", "§");
                    val mob = EntityType.valueOf(phaseKey.getString("Mobs"));
                    val mobLife = phaseKey.getDouble("Mob-Life");
                    val boss = EntityType.valueOf(phaseKey.getString("Boss"));
                    val bossName = phaseKey.getString("BossName");
                    val bossLife = phaseKey.getDouble("Boss-Life");
                    val hasBoss = phaseKey.getBoolean("HasBoss");
                    val amount = phaseKey.getInt("Amount");

                    val phase = new Phase(phaseId, phaseName, mob, boss, bossName, amount, hasBoss, mobLife, bossLife);

                    List<String> locStrings = phaseKey.getStringList("SpawnLocations");
                    List<Location> locs = locStrings.stream()
                            .map(LocationUtils::deserialize)
                            .collect(Collectors.toList());
                    phase.setSpawnLocations(locs);
                    phases.add(phase);
                }
            }

            val dungeon = new Dungeon(path, name, icon, entry, exit, phases, minPlayers, maxPlayers, enabled, rewards);
            dungeon.setDungeonState(DungeonState.TO_BE_EXPLORED);
            dungeon.setScoreboardManager(main.getScoreboardManager());
            DungeonDAO.getDungeons().add(dungeon);
        }
    }
}