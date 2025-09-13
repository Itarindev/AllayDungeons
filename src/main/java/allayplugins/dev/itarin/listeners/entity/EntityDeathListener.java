package allayplugins.dev.itarin.listeners.entity;

import allayplugins.dev.itarin.AllayDungeons;
import allayplugins.dev.itarin.configuration.MessagesConfiguration;
import allayplugins.dev.itarin.dao.dungeon.DungeonDAO;
import allayplugins.dev.itarin.manager.scoreboard.ScoreboardManager;
import allayplugins.dev.itarin.model.Dungeon;
import allayplugins.dev.itarin.utils.PlayerUtils;
import allayplugins.dev.itarin.utils.TitleUtils;
import lombok.val;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.Objects;

public class EntityDeathListener implements Listener {

    private final AllayDungeons main;

    private final MessagesConfiguration messagesConfiguration;

    private final ScoreboardManager scoreboardManager;

    public EntityDeathListener(AllayDungeons main) {
        this.main = main;

        Bukkit.getPluginManager().registerEvents(this, main);

        messagesConfiguration = main.getMessagesConfiguration();

        scoreboardManager = main.getScoreboardManager();
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        val uuid = event.getEntity().getUniqueId();
        val killer = event.getEntity().getKiller();
        if (killer == null) return;

        DungeonDAO.getDungeons().forEach(dungeon -> {
            val lastPhase = dungeon.getCurrentPhaseIndex() == dungeon.getPhases().size() - 1;

            dungeon.getPhases().forEach(phase -> {
                if (phase.getLivingMobs().remove(uuid)) {
                    updateKilledMobsAndScoreboard(dungeon, killer);

                    if (!phase.getLivingMobs().isEmpty()) return;

                    if (phase.isHasBoss()) {
                        dungeon.spawnBoss(phase);
                        messagesConfiguration.getBossSpawned().forEach(msg ->
                                PlayerUtils.sendMessage(killer, msg.replace("{phase_boss_name}", phase.getBossName()))
                        );

                    } else if (lastPhase) {
                        finishDungeon(dungeon);

                    } else {
                        goToNextPhaseWithDelay(dungeon, killer);
                    }

                    return;
                }

                if (phase.isHasBoss() && uuid.equals(phase.getBossUuid())) {
                    if (lastPhase) {
                        finishDungeon(dungeon);
                    } else {
                        PlayerUtils.sendMessage(killer, messagesConfiguration.getBossKilled()
                                .replace("{phase_boss_name}", phase.getBossName())
                                .replace("{dungeon_phase_name}", phase.getName()));
                        goToNextPhaseWithDelay(dungeon, killer);
                    }
                }
            });
        });
    }

    private void updateKilledMobsAndScoreboard(Dungeon dungeon, Player killer) {
        val current = dungeon.getKilledMobs().getOrDefault(killer, 0);
        dungeon.getKilledMobs().put(killer, current + 1);
        scoreboardManager.updateScoreboard(killer, dungeon);
    }

    private void goToNextPhaseWithDelay(Dungeon dungeon, Player player) {
        val phaseName = dungeon.getNextPhaseName();

        if (phaseName != null) {
            for (String message : messagesConfiguration.getNextPhase()) {
                PlayerUtils.sendMessage(player, message.replace("{dungeon_phase_name}", phaseName));
            }
        }

        Bukkit.getScheduler().runTaskLater(main, dungeon::nextPhase, 20L * 30);
    }

    private void finishDungeon(Dungeon dungeon) {
        dungeon.getAllPlayers().stream()
                .map(Bukkit::getPlayer)
                .filter(Objects::nonNull)
                .filter(Player::isOnline)
                .forEach(player -> {
                    messagesConfiguration.getCompletedMessage().forEach(message ->
                            PlayerUtils.sendMessage(player, message)
                    );

                    TitleUtils.sendTitle(
                            player,
                            messagesConfiguration.getCompletedTitle(),
                            messagesConfiguration.getCompletedSubTitle(),
                            10, 70, 20
                    );
                });

        dungeon.end();
    }
}