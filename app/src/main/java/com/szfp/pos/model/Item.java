package com.szfp.pos.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 戴尔 on 2017/11/18.
 */

public class Item implements Serializable {
    private String gameType;
    private String gameOption;
    private String OldType;
    private List<Integer> list;
    private int amount;
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGameType() {
        return gameType;
    }

    public void setGameType(String gameType) {
        this.gameType = gameType;
    }

    public String getGameOption() {
        return gameOption;
    }

    public void setGameOption(String gameOption) {
        this.gameOption = gameOption;
    }

    public String getOldType() {
        return OldType;
    }

    public void setOldType(String oldType) {
        OldType = oldType;
    }

    public List<Integer> getList() {
        return list;
    }

    public void setList(List<Integer> list) {
        this.list = list;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
