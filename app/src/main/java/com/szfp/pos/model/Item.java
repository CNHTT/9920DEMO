package com.szfp.pos.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 戴尔 on 2017/11/18.
 */

public class Item implements Serializable {
    private String type;
    private String under;
    private List<String> games;
    private int amount;


    private String OldType;
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGameType() {
        return type;
    }

    public void setGameType(String gameType) {
        this.type = gameType;
    }

    public String getUnder() {
        return under;
    }

    public void setUnder(String under) {
        this.under = under;
    }

    public String getOldType() {
        return OldType;
    }

    public void setOldType(String oldType) {
        OldType = oldType;
    }

    public List<String> getList() {
        return games;
    }

    public void setList(List<String> list) {
        this.games = list;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
