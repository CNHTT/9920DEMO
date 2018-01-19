package com.szfp.pos.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;
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
    private String sn;
    private int totalStake;
    private Date createTime;
    private String operator;
    private Date  matchPlayed;
    private String closingTime="03:AM";
    private Date validity;
    private Date SubmitTime=createTime;

    public Date getSubmitTime() {
        return SubmitTime;
    }

    public void setSubmitTime(Date submitTime) {
        SubmitTime = submitTime;
    }

    @Transient
    List<Item> datas;
    private String list;
    @Generated(hash = 1685658528)
    public PosRecord(Long id, String sn, int totalStake, Date createTime,
            String operator, Date matchPlayed, String closingTime, Date validity,
            Date SubmitTime, String list) {
        this.id = id;
        this.sn = sn;
        this.totalStake = totalStake;
        this.createTime = createTime;
        this.operator = operator;
        this.matchPlayed = matchPlayed;
        this.closingTime = closingTime;
        this.validity = validity;
        this.SubmitTime = SubmitTime;
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
    public String getSn() {
        return this.sn;
    }
    public void setSn(String sn) {
        this.sn = sn;
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
    public String getOperator() {
        return this.operator;
    }
    public void setOperator(String operator) {
        this.operator = operator;
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

    public List<Item> getDatas() {
        return datas;
    }

    public void setDatas(List<Item> datas) {
        this.datas = datas;
    }
}
