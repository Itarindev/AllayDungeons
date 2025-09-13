package allayplugins.dev.itarin.manager.reward;

import allayplugins.dev.itarin.connections.model.IDatabase;
import allayplugins.dev.itarin.model.user.UserReward;
import allayplugins.dev.itarin.utils.ItemStackUtils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class RewardManager {

    private final IDatabase iDatabase;

    public RewardManager(IDatabase iDatabase) {
        this.iDatabase = iDatabase;
    }

    public void insert(Player player, String dungeonId, List<ItemStack> rewards) {
        iDatabase.executeUpdate("INSERT INTO allaydungeons_users_rewards (user_uuid, user_name, dungeon_id, rewards) VALUES (?,?,?,?)",
                player.getUniqueId().toString(),
                player.getName(),
                dungeonId,
                ItemStackUtils.listToBase64(rewards));
    }

    public void delete(UserReward userReward) {
        iDatabase.executeUpdate("DELETE FROM allaydungeons_users_rewards WHERE user_uuid = ?", userReward.getUserUuid().toString());
    }
}