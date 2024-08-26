package ru.lewis.slideinvestment.cache;

import ru.lewis.slideinvestment.exception.NetBablaException;
import ru.lewis.slideinvestment.model.Settings;
import ru.lewis.slideinvestment.player.PlayerInvestment;

import java.util.NoSuchElementException;
import java.util.UUID;

public interface InvestmentCache {

    public PlayerInvestment getPlayerInvestment(UUID uuid) throws NoSuchElementException;
    public void removePlayerInvestment(UUID uuid);
    public PlayerInvestment addInvestmentToPlayerInvestmentOrCreatePlayerInvestmentAndAddInvestment(UUID uuid, Settings.Options.Investment investment) throws IllegalStateException, NetBablaException;

}
