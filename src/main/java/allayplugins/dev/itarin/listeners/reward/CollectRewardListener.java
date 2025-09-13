package allayplugins.dev.itarin.listeners.reward;

import allayplugins.dev.itarin.AllayDungeons;
import allayplugins.dev.itarin.configuration.MessagesConfiguration;
import allayplugins.dev.itarin.dao.rewards.UserRewardsDAO;
import allayplugins.dev.itarin.manager.reward.RewardManager;
import allayplugins.dev.itarin.utils.PlayerUtils;
import lombok.val;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class CollectRewardListener implements Listener {

    private MessagesConfiguration messagesConfiguration;

    private final RewardManager rewardManager;

    public CollectRewardListener(AllayDungeons main) {
        Bukkit.getPluginManager().registerEvents(this, main);

        messagesConfiguration = main.getMessagesConfiguration();

        rewardManager = main.getRewardManager();
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        val title = event.getView().getTitle();
        if (!title.equalsIgnoreCase("§8Suas recompensas")) return;
        event.setCancelled(true);

        val player = (Player) event.getWhoClicked();
        val slot = event.getRawSlot();

        val user = UserRewardsDAO.findUserByName(player.getName());
        if (user == null) return;

        val inventory = event.getInventory();

        if (slot == 40) {
            for (int i = 10; i < 39; i++) {
                val item = inventory.getItem(i);
                if (item != null && item.getType() != Material.AIR) {
                    player.getInventory().addItem(item);
                    inventory.setItem(i, null);
                }
            }

            UserRewardsDAO.getUserRewards().remove(user);
            rewardManager.delete(user);
            player.closeInventory();
            for (val message : messagesConfiguration.getCollectedRewards())
                PlayerUtils.sendMessage(player, message);
        }
    }
}