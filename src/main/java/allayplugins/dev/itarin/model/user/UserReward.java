package allayplugins.dev.itarin.model.user;

import java.util.List;
import java.util.UUID;

import allayplugins.dev.itarin.model.Dungeon;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.inventory.ItemStack;

@AllArgsConstructor
@Data
public class UserReward {

    private UUID userUuid;
    private String userName;
    private Dungeon dungeon;
    private List<ItemStack> userRewards;

}