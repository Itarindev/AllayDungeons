package allayplugins.dev.itarin.dao.rewards;

import allayplugins.dev.itarin.model.user.UserReward;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class UserRewardsDAO {

    @Getter
    private static final List<UserReward> userRewards = new ArrayList<>();

    public static UserReward findUserByName(String userName) {
        return userRewards.stream().filter(userReward -> userReward.getUserName().equals(userName)).findFirst().orElse(null);
    }
}
