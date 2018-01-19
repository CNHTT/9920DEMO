package com.szfp.pos.model;

import java.util.Date;
import java.util.List;

/**
 * Created by 戴尔 on 2017/11/29.
 */

public class Pos {
    private String sn;
    private String operator;
    private Date SubmitTime;
    private List<Item> datas;

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Date getSubmitTime() {
        return SubmitTime;
    }

    public void setSubmitTime(Date submitTime) {
        SubmitTime = submitTime;
    }

    public List<Item> getDatas() {
        return datas;
    }

    public void setDatas(List<Item> datas) {
        this.datas = datas;
    }
}
