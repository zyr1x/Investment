package ru.lewis.slideinvestment.image;

import ru.lewis.slideinvestment.model.Settings;

import java.util.UUID;

public interface InvestmentImage {

    public boolean isWin();
    public void giveAward() throws NullPointerException;
    public Settings.Options.Investment getInvestment();
    public UUID getInvestor();

}
