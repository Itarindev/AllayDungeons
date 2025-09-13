package allayplugins.dev.itarin.inventories.reward;

import allayplugins.dev.itarin.utils.ItemBuilder;
import lombok.val;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class CollectRewardMenu {

    public void openRewardsInventory(Player player, List<ItemStack> items) {
        val inv = Bukkit.createInventory(null, 45, "§8Suas recompensas");

        int slot = 10;
        for (ItemStack item : items) {
            if (slot >= 39) break;
            inv.setItem(slot, item);
            slot++;
        }

        ItemStack collectAll = new ItemBuilder(Material.CHEST)
                .setName("§aClique aqui para coletar tudo!")
                .setLore(Arrays.asList("", "§7Clique para pegar todos os itens", "§7de uma vez só!"))
                .build();
        inv.setItem(40, collectAll);

        player.openInventory(inv);
    }
}
