package com.doiry.baoxiaobao.beans;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

public class ShowBillListviewBeans {
    public Bitmap bitmap;
    public String name;
    public Float amout;
    public String description;

    public ShowBillListviewBeans(Bitmap bitmap, String name, Float number, String description) {
        this.bitmap = bitmap;
        this.name = name;
        this.amout = number;
        this.description = description;
    }

    private static Bitmap[] iconArray = {
    };

    private static String[] nameArray = {
            "0000",
            "0000",
            "4525",
            "0000",
            "35436",
            "35436",
            "35436",
            "35436",
            "35436",
            "35436",
            "35436",
            "35436"
    };

    private static Float[] numberArray = {
    };

    private static String[] descriptionArray = {
            "0000",
            "0000",
            "0000",
            "0000",
            "35436",
            "35436",
            "35436",
            "35436",
            "35436",
            "35436",
            "35436",
            "35436"
    };
    public static List<ShowBillListviewBeans> getDefaultList() {
        List<ShowBillListviewBeans> showBillListviewBeans = new ArrayList<ShowBillListviewBeans>();
        for (int i = 1; i < iconArray.length; i++) {
            showBillListviewBeans.add(new ShowBillListviewBeans(iconArray[i], nameArray[i], numberArray[i], descriptionArray[i]));
        }
        return showBillListviewBeans;
    }
}