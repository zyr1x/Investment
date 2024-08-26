package ru.lewis.slideinvestment.handler;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import ru.lewis.slideinvestment.Main;
import ru.lewis.slideinvestment.event.InvestmentAwardEvent;
import ru.lewis.slideinvestment.event.InvestmentCancelAwardEvent;
import ru.lewis.slideinvestment.event.InvestmentStopEvent;
import ru.lewis.slideinvestment.event.RemovePlayerInvestmentEvent;
import ru.lewis.slideinvestment.player.PlayerInvestment;

public class HandlerInvestmentAward implements Listener {

    @EventHandler
    public void onInvestmentStop(InvestmentStopEvent e) {
        PlayerInvestment playerInvestment = Main.getInstance().getInvestmentCache().getPlayerInvestment(e.getPlayer().getUniqueId());
        playerInvestment.getInvestment().remove(e.getInvestmentImage());
    }

    @EventHandler
    public void onInvestmentAward(InvestmentAwardEvent e) {
        if (e.getPlayer() != null) {
            e.getPlayer().sendMessage(Main.getInstance().getSettings().getMessages().successfully_invest
                    .replace("{name}", e.getInvestmentImage().getInvestment().investment_name())
                    .replace("{amount}", String.valueOf(e.getInvestmentImage().getInvestment().price() * e.getInvestmentImage().getInvestment().multiplier()))
            );
        }
    }

    @EventHandler
    public void onRemovePlayerInvestment(RemovePlayerInvestmentEvent e) {
        e.getPlayer().sendMessage(Main.getInstance().getSettings().getMessages().cancel_all_invest);
    }

    @EventHandler
    public void onInvestmentCancelAward(InvestmentCancelAwardEvent e) {
        if (e.getPlayer() != null) {
            e.getPlayer().sendMessage(Main.getInstance().getSettings().getMessages().cancel_invest
                    .replace("{name}", e.getInvestmentImage().getInvestment().investment_name())
            );
        }
    }
}
