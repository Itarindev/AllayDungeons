package allayplugins.dev.itarin.utils;

import com.google.common.base.Strings;
import lombok.val;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PlayerUtils {

    public static void sendMessage(CommandSender sender, String message) {
        if (sender instanceof ConsoleCommandSender) return;

        val player = (Player) sender;
        player.sendMessage(message.replace("&", "§"));
    }

    public static void playSound(CommandSender sender, Sound sound) {
        if (sender instanceof ConsoleCommandSender) return;

        val player = (Player) sender;
        player.playSound(player.getLocation(), sound, 1F, 1F);
    }

    public static void sendActionBar(Player player, String text) {
        sendPacket(new PacketPlayOutChat(IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + text.replace("&", "§") + "\"}"), (byte) 2), player);
    }

    @SuppressWarnings("rawtypes")
    private static void sendPacket(Packet packet, Player player) {
        CraftPlayer craftPlayer = (CraftPlayer) player;
        (craftPlayer.getHandle()).playerConnection.sendPacket(packet);
    }

    public static boolean isNumber(CommandSender sender, String number) {
        if (!NumberUtils.isNumber(number) || Double.parseDouble(number) <= 0) {
            sender.sendMessage("§c✘ Quantidade inválida! §7Use um valor maior que zero.");
            playSound(sender, Sound.VILLAGER_NO);
            return false;
        }
        return true;
    }

    public static ItemStack getPlayerHead(String playerName) {
        ItemStack playerHead = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta meta = (SkullMeta) playerHead.getItemMeta();

        meta.setOwner(playerName);
        playerHead.setItemMeta(meta);

        return playerHead;
    }

    public static void removeItem(Player player) {
        val item = player.getItemInHand();
        if (item.getAmount() <= 1)
            player.setItemInHand(new ItemStack(Material.AIR));

        else
            item.setAmount(item.getAmount() - 1);
    }

    public static int getAmount(Inventory inventory, ItemStack item) {
        int amount = 0;
        for (val items : inventory.all(item.getType()).values()) {
            if (!items.isSimilar(item))
                continue;

            amount = items.getAmount();
        }
        return amount;
    }

    public static void removeItemInventory(Inventory inventory, ItemStack item, int amount) {
        for (Map.Entry<Integer, ? extends ItemStack> entry : inventory.all(item.getType()).entrySet()) {
            val items = entry.getValue();
            if (items.isSimilar(item)) {
                if (items.getAmount() <= amount) {
                    amount -= items.getAmount();
                    inventory.clear(entry.getKey());

                } else {
                    items.setAmount(items.getAmount() - amount);
                    amount = 0;
                }
            }
            if (amount == 0)
                break;
        }
    }

    public static List<Player> getNearbyPlayers(Location location, double distance) {
        val players = new ArrayList<Player>();
        location.getWorld().getNearbyEntities(location, distance, distance, distance).stream().filter(entities -> entities instanceof Player).forEach(entities -> players.add((Player) entities));
        return players;
    }

    public static String sendProgressBar(String symbol, ChatColor completedColor, ChatColor notCompleteColor, double current, double max, int totalBars) {
        val percentage = (float) (current / max);
        val bars = (int) (totalBars * percentage);

        val completed = Strings.repeat(completedColor + symbol, bars);
        val notComplete = Strings.repeat(notCompleteColor + symbol, totalBars - bars);

        return completed + notComplete;
    }

    public static double clamp(double max, double min, double value) {
        return Math.max(min, Math.min(max, value));
    }

}