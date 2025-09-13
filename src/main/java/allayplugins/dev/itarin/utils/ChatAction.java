package allayplugins.dev.itarin.utils;

import allayplugins.dev.itarin.AllayDungeons;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.LinkedHashMap;
import java.util.function.BiConsumer;

public class ChatAction {

    private final LinkedHashMap<String, BiConsumer<Player, String>> players;

    public ChatAction(AllayDungeons main) {
        players = new LinkedHashMap<>();
        Bukkit.getPluginManager().registerEvents(new Listener() {

            @EventHandler
            void onMessage(AsyncPlayerChatEvent event) {
                BiConsumer<Player, String> consumer = players.getOrDefault(event.getPlayer().getName(), null);

                event.setCancelled(consumer != null);
                if (event.isCancelled()) {
                    consumer.accept(event.getPlayer(), event.getMessage());
                    players.remove(event.getPlayer().getName());
                }
            }
        }, main);
    }

    public void addAction(Player player, BiConsumer<Player, String> consumer) {
        players.put(player.getName(), consumer);
    }
}