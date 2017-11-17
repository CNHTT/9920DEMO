package com.szfp.pos.model;

/**
 * author：ct on 2017/10/25 17:23
 * email：cnhttt@163.com
 */

public class TableItem {

    private String[] text;
    private int[] width;
    private int[] align;

    public TableItem() {
        text = new String[]{"test","test","test"};
        width = new int[]{1,1,1};
        align = new int[]{0,0,0};
    }

    public String[] getText() {
        return text;
    }

    public void setText(String[] text) {
        this.text = text;
    }

    public int[] getWidth() {
        return width;
    }

    public void setWidth(int[] width) {
        this.width = width;
    }

    public int[] getAlign() {
        return align;
    }

    public void setAlign(int[] align) {
        this.align = align;
    }
}
