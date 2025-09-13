package allayplugins.dev.itarin.commands;

import allayplugins.dev.itarin.AllayDungeons;
import allayplugins.dev.itarin.inventories.dungeon.ViewDungeonsMenu;
import allayplugins.stompado.command.CommandContext;
import org.bukkit.entity.Player;

public class DungeonsCommand {

    private final AllayDungeons main;

    public DungeonsCommand(AllayDungeons main) {
        this.main = main;
    }

    @CommandContext(name = "dungeons", allowedConsole = false)
    public void dungeonsCommand(Player player) {
        new ViewDungeonsMenu(main).open(player);
    }
}