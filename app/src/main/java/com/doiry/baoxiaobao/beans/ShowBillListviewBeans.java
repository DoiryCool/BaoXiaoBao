package com.doiry.baoxiaobao.beans;

import java.util.ArrayList;
import java.util.List;

public class ShowBillListviewBeans {
    public String image;
    public String name;
    public String amout;
    public String description;

    public ShowBillListviewBeans(String image, String name, String number, String description) {
        this.image = image;
        this.name = name;
        this.amout = number;
        this.description = description;
    }

    private static String[] iconArray = {
            "0000",
            "0000",
            "54",
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

    private static String[] numberArray = {
            "0000",
            "0000",
            "3242",
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