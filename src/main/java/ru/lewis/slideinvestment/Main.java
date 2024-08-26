package ru.lewis.slideinvestment;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import ru.lewis.slideinvestment.bossbar.HandlerBossBar;
import ru.lewis.slideinvestment.cache.InvestmentCache;
import ru.lewis.slideinvestment.cache.impl.InvestmentCacheImpl;
import ru.lewis.slideinvestment.commands.InvestmentCommand;
import ru.lewis.slideinvestment.handler.HandlerInvestmentAward;
import ru.lewis.slideinvestment.model.Settings;
import ru.lewis.slideinvestment.utils.Vault;

public final class Main extends JavaPlugin {

    @Getter
    private static Main instance;

    @Getter
    private Settings settings;

    @Getter
    private InvestmentCache investmentCache;

    @Override
    public void onEnable() {
        instance = this;

        this.settings = new Settings();
        this.settings.initConfig();

        this.investmentCache = new InvestmentCacheImpl();

        getCommand("investment").setExecutor(new InvestmentCommand());

        Bukkit.getPluginManager().registerEvents(new HandlerInvestmentAward(), this);

        if (this.settings.getOptions().isBossBarEnable) {
            Bukkit.getPluginManager().registerEvents(new HandlerBossBar(), this);
        }

        Vault.setupEconomy();

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
