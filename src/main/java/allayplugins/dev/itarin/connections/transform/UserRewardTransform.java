package allayplugins.dev.itarin.connections.transform;

import allayplugins.dev.itarin.AllayDungeons;
import allayplugins.dev.itarin.connections.model.IDatabase;
import allayplugins.dev.itarin.dao.dungeon.DungeonDAO;
import allayplugins.dev.itarin.dao.rewards.UserRewardsDAO;
import allayplugins.dev.itarin.model.user.UserReward;
import allayplugins.dev.itarin.utils.ItemStackUtils;
import lombok.val;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class UserRewardTransform {

    protected AllayDungeons main;

    private final IDatabase iDatabase;

    public UserRewardTransform(AllayDungeons main) {
        this.main = main;

        iDatabase = main.getIDatabase();
    }

    public UserReward userRewardTransform(ResultSet rs) throws SQLException {
        val uuid = UUID.fromString(rs.getString("user_uuid"));
        val userName = rs.getString("user_name");
        val dungeon = DungeonDAO.findDungeonById(rs.getString("dungeon_id"));
        val rewards = ItemStackUtils.listFromBase64(rs.getString("rewards"));

        return new UserReward(uuid, userName, dungeon, rewards);
    }

    public void loadUserRewards() {
        try (val ps = iDatabase.getConnection().prepareStatement("SELECT * FROM `allaydungeons_users_rewards`")) {
            val rs = ps.executeQuery();

            while (rs.next()) {
                val userReward = userRewardTransform(rs);
                if (userReward == null) continue;

                UserRewardsDAO.getUserRewards().add(userReward);
                System.out.println("[AllayDungeons] Recompensa carregada: jogador=" + userReward.getUserName() + " itens=" + userReward.getUserRewards().size());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}