package ru.lewis.slideinvestment.player;

import lombok.Getter;
import org.bukkit.boss.BossBar;
import ru.lewis.slideinvestment.image.impl.InvestmentImageImpl;

import java.util.List;
import java.util.TimerTask;
import java.util.UUID;

public class PlayerInvestment {

    @Getter
    private UUID uuid;

    @Getter
    private List<InvestmentImageImpl> investment;

    @Getter
    private BossBar bossBar;

    @Getter
    private TimerTask timerTask;

    public PlayerInvestment(UUID uuid, List<InvestmentImageImpl> investment) {
        this.uuid = uuid;
        this.investment = investment;
    }
}
