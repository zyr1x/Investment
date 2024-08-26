package ru.lewis.slideinvestment.cache.impl;

import lombok.Getter;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import ru.lewis.slideinvestment.Main;
import ru.lewis.slideinvestment.cache.InvestmentCache;
import ru.lewis.slideinvestment.event.PlayerCreateFirstInvestmentEvent;
import ru.lewis.slideinvestment.event.PlayerCreateNewInvestmentEvent;
import ru.lewis.slideinvestment.exception.NetBablaException;
import ru.lewis.slideinvestment.image.impl.InvestmentImageImpl;
import ru.lewis.slideinvestment.model.Settings;
import ru.lewis.slideinvestment.player.PlayerInvestment;
import ru.lewis.slideinvestment.utils.Vault;

import java.util.*;

public class InvestmentCacheImpl
implements InvestmentCache {

    @Getter
    private List<PlayerInvestment> playersInvestment = new ArrayList<>();

    public PlayerInvestment getPlayerInvestment(UUID uuid) throws NoSuchElementException {

        if (playersInvestment.isEmpty()) {
            throw new NoSuchElementException("Player with uuid " + uuid.toString() + " not found");
        }

        return playersInvestment.stream()
                .filter(p -> p.getUuid().equals(uuid))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Player with uuid " + uuid.toString() + " not found"));
    }

    @Override
    public void removePlayerInvestment(UUID uuid) {
        playersInvestment.removeIf(p -> p.getUuid().equals(uuid));
    }

    private PlayerInvestment createPlayerInvestment(UUID uuid, Settings.Options.Investment investment) {
        PlayerInvestment playerInvestment = new PlayerInvestment(uuid, new ArrayList<>(List.of(new InvestmentImageImpl(investment, uuid))));
        playersInvestment.add(playerInvestment);
        Bukkit.getPluginManager().callEvent(new PlayerCreateFirstInvestmentEvent(Bukkit.getPlayer(uuid)));
        return playerInvestment;
    }

    @Override
    public PlayerInvestment addInvestmentToPlayerInvestmentOrCreatePlayerInvestmentAndAddInvestment(UUID uuid, Settings.Options.Investment investment) throws IllegalStateException, NetBablaException {

        EconomyResponse economyResponse = Vault.econ.withdrawPlayer(Bukkit.getOfflinePlayer(uuid), investment.price());
        if (!economyResponse.transactionSuccess()) {
            throw new NetBablaException("У игрока с uuid " + uuid.toString() + " недостаточно монеток");
        }

        try {

            PlayerInvestment playerInvestment = getPlayerInvestment(uuid);

            if (playerInvestment.getInvestment().size() == Main.getInstance().getSettings().getOptions().max_investment) {
                throw new IllegalStateException("У игрока "+ uuid + " закончились слоты в инвестициях");
            }

            InvestmentImageImpl investmentImage = new InvestmentImageImpl(investment, uuid);
            playerInvestment.getInvestment().add(investmentImage);
            Bukkit.getPluginManager().callEvent(new PlayerCreateNewInvestmentEvent(Bukkit.getPlayer(uuid)));

            return playerInvestment;

        } catch (NoSuchElementException e) {
            return createPlayerInvestment(uuid, investment);
        }
    }
}
