package com.doiry.baoxiaobao.beans;
import com.doiry.baoxiaobao.R;

import java.util.ArrayList;
import java.util.List;

public class BindedListviewBeans {
    public int image; // 行星图标
    public String name; // 行星名称
    public String Emp_number; // 行星描述

    public BindedListviewBeans(int image, String name, String number) {
        this.image = image;
        this.name = name;
        this.Emp_number = number;
    }

    private static int[] iconArray = {
            R.drawable.cat,
            R.drawable.home_image,
            R.drawable.home_image,
            R.drawable.home_image,
            R.drawable.cat,
            R.drawable.cat
    };
    private static String[] nameArray = {
            "0000",
            "0000",
            "0000",
            "0000",
            "35436"
    };
    private static String[] numberArray = {
            "0000",
            "0000",
            "0000",
            "0000",
            "35436"
    };

    public static List<BindedListviewBeans> getDefaultList() {
        List<BindedListviewBeans> bindedListviewBeansList = new ArrayList<BindedListviewBeans>();
        for (int i = 1; i < iconArray.length; i++) {
            bindedListviewBeansList.add(new BindedListviewBeans(iconArray[i - 1], nameArray[i - 1], numberArray[i - 1]));
        }
        return bindedListviewBeansList;
    }
}