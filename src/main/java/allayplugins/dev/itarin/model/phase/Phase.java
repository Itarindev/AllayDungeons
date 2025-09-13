package allayplugins.dev.itarin.model.phase;

import lombok.Data;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class Phase {

    private String id, name;
    private EntityType mobs, boss;
    private String bossName;
    private int amount;
    private boolean hasBoss;
    private double mobLife, bossLife;
    private List<Location> spawnLocations = new ArrayList<>();
    private List<String> rewardCommands = new ArrayList<>();

    private List<UUID> livingMobs = new ArrayList<>();
    private UUID bossUuid;

    public Phase(String id, String name, EntityType mobs, EntityType boss, String bossName, int amount, boolean hasBoss, double mobLife, double bossLife) {
        this.id = id;
        this.name = name;
        this.mobs = mobs;
        this.boss = boss;
        this.bossName = bossName;
        this.amount = amount;
        this.hasBoss = hasBoss;
        this.mobLife = mobLife;
        this.bossLife = bossLife;
    }

    public String validate() {
        if (name == null || name.isEmpty()) {
            return "§cVocê precisa definir o nome da fase.";
        }
        if (mobs == null) {
            return "§cVocê precisa escolher o mob principal.";
        }
        if (boss == null) {
            return "§cVocê precisa escolher o boss.";
        }
        if (amount <= 0) {
            return "§cA quantidade de mobs deve ser maior que 0.";
        }
        return null;
    }
}