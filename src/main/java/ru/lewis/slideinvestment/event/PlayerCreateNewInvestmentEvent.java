package ru.lewis.slideinvestment.event;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

public class PlayerCreateNewInvestmentEvent extends PlayerEvent {

    private static final HandlerList handlers = new HandlerList();

    private Player player;

    public PlayerCreateNewInvestmentEvent(Player player) {
        super(player);
        this.player = player;
    }

    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    public static @NotNull HandlerList getHandlerList() {
        return handlers;
    }
}
