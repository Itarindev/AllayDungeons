package allayplugins.dev.itarin.runnable;

import allayplugins.dev.itarin.dao.dungeon.DungeonDAO;
import allayplugins.dev.itarin.enuns.DungeonState;
import org.bukkit.scheduler.BukkitRunnable;

public class DungeonStartRunnable extends BukkitRunnable{

    @Override
    public void run() {
        DungeonDAO.getDungeons().forEach(dungeon -> {
            if (dungeon.getDungeonState() != DungeonState.TO_BE_EXPLORED) return;

            if (dungeon.getAllPlayers().size() < dungeon.getMinPlayers()) return;

            if (!dungeon.isEnabled()) return;

            dungeon.start();
        });
    }
}