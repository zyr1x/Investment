package ru.lewis.slideinvestment.image.impl;

import lombok.Getter;
import org.bukkit.Bukkit;
import ru.lewis.slideinvestment.event.InvestmentAwardEvent;
import ru.lewis.slideinvestment.event.InvestmentCancelAwardEvent;
import ru.lewis.slideinvestment.event.InvestmentStopEvent;
import ru.lewis.slideinvestment.image.InvestmentImage;
import ru.lewis.slideinvestment.model.Settings;
import ru.lewis.slideinvestment.task.TimerTask;
import ru.lewis.slideinvestment.utils.Utils;
import ru.lewis.slideinvestment.utils.Vault;

import java.util.UUID;

public class InvestmentImageImpl
implements InvestmentImage {

    private Settings.Options.Investment investment;

    private UUID investor;

    @Getter
    private TimerTask timerTask;

    @Getter
    private boolean isWin;

    public InvestmentImageImpl(Settings.Options.Investment investment, UUID investor) {
        this.investment = investment;
        this.investor = investor;
        this.isWin = Utils.calculateChance(investment.chance());
        this.timerTask = new TimerTask(investment.time_in_second(), () -> {
            this.giveAward();
            Bukkit.getPluginManager().callEvent(new InvestmentStopEvent(Bukkit.getPlayer(investor), this));
            }, () -> {});
    }

    @Override
    public boolean isWin() {
        return isWin;
    }

    @Override
    public void giveAward() throws NullPointerException {
        if (!isWin()) {
            Bukkit.getPluginManager().callEvent(new InvestmentCancelAwardEvent(Bukkit.getPlayer(investor), this));
            return;
        }
        Bukkit.getPluginManager().callEvent(new InvestmentAwardEvent(Bukkit.getPlayer(investor), this));
        Vault.econ.depositPlayer(Bukkit.getOfflinePlayer(getInvestor()), investment.multiplier() * investment.price());
    }

    @Override
    public Settings.Options.Investment getInvestment() {
        return investment;
    }

    @Override
    public UUID getInvestor() {
        return investor;
    }
}
