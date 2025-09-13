package allayplugins.dev.itarin;

import allayplugins.dev.itarin.commands.DungeonCommand;
import allayplugins.dev.itarin.commands.DungeonsCommand;
import allayplugins.dev.itarin.configuration.InventoryConfiguration;
import allayplugins.dev.itarin.configuration.MessagesConfiguration;
import allayplugins.dev.itarin.connections.MySQL;
import allayplugins.dev.itarin.connections.SQLite;
import allayplugins.dev.itarin.connections.model.IDatabase;
import allayplugins.dev.itarin.connections.transform.UserRewardTransform;
import allayplugins.dev.itarin.dao.dungeon.DungeonDAO;
import allayplugins.dev.itarin.listeners.dungeon.DungeonEditListener;
import allayplugins.dev.itarin.listeners.dungeon.SelectDungeonListener;
import allayplugins.dev.itarin.listeners.phase.EditPhaseListener;
import allayplugins.dev.itarin.listeners.phase.NewPhaseEditListener;
import allayplugins.dev.itarin.listeners.phase.PhaseEditListener;
import allayplugins.dev.itarin.listeners.entity.EntityDamageListener;
import allayplugins.dev.itarin.listeners.entity.EntityDeathListener;
import allayplugins.dev.itarin.listeners.player.PlayerProcessCommandListener;
import allayplugins.dev.itarin.listeners.reward.CollectRewardListener;
import allayplugins.dev.itarin.listeners.reward.RewardSaveListener;
import allayplugins.dev.itarin.manager.dungeon.DungeonManager;
import allayplugins.dev.itarin.manager.reward.RewardManager;
import allayplugins.dev.itarin.manager.scoreboard.ScoreboardManager;
import allayplugins.dev.itarin.runnable.DungeonStartRunnable;
import allayplugins.dev.itarin.setup.DungeonSetup;
import allayplugins.dev.itarin.utils.ChatAction;
import allayplugins.dev.itarin.utils.MultipleFiles;
import allayplugins.stompado.manager.CommandManager;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class AllayDungeons extends JavaPlugin {

    @Getter
    private static AllayDungeons instance;

    private MessagesConfiguration messagesConfiguration;
    private InventoryConfiguration inventoryConfiguration;

    private IDatabase iDatabase;
    private RewardManager rewardManager;

    private CommandManager commandManager;

    private DungeonManager dungeonManager;
    private ScoreboardManager scoreboardManager;

    private ChatAction chatAction;

    @Override
    public void onEnable() {
        instance = this;
        registerYaml();
        registerConnections();

        chatAction = new ChatAction(this);

        scoreboardManager = new ScoreboardManager(this);

        new DungeonSetup(this);

        registerEvents();
        registerCommands();

        new DungeonStartRunnable().runTaskTimer(this, 20L * 30, 20L * 30);

        sendMessage();
    }

    @Override
    public void onDisable() {
        DungeonDAO.getDungeons().forEach(dungeonManager::save);
        iDatabase.closeConnection();
    }

    private void registerCommands() {
        commandManager = new CommandManager(this);
        commandManager.registerCommands(new DungeonCommand(this), new DungeonsCommand(this));
    }

    private void registerConnections() {
        iDatabase = getConfig().getBoolean("MySQL.Active") ? new MySQL() : new SQLite();
        rewardManager = new RewardManager(iDatabase);

        new UserRewardTransform(this).loadUserRewards();
    }

    private void registerEvents() {
        new DungeonEditListener(this);
        new PhaseEditListener(this);
        new NewPhaseEditListener(this);
        new EditPhaseListener(this);
        new EntityDeathListener(this);
        new EntityDamageListener(this);
        new RewardSaveListener(this);
        new CollectRewardListener(this);
        new SelectDungeonListener(this);
        new PlayerProcessCommandListener(this);
    }

    private void registerYaml() {
        MultipleFiles.createFolder(this, "cache");

        saveDefaultConfig();
        MultipleFiles.createConfig(this, "dungeons");
        MultipleFiles.createConfig(this, "scoreboard");
        MultipleFiles.createConfig(this, "messages");
        MultipleFiles.createConfig(this, "inventories");

        dungeonManager = new DungeonManager(this);

        inventoryConfiguration = new InventoryConfiguration(this);
        inventoryConfiguration.loadInventories();

        messagesConfiguration = new MessagesConfiguration(this);
        messagesConfiguration.loadMessages();
    }

    private void sendMessage() {
        Bukkit.getConsoleSender().sendMessage("§6[AllayDungeons] §fCriado por §6[Itarin]");
        Bukkit.getConsoleSender().sendMessage("§6[AllayDungeons] §aO plugin foi inciado com sucesso.");
    }
}