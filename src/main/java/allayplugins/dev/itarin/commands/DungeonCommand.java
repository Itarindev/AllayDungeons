package allayplugins.dev.itarin.commands;

import allayplugins.dev.itarin.AllayDungeons;
import allayplugins.dev.itarin.configuration.MessagesConfiguration;
import allayplugins.dev.itarin.dao.dungeon.DungeonDAO;
import allayplugins.dev.itarin.dao.rewards.UserRewardsDAO;
import allayplugins.dev.itarin.enuns.DungeonState;
import allayplugins.dev.itarin.inventories.dungeon.DungeonEditorMenu;
import allayplugins.dev.itarin.inventories.reward.CollectRewardMenu;
import allayplugins.dev.itarin.manager.dungeon.DungeonManager;
import allayplugins.dev.itarin.model.Dungeon;
import allayplugins.dev.itarin.utils.ItemBuilder;
import allayplugins.dev.itarin.utils.PlayerUtils;
import allayplugins.stompado.command.CommandContext;
import lombok.val;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import sun.dc.pr.PRError;

import java.util.ArrayList;

public class DungeonCommand {

    private final AllayDungeons main;

    private final MessagesConfiguration messagesConfiguration;

    public DungeonCommand(AllayDungeons main) {
        this.main = main;

        messagesConfiguration = main.getMessagesConfiguration();
    }

    @CommandContext(name = "dungeon", usage = "/dungeon", permission = "allaydungeons.manager.dungeons", allowedConsole = false)
    public void dungeonHelpCommand(Player player) {
        player.sendMessage("§8§m--------------------------------------------------");
        player.sendMessage("§6§lAllayDungeons §7- §fComandos disponíveis:");
        player.sendMessage("");
        player.sendMessage("§6/dungeon criar §7<id> §8- §fCria uma nova dungeon com o ID especificado.");
        player.sendMessage("§6/dungeon editar §7<id> §8- §fEdita uma dungeon existente.");
        player.sendMessage("§6/dungeon encenrrar §7<id> §8- §fEncerra uma dungeon em exploracao.");
        player.sendMessage("§6/dungeon sair §8- §fSai da dungeon atual.");
        player.sendMessage("§6/dungeon coletar §8- §fColeta suas recompensas pendentes.");
        player.sendMessage("§6/dungeons §8- §fMostra todas as dungeons disponíveis no servidor.");
        player.sendMessage("§8§m--------------------------------------------------");
    }

    @CommandContext(name = "dungeon criar", usage = "/dungeon criar (dungeon - id)", permission = "allaydungeons.manager.dungeons", allowedConsole = false)
    public void dungeonCreateCommand(Player player, String dungeonId) {
        val dungeonIsExisting = DungeonDAO.findDungeonById(dungeonId);
        if (dungeonIsExisting != null) {
            player.sendMessage("§6[AllayDungeons] §c✘ Essa dungeon ja existe.");
            return;
        }

        val icon = new ItemBuilder(Material.BOOK_AND_QUILL).build();

        val dungeon = new Dungeon(dungeonId, dungeonId, icon, null, null, new ArrayList<>(), 1, 2, false, new ArrayList<>());
        dungeon.setDungeonState(DungeonState.TO_BE_EXPLORED);
        dungeon.setScoreboardManager(main.getScoreboardManager());
        DungeonDAO.getDungeons().add(dungeon);
        player.sendMessage("§6[AllayDungeons] §aDungeon criada! Para editar, digite §e/dungeon editar " + dungeonId + " §ae personalize sua aventura.");
    }

    @CommandContext(name = "dungeon editar", usage = "/dungeon editar (dungeon - id)", permission = "allaydungeons.manager.dungeons", allowedConsole = false)
    public void dungeonEditCommand(Player player, String dungeonId) {
        val dungeonIsExisting = DungeonDAO.findDungeonById(dungeonId);
        if (dungeonIsExisting == null) {
            player.sendMessage("§6[AllayDungeons] §c✘ Essa dungeon nao existe.");
            return;
        }

        new DungeonEditorMenu(dungeonIsExisting).open(player);
    }

    @CommandContext(name = "dungeon sair", usage = "/dungeon sair", allowedConsole = false)
    public void dungeonExitCommand(Player player) {
        val dungeon = DungeonDAO.getDungeonWherePlayerIs(player.getName());

        if (dungeon == null) {
            PlayerUtils.sendMessage(player, messagesConfiguration.getNotADungeon());
            return;
        }

        dungeon.getAllPlayers().remove(player.getName());
        PlayerUtils.sendMessage(player, messagesConfiguration.getDungeonExit().replace("{dungeon_name}", dungeon.getName()));
    }

    @CommandContext(name = "dungeon coletar", allowedConsole = false)
    public void dungeonCollectRewardCommand(Player player) {
        val userReward = UserRewardsDAO.findUserByName(player.getName());
        if (userReward == null) {
            PlayerUtils.sendMessage(player, messagesConfiguration.getNoHaveRewards());
            return;
        }

        val items = userReward.getUserRewards();
        if (items == null || items.isEmpty()) return;

        new CollectRewardMenu().openRewardsInventory(player, items);
    }

    @CommandContext(name = "dungeon encerrar", permission = "allaydungeons.manager.dungeons", usage = "/dungeon encenrrar (dungeon - id)", allowedConsole= true)
    public void dungeonEnd(Player player, String dungeonId) {
        val dungeon = DungeonDAO.findDungeonById(dungeonId);
        if (dungeon == null) {
            player.sendMessage("§6[AllayDungeons] §c✘ Essa dungeon nao existe.");
            return;
        }

        if (dungeon.getDungeonState() != DungeonState.IN_EXPLORATION) {
            player.sendMessage("");
            return;
        }

        dungeon.stop();
    }
}