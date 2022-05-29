package com.doiry.baoxiaobao.beans;
import com.doiry.baoxiaobao.R;

import java.util.ArrayList;
import java.util.List;

public class BindedListviewBeans {
    public String image;
    public String name;
    public String Emp_number;

    public BindedListviewBeans(String image, String name, String number) {
        this.image = image;
        this.name = name;
        this.Emp_number = number;
    }

    private static String[] iconArray = {
            "sss",
            "sss",
            "sss",
            "sss",
            "sss",
            "sss",
            "sss",
            "sss",
            "sss",
            "sss"
    };
    private static String[] nameArray = {
            "0000",
            "0000",
            "0000",
            "0000",
            "0000",
            "0000",
            "0000",
            "0000",
            "35436",
            "35436"
    };
    private static String[] numberArray = {
            "0000",
            "0000",
            "0000",
            "0000",
            "0000",
            "0000",
            "0000",
            "0000",
            "35436",
            "35436"
    };

    public static List<BindedListviewBeans> getDefaultList() {
        List<BindedListviewBeans> bindedListviewBeansList = new ArrayList<BindedListviewBeans>();
        for (int i = 1; i < iconArray.length; i++) {
            bindedListviewBeansList.add(new BindedListviewBeans(iconArray[i], nameArray[i], numberArray[i]));
        }
        return bindedListviewBeansList;
    }
}