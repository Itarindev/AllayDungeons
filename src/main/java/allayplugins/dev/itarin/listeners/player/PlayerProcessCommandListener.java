package allayplugins.dev.itarin.listeners.player;

import allayplugins.dev.itarin.AllayDungeons;
import allayplugins.dev.itarin.configuration.MessagesConfiguration;
import allayplugins.dev.itarin.dao.dungeon.DungeonDAO;
import allayplugins.dev.itarin.utils.PlayerUtils;
import lombok.val;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class PlayerProcessCommandListener implements Listener {

    private final FileConfiguration configuration;

    private final MessagesConfiguration messagesConfiguration;

    public PlayerProcessCommandListener(AllayDungeons main) {
        Bukkit.getPluginManager().registerEvents(this, main);

        configuration = main.getConfig();

        messagesConfiguration = main.getMessagesConfiguration();
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        val player = event.getPlayer();

        val dungeon = DungeonDAO.getDungeonWherePlayerIs(player.getName());
        if (dungeon == null) return;

        val commandsReleased = configuration.getStringList("CommandsReleased");

        val message = event.getMessage();

        if (message.toLowerCase().startsWith("/dungeon sair")) return;

        if (player.hasPermission("allaydungeons.bypass.commands")) return;

        val command = message.split(" ")[0].substring(1).toLowerCase();

        if (!commandsReleased.contains(command)) {
            event.setCancelled(true);
            PlayerUtils.sendMessage(player, messagesConfiguration.getCommandNotReleased());
        }
    }
}