package com.szfp.pos.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.converter.PropertyConverter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by 戴尔 on 2017/11/18.
 */

@Entity
public class PosRecord implements Serializable {
    static final long serialVersionUID = 42L;
    @Id(autoincrement = true)
    private Long id;
    private String tsn;
    private int totalStake;
    private Date createTime;
    private String tID;
    private Date  matchPlayed;
    private String closingTime="03:AM";
    private Date validity;
//    List<Item> list;
    private String list;
    @Generated(hash = 495716509)
    public PosRecord(Long id, String tsn, int totalStake, Date createTime,
            String tID, Date matchPlayed, String closingTime, Date validity,
            String list) {
        this.id = id;
        this.tsn = tsn;
        this.totalStake = totalStake;
        this.createTime = createTime;
        this.tID = tID;
        this.matchPlayed = matchPlayed;
        this.closingTime = closingTime;
        this.validity = validity;
        this.list = list;
    }
    @Generated(hash = 528020167)
    public PosRecord() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getTsn() {
        return this.tsn;
    }
    public void setTsn(String tsn) {
        this.tsn = tsn;
    }
    public int getTotalStake() {
        return this.totalStake;
    }
    public void setTotalStake(int totalStake) {
        this.totalStake = totalStake;
    }
    public Date getCreateTime() {
        return this.createTime;
    }
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    public String getTID() {
        return this.tID;
    }
    public void setTID(String tID) {
        this.tID = tID;
    }
    public Date getMatchPlayed() {
        return this.matchPlayed;
    }
    public void setMatchPlayed(Date matchPlayed) {
        this.matchPlayed = matchPlayed;
    }
    public String getClosingTime() {
        return this.closingTime;
    }
    public void setClosingTime(String closingTime) {
        this.closingTime = closingTime;
    }
    public Date getValidity() {
        return this.validity;
    }
    public void setValidity(Date validity) {
        this.validity = validity;
    }
    public String getList() {
        return this.list;
    }
    public void setList(String list) {
        this.list = list;
    }

}
