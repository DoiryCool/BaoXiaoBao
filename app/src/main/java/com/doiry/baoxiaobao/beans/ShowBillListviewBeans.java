package com.doiry.baoxiaobao.beans;

import android.graphics.Bitmap;
import android.widget.CheckBox;

import java.util.ArrayList;
import java.util.List;

public class ShowBillListviewBeans {
    public Bitmap saveBitmap;
    public Bitmap bitmap;
    public String name;
    public String time;
    public Float amout;
    public Boolean check;
    public String description;
    public String id;

    public ShowBillListviewBeans(Bitmap saveBitmap,
                                 String name,
                                 String time,
                                 Float number,
                                 String description,
                                 Boolean check,
                                 String logId,
                                 Bitmap bitmap) {
        this.bitmap = bitmap;
        this.name = name;
        this.time = time;
        this.amout = number;
        this.check = check;
        this.description = description;
        this.id = logId;
        this.saveBitmap = saveBitmap;
    }

}