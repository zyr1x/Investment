package ru.lewis.slideinvestment.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import ru.lewis.slideinvestment.Main;
import ru.lewis.slideinvestment.exception.NetBablaException;
import ru.lewis.slideinvestment.model.Settings;
import ru.lewis.slideinvestment.player.PlayerInvestment;

import java.util.NoSuchElementException;
import java.util.UUID;

public class InvestmentCommand
implements CommandExecutor {


    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        // investment buy 1
        if (args.length != 2) {
            return true;
        }

        if (!(commandSender instanceof Player)) {
            return true;
        }

        int price = Integer.parseInt(args[1]);
        Player player = (Player) commandSender;
        UUID uuid = player.getUniqueId();

        Settings.Options.Investment investment = Main.getInstance().getSettings().getOptions().investments.stream()
                .filter(i -> i.price() == price).findFirst()
                .orElseThrow(() -> new NoSuchElementException("Investment with price " + price + " not found"));

        try {
            PlayerInvestment playerInvestment = Main.getInstance().getInvestmentCache().addInvestmentToPlayerInvestmentOrCreatePlayerInvestmentAndAddInvestment(uuid, investment);
            player.sendMessage(Main.getInstance().getSettings().getMessages().successfully_create_invest
                    .replace("{amount}", String.valueOf(playerInvestment.getInvestment().size())));
        } catch (IllegalStateException e) {
            player.sendMessage(Main.getInstance().getSettings().getMessages().cancel_create_invest);
        } catch (NetBablaException e) {
            player.sendMessage(Main.getInstance().getSettings().getMessages().net_babla);
        }


        return true;
    }
}
