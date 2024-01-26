package com.aisys.controller.admin;

import java.util.Calendar;
import java.util.Random;

public class Test {
    public static String generateRandomFilename(){
        String RandomFilename = "";
        Random rand = new Random();//生成随机数
        int random = rand.nextInt();

        Calendar calCurrent = Calendar.getInstance();
        int intDay = calCurrent.get(Calendar.DATE);
        int intMonth = calCurrent.get(Calendar.MONTH) + 1;
        int intYear = calCurrent.get(Calendar.YEAR);
        int intHour= calCurrent.get(Calendar.HOUR);
        int intMinute=calCurrent.get(Calendar.MINUTE);
        int intSecond= calCurrent.get(Calendar.SECOND);
        int intMillisecond=calCurrent.get(Calendar.MILLISECOND);
        String now = String.valueOf(intYear) + "_" + String.valueOf(intMonth) + "_" +
                String.valueOf(intDay) + "_"+String.valueOf(intHour)+
                String.valueOf(intMinute)+String.valueOf(intSecond)+
                String.valueOf(intMillisecond)+"_";

        RandomFilename = now + String.valueOf(random > 0 ? random : ( -1) * random);

        return RandomFilename;
    }

    public static void main(String[] args) {
        System.out.println(Test.generateRandomFilename());
    }
}
