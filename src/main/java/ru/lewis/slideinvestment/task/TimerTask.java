package ru.lewis.slideinvestment.task;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.scheduler.BukkitRunnable;
import ru.lewis.slideinvestment.Main;

public class TimerTask extends BukkitRunnable {

    @Getter
    @Setter
    private int secondsLeft;

    @Setter
    @Getter
    private Runnable endRunnable;

    @Setter
    @Getter
    private Runnable onDurationRunnable;

    public TimerTask(int timeInSecond, Runnable endRunnable, Runnable onDurationRunnable) {
        this.secondsLeft = timeInSecond;
        this.endRunnable = endRunnable;
        this.onDurationRunnable = onDurationRunnable;

        this.runTaskTimer(Main.getInstance(), 0L, 20L);
    }

    @Override
    public void run() {

        if (this.secondsLeft == 0) {
            this.endRunnable.run();
            this.cancel();
            return;
        }
        this.onDurationRunnable.run();
        this.secondsLeft--;
    }

}
