package allayplugins.dev.itarin.configuration;

import allayplugins.dev.itarin.AllayDungeons;
import allayplugins.dev.itarin.utils.MultipleFiles;
import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

@Getter
public class MessagesConfiguration {

    private final FileConfiguration configuration;

    private String notADungeon;
    private String dungeonExit;
    private String commandNotReleased;

    private String noHaveRewards;
    private List<String> collectedRewards;

    private List<String> bossSpawned;
    private String bossKilled;

    private String lastPhase, maxLimit;

    private List<String> nextPhase, inLine;

    private String startingTitle, startingSubTitle;
    private List<String> startingMessage;

    private String startedTitle, startedSubTitle;
    private List<String> startedMessage;

    private String completedTitle, completedSubTitle;
    private List<String> completedMessage;

    public MessagesConfiguration(AllayDungeons main) {
        configuration = MultipleFiles.getConfig(main, "messages");
    }

    public void loadMessages() {

        notADungeon = get("All", "NotADungeon");
        dungeonExit = get("All", "DungeonExit");
        commandNotReleased = get("All", "CommandNotReleased");

        noHaveRewards = get("Rewards", "NoHaveRewards");
        collectedRewards = getList("Rewards", "Collected");

        bossSpawned = getList("Boss", "BossSpawned");
        bossKilled = get("Boss", "BossKilled");

        lastPhase = get("Phases", "LastPhase");
        maxLimit = get("Phases", "MaxLimit");

        nextPhase = getList("Phases", "NextPhase");
        inLine = getList("InLine", "Message");

        startingTitle = get("Starting", "Title");
        startingSubTitle = get("Starting", "SubTitle");
        startingMessage = getList("Starting", "Message");

        startedTitle = get("Started", "Title");
        startedSubTitle = get("Started", "SubTitle");
        startedMessage = getList("Started", "Message");

        completedTitle = get("Completed", "Title");
        completedSubTitle = get("Completed", "SubTitle");
        completedMessage = getList("Completed", "Message");
    }

    public String get(String path, String key) {
        return configuration.getString("Messages." + path + "." + key);
    }

    public List<String> getList(String path, String key) {
        return configuration.getStringList("Messages." + path + "." + key);
    }
}