package ru.lewis.slideinvestment.bossbar;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitTask;
import ru.lewis.slideinvestment.Main;
import ru.lewis.slideinvestment.event.InvestmentStopEvent;
import ru.lewis.slideinvestment.event.PlayerCreateFirstInvestmentEvent;
import ru.lewis.slideinvestment.event.RemovePlayerInvestmentEvent;
import ru.lewis.slideinvestment.image.impl.InvestmentImageImpl;
import ru.lewis.slideinvestment.player.PlayerInvestment;
import ru.lewis.slideinvestment.utils.Utils;

import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;

public class HandlerBossBar implements Listener {

    private HashMap<PlayerInvestment, BossBarUtil> playersInvestmentsBossBar = new HashMap<>();
    private HashMap<PlayerInvestment, BukkitTask> playersInvestmentsTasks = new HashMap<>();

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        try {
            PlayerInvestment playerInvestment = Main.getInstance().getInvestmentCache().getPlayerInvestment(e.getPlayer().getUniqueId());
            if (playersInvestmentsTasks.containsKey(playerInvestment)) {

                BukkitTask bukkitTask = playersInvestmentsTasks.get(playerInvestment);
                bukkitTask.cancel();
                playersInvestmentsTasks.remove(playerInvestment);

                BossBarUtil bossBar = playersInvestmentsBossBar.get(playerInvestment);
                bossBar.hide();
                playersInvestmentsBossBar.remove(playerInvestment);

                for (InvestmentImageImpl i : playerInvestment.getInvestment()) {
                    i.getTimerTask().cancel();
                }

                Main.getInstance().getInvestmentCache().removePlayerInvestment(e.getPlayer().getUniqueId());
            }
        } catch (NoSuchElementException ignored) {}
    }

    @EventHandler
    public void onRemovePlayerInvestment(RemovePlayerInvestmentEvent e) {
        PlayerInvestment playerInvestment = Main.getInstance().getInvestmentCache().getPlayerInvestment(e.getPlayer().getUniqueId());
        playersInvestmentsTasks.get(playerInvestment).cancel();
    }

    @EventHandler
    public void onInvestmentStop(InvestmentStopEvent e) {
        PlayerInvestment playerInvestment = Main.getInstance().getInvestmentCache().getPlayerInvestment(e.getPlayer().getUniqueId());
        if (playerInvestment.getInvestment().isEmpty()) {

            BukkitTask bukkitTask = playersInvestmentsTasks.get(playerInvestment);
            bukkitTask.cancel();
            playersInvestmentsTasks.remove(playerInvestment);

            BossBarUtil bossBar = playersInvestmentsBossBar.get(playerInvestment);
            bossBar.hide();
            playersInvestmentsBossBar.remove(playerInvestment);

            Main.getInstance().getInvestmentCache().removePlayerInvestment(e.getPlayer().getUniqueId());
        }
    }

    @EventHandler
    public void onPlayerFirstCreateInvestment(PlayerCreateFirstInvestmentEvent e) {
        BossBarUtil bossBar = new BossBarUtil(e.getPlayer(), "Загрузка...", 1.0f);
        bossBar.sendPacketCreateBossBar();

        PlayerInvestment playerInvestment = Main.getInstance().getInvestmentCache().getPlayerInvestment(e.getPlayer().getUniqueId());
        playersInvestmentsBossBar.put(playerInvestment, bossBar);

        BukkitTask bukkitTask = Bukkit.getScheduler().runTaskTimer(Main.getInstance(), () -> {
           this.updateBossBar(playerInvestment, bossBar);
        }, 0L, 20L);
        playersInvestmentsTasks.put(playerInvestment, bukkitTask);
    }

    private void updateBossBar(PlayerInvestment playerInvestment, BossBarUtil bossBar) {
        String[] titleTemplate = Main.getInstance().getSettings().getMessages().bossBar.name().split(" ");
        List<InvestmentImageImpl> investments = playerInvestment.getInvestment();
        int investSize = investments.size();

        int investIndex = 0;
        int timeIndex = 0;
        StringBuilder titleBuilder = new StringBuilder();

        for (String part : titleTemplate) {
            if (part.contains("{invest}")) {
                String investmentName = investIndex < investSize ?
                        investments.get(investIndex).getInvestment().investment_name() :
                        Main.getInstance().getSettings().getMessages().bossBar.placeHolderInvestNone();
                titleBuilder.append(part.replace("{invest}", investmentName)).append(" ");
                investIndex++;
            } else if (part.contains("{time}")) {
                String timeString = timeIndex < investSize ?
                        Utils.convertSecondsToMinutesAndSeconds(investments.get(timeIndex).getTimerTask().getSecondsLeft()) :
                        "";
                titleBuilder.append(part.replace("{time}", timeString)).append(" ");
                timeIndex++;
            } else {
                titleBuilder.append(part).append(" ");
            }
        }

        String finalTitle = titleBuilder.toString().trim();
        bossBar.updateTitle(finalTitle);
    }
}
