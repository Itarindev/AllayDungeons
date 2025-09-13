package allayplugins.dev.itarin.model;

import allayplugins.dev.itarin.AllayDungeons;
import allayplugins.dev.itarin.configuration.MessagesConfiguration;
import allayplugins.dev.itarin.dao.rewards.UserRewardsDAO;
import allayplugins.dev.itarin.enuns.DungeonState;
import allayplugins.dev.itarin.manager.reward.RewardManager;
import allayplugins.dev.itarin.manager.scoreboard.ScoreboardManager;
import allayplugins.dev.itarin.model.phase.Phase;
import allayplugins.dev.itarin.model.user.UserReward;
import allayplugins.dev.itarin.utils.PlayerUtils;
import allayplugins.dev.itarin.utils.TitleUtils;
import lombok.Data;
import lombok.val;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.ItemStack;

import java.util.*;

@Data
public class Dungeon {

    private String id, name;
    private ItemStack icon;
    private Location entry, exit;
    private List<Phase> phases;
    private int minPlayers, maxPlayers;
    private boolean enabled;
    private List<ItemStack> rewards;

    private List<String> allPlayers = new ArrayList<>();
    private Map<Player, Integer> killedMobs = new HashMap<>();
    private DungeonState dungeonState;
    private int currentPhaseIndex = 0;

    private final MessagesConfiguration messagesConfiguration = AllayDungeons.getInstance().getMessagesConfiguration();

    private RewardManager rewardManager = AllayDungeons.getInstance().getRewardManager();
    private ScoreboardManager scoreboardManager;

    public Dungeon(String id, String name, ItemStack icon, Location entry, Location exit, List<Phase> phases, int minPlayers, int maxPlayers, boolean enabled, List<ItemStack> rewards) {
        this.id = id;
        this.name = name;
        this.icon = icon;
        this.entry = entry;
        this.exit = exit;
        this.phases = phases;
        this.maxPlayers = maxPlayers;
        this.minPlayers = minPlayers;
        this.enabled = enabled;
        this.rewards = rewards;
    }

    public void start() {
        setDungeonState(DungeonState.IN_EXPLORATION);

        allPlayers.forEach(playerName -> {
            val player = Bukkit.getPlayer(playerName);

            if (player != null) {
                player.teleport(entry);
                player.setGameMode(GameMode.SURVIVAL);
                TitleUtils.sendTitle(player, messagesConfiguration.getStartingTitle(), messagesConfiguration.getStartingSubTitle(), 10, 70, 20);
                for (val message : messagesConfiguration.getStartingMessage())
                    PlayerUtils.sendMessage(player, message);
            }
        });

        Bukkit.getScheduler().runTaskLater(AllayDungeons.getInstance(), () -> {
            if (entry == null || exit == null) return;

            if (phases.isEmpty()) return;

            currentPhaseIndex = 0;

            startPhase(phases.get(currentPhaseIndex));

            allPlayers.forEach(playerAlives -> {
                val player = Bukkit.getPlayer(playerAlives);

                if (player != null) {
                    scoreboardManager.updateScoreboard(player, this);

                    TitleUtils.sendTitle(player, messagesConfiguration.getStartedTitle(), messagesConfiguration.getStartedSubTitle(), 10, 70, 20);
                    for (val message : messagesConfiguration.getStartedMessage()) {
                        PlayerUtils.sendMessage(player, message);
                    }
                }
            });


        }, 20L * 30);
    }

    private void startPhase(Phase phase) {
        val livingMobs = new ArrayList<UUID>();

        val spawnLocations = phase.getSpawnLocations();
        val totalMobs = phase.getAmount();

        if (spawnLocations == null || spawnLocations.isEmpty()) return;

        val baseAmountPerLocation = totalMobs / spawnLocations.size();
        val remainder = totalMobs % spawnLocations.size();

        for (int i = 0; i < spawnLocations.size(); i++) {
            int mobsToSpawnHere = baseAmountPerLocation;
            if (i < remainder) {
                mobsToSpawnHere++;
            }

            val loc = spawnLocations.get(i);
            for (int j = 0; j < mobsToSpawnHere; j++) {
                val entity = loc.getWorld().spawnEntity(loc, phase.getMobs());
                if (entity instanceof LivingEntity) {
                    val living = (LivingEntity) entity;
                    living.setMaxHealth(phase.getMobLife());
                    living.setHealth(phase.getMobLife());
                    living.setCustomNameVisible(true);
                    living.setCustomName("§c" + phase.getMobs().name());

                    if (entity instanceof Zombie || entity instanceof Skeleton) {
                        living.getEquipment().setHelmet(new ItemStack(Material.LEATHER_HELMET));
                    }
                }
                livingMobs.add(entity.getUniqueId());
            }
        }

        phase.setLivingMobs(livingMobs);
    }

    public void nextPhase() {
        currentPhaseIndex++;

        val nextPhase = phases.get(currentPhaseIndex);

        allPlayers.forEach(playerName -> {
            val player = Bukkit.getPlayer(playerName);
            if (player != null) {
                scoreboardManager.updateScoreboard(player, this);
            }
        });

        startPhase(nextPhase);
    }

    public String getNextPhaseName() {
        int nextIndex = currentPhaseIndex + 1;
        if (nextIndex >= phases.size()) return null;
        return phases.get(nextIndex).getName();
    }

    public void end() {
        if (exit == null) return;

        setDungeonState(DungeonState.TO_BE_EXPLORED);

        allPlayers.forEach(playerAlives -> {
            val player = Bukkit.getPlayer(playerAlives);

            player.teleport(exit);
            player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());

            if (rewards == null || rewards.isEmpty()) return;

            val userReward = new UserReward(player.getUniqueId(), player.getName(), this, rewards);
            UserRewardsDAO.getUserRewards().add(userReward);

            rewardManager.insert(player, id, rewards);
        });

        for (val phase : phases) {
            if (phase.getLivingMobs() != null)
                phase.getLivingMobs().clear();
            phase.setBossUuid(null);
        }

        allPlayers.clear();
        killedMobs.clear();
    }

    public void stop() {
        if (exit == null) return;

        setDungeonState(DungeonState.TO_BE_EXPLORED);

        allPlayers.forEach(playerAlives -> {
            val player = Bukkit.getPlayer(playerAlives);

            player.teleport(exit);
            player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
        });

        for (val phase : phases) {
            if (phase.getLivingMobs() != null)
                phase.getLivingMobs().clear();
            phase.setBossUuid(null);
        }

        allPlayers.clear();
        killedMobs.clear();
    }

    public void spawnBoss(Phase phase) {
        if (!phase.isHasBoss()) return;

        if (phase.getSpawnLocations() == null || phase.getSpawnLocations().isEmpty()) return;

        if (phase.getBoss() == null) return;

        val firstLoc = phase.getSpawnLocations().get(0);
        val bossEntity = firstLoc.getWorld().spawnEntity(firstLoc, phase.getBoss());

        if (bossEntity instanceof LivingEntity) {
            val living = (LivingEntity) bossEntity;
            living.setMaxHealth(phase.getBossLife());
            living.setHealth(phase.getBossLife());
            living.setCustomNameVisible(true);
            living.setCustomName(phase.getBossName() != null ? phase.getBossName() : "§cNome não definido");
        }
        phase.setBossUuid(bossEntity.getUniqueId());
    }

    public Phase getCurrentPhase() {
        if (phases == null || phases.isEmpty()) return null;
        if (currentPhaseIndex < 0 || currentPhaseIndex >= phases.size()) return null;
        return phases.get(currentPhaseIndex);
    }
}