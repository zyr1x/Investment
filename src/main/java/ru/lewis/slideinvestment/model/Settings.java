package ru.lewis.slideinvestment.model;

import de.exlll.configlib.Comment;
import de.exlll.configlib.Configuration;
import de.exlll.configlib.Ignore;
import de.exlll.configlib.YamlConfigurations;
import lombok.Getter;
import ru.lewis.slideinvestment.Main;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class Settings {

    @Getter
    private Messages messages;
    @Getter
    private Options options;

    public void initConfig() {
        this.messages = new Messages();
        this.options = new Options();

        if (!loadConfiguration(this.messages.path, Messages.class, this.messages)) {
            this.messages = YamlConfigurations.load(this.messages.path, Messages.class);
        }

        if (!loadConfiguration(this.options.path, Options.class, this.options)) {
            this.options = YamlConfigurations.load(this.options.path, Options.class);
        }
    }

    public <T> boolean loadConfiguration(Path path, Class<T> configurationType, T config) {
        File file = path.toFile();
        if (!file.exists()) {
            YamlConfigurations.save(path, configurationType, config);
            return true;
        }
        return false;
    }

    @Configuration
    public static class Messages {

        @Ignore
        public Path path;

        public Messages() {
            this.path = Paths.get(Main.getInstance().getDataFolder() + File.separator + "messages.yml");
        }

        @Comment("Сообщение о том, что инвестиция была успешно создана")
        public String successfully_create_invest = "Вы успешно создали инвестицию, у вас уже {amount}";

        @Comment("Сообщение о том, что достигнут лимит по сломатам в инвестициях.")
        public String cancel_create_invest = "Вы достигли лимита по созданию инвестиций!";

        @Comment("Сообщене о том, что у игрока закончились все инвестиции")
        public String cancel_all_invest = "У вас закончились все инвестиции, если захотите инвестировать еще используйте команду /investment";

        @Comment("Сообщение о том, что инвестиция зашла.")
        public String successfully_invest = "Ваша инвестиция {name} успешно зашла! Вы выиграли {amount} монеток";

        @Comment("Сообщение о том, что инвестиция прогорела.")
        public String cancel_invest = "Ваша инвестиция {name} прогорела :(";

        @Comment("Сообщение о том, что у игрока не хватает монеток.")
        public String net_babla = "У вас недостаточно монеток для покупки этой инвестиции.";

        @Comment("Настройки BossBar при активных инвестициях, если у вас нет плагина ProtocolLib на сервере, смело можете отключать BossBar, в альтернативном случае у вас будут ошибки в консоле.")
        public BossBar bossBar = new BossBar(
                "{invest} §6{time} §8| {invest} §6{time} §f<- §nИнвестиции ->§f {invest} §6{time} §8| {invest} §6{time}",
                "§7Свободный слот"
        );

        public record BossBar(
                String name,
                String placeHolderInvestNone
        ) {}

    }

    @Configuration
    public static class Options {

        @Ignore
        public Path path;

        public Options() {
            this.path = Paths.get(Main.getInstance().getDataFolder() + File.separator + "options.yml");
        }

        @Comment("Будет ли работать BossBar?")
        public boolean isBossBarEnable = true;

        @Comment("Настройка инвестиций плагина")
        public List<Investment> investments = Arrays.asList(
                new Investment(0, 60, "Инвестиция 1", 60, 2)
        );

        @Comment("Максимальное количество инвестиций за раз")
        public int max_investment = 4;

        public record Investment(
                int price,
                int chance,
                String investment_name,
                int time_in_second,
                double multiplier
        ) {}
    }

}