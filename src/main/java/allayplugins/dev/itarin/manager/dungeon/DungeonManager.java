package allayplugins.dev.itarin.manager.dungeon;

import allayplugins.dev.itarin.AllayDungeons;
import allayplugins.dev.itarin.model.Dungeon;
import allayplugins.dev.itarin.utils.ItemStackUtils;
import allayplugins.dev.itarin.utils.LocationUtils;
import allayplugins.dev.itarin.utils.MultipleFiles;
import lombok.SneakyThrows;
import lombok.val;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

public class DungeonManager {

    protected AllayDungeons main;

    private final FileConfiguration configuration;

    public DungeonManager(AllayDungeons main) {
        this.main = main;

         configuration = MultipleFiles.getConfig(main, "dungeons");
    }

    @SneakyThrows
    public void save(Dungeon dungeon) {
        val path = "Dungeons." + dungeon.getId();

        configuration.set(path + ".Name", dungeon.getName());
        configuration.set(path + ".Entry", LocationUtils.serialize(dungeon.getEntry()));
        configuration.set(path + ".Exit", LocationUtils.serialize(dungeon.getExit()));
        configuration.set(path + ".Icon", dungeon.getIcon().getType().name());
        configuration.set(path + ".Max Players", dungeon.getMaxPlayers());
        configuration.set(path + ".Min Players", dungeon.getMinPlayers());
        configuration.set(path + ".Enabled", dungeon.isEnabled());

        configuration.save(new File(main.getDataFolder(), "dungeons.yml"));
    }

    @SneakyThrows
    public void saveRewards(Dungeon dungeon) {
        val path = "Dungeons." + dungeon.getId();

        val base64List = ItemStackUtils.listToBase64(dungeon.getRewards());
        configuration.set(path + ".Rewards", base64List);
        configuration.save(new File(main.getDataFolder(), "dungeons.yml"));
    }

    @SneakyThrows
    public void savePhaseWithDungeon(Dungeon dungeon) {
        val path = "Dungeons." + dungeon.getId() + ".Phases";

        for (val phase : dungeon.getPhases()) {
            val phasePath = path + "." + phase.getId();

            configuration.set(phasePath + ".Name", phase.getName());
            configuration.set(phasePath + ".Mobs", phase.getMobs().name());
            configuration.set(phasePath + ".Boss", phase.getBoss().name());
            configuration.set(phasePath + ".BossName", phase.getBossName());
            configuration.set(phasePath + ".HasBoss", phase.isHasBoss());
            configuration.set(phasePath + ".Boss-Life", phase.getBossLife());
            configuration.set(phasePath + ".Mob-Life", phase.getMobLife());
            configuration.set(phasePath + ".Amount", phase.getAmount());

            List<String> locs = phase.getSpawnLocations().stream()
                    .map(LocationUtils::serialize)
                    .collect(Collectors.toList());
            configuration.set(phasePath + ".SpawnLocations", locs);
        }

        configuration.save(new File(main.getDataFolder(), "dungeons.yml"));
    }
}