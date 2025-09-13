package allayplugins.dev.itarin.listeners.entity;

import allayplugins.dev.itarin.AllayDungeons;
import allayplugins.dev.itarin.dao.dungeon.DungeonDAO;
import allayplugins.dev.itarin.utils.PlayerUtils;
import lombok.val;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class EntityDamageListener implements Listener {

    private final AllayDungeons main;

    public EntityDamageListener(AllayDungeons main) {
        this.main = main;

        Bukkit.getPluginManager().registerEvents(this, main);
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof LivingEntity)) return;
        val entity = (LivingEntity) event.getEntity();

        if (!(event.getDamager() instanceof Player)) return;
        val player = (Player) event.getDamager();

        val dungeon = DungeonDAO.getDungeonWherePlayerIs(player.getName());
        if (dungeon == null) return;

        val phase = dungeon.getCurrentPhase();
        if (phase == null) return;

        Bukkit.getScheduler().runTaskLater(main, () -> {
            if (entity.isDead()) return;

            if (!phase.getLivingMobs().contains(entity.getUniqueId())) return;

            String name;
            if (phase.getBossUuid() != null && phase.getBossUuid().equals(entity.getUniqueId())) {
                name = "§6" + (phase.getBossName() != null ? phase.getBossName() : "Boss");
            } else {
                name = "§c" + phase.getMobs().name();
            }

            val message = name + " §7- §a❤ " + (int) entity.getHealth() + "§7/§c" + (int) entity.getMaxHealth();

            PlayerUtils.sendActionBar(player, message);
        }, 1L);
    }
}