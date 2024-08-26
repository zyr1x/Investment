package ru.lewis.slideinvestment.event;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import ru.lewis.slideinvestment.image.impl.InvestmentImageImpl;

public class InvestmentAwardEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    @Getter
    private Player player;

    @Getter
    InvestmentImageImpl investmentImage;

    public InvestmentAwardEvent(Player player, InvestmentImageImpl investmentImage) {
        this.player = player;
        this.investmentImage = investmentImage;
    }

    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    public static @NotNull HandlerList getHandlerList() {
        return handlers;
    }

}
