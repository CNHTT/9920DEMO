package com.szfp.pos.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;
import java.util.Date;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by 戴尔 on 2017/11/18.
 */

@Entity
public class LoginRecord  implements Serializable {
    static final long serialVersionUID = 42L;


    @Id
    private Long id;
    private Date loginDate;
    private Date logoutDate;
    private String terminalId;
    private int soldQty;
    private int paymentQty;

    private Float soldAmount;
    private Float paymentAmount;
    private Float netAmount;
    @Generated(hash = 1091554816)
    public LoginRecord(Long id, Date loginDate, Date logoutDate, String terminalId,
            int soldQty, int paymentQty, Float soldAmount, Float paymentAmount,
            Float netAmount) {
        this.id = id;
        this.loginDate = loginDate;
        this.logoutDate = logoutDate;
        this.terminalId = terminalId;
        this.soldQty = soldQty;
        this.paymentQty = paymentQty;
        this.soldAmount = soldAmount;
        this.paymentAmount = paymentAmount;
        this.netAmount = netAmount;
    }
    @Generated(hash = 601420123)
    public LoginRecord() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Date getLoginDate() {
        return this.loginDate;
    }
    public void setLoginDate(Date loginDate) {
        this.loginDate = loginDate;
    }
    public Date getLogoutDate() {
        return this.logoutDate;
    }
    public void setLogoutDate(Date logoutDate) {
        this.logoutDate = logoutDate;
    }
    public String getTerminalId() {
        return this.terminalId;
    }
    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }
    public int getSoldQty() {
        return this.soldQty;
    }
    public void setSoldQty(int soldQty) {
        this.soldQty = soldQty;
    }
    public int getPaymentQty() {
        return this.paymentQty;
    }
    public void setPaymentQty(int paymentQty) {
        this.paymentQty = paymentQty;
    }
    public Float getSoldAmount() {
        return this.soldAmount;
    }
    public void setSoldAmount(Float soldAmount) {
        this.soldAmount = soldAmount;
    }
    public Float getPaymentAmount() {
        return this.paymentAmount;
    }
    public void setPaymentAmount(Float paymentAmount) {
        this.paymentAmount = paymentAmount;
    }
    public Float getNetAmount() {
        return this.netAmount;
    }
    public void setNetAmount(Float netAmount) {
        this.netAmount = netAmount;
    }



}
