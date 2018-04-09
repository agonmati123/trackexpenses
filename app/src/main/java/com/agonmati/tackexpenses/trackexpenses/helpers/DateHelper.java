package com.agonmati.tackexpenses.trackexpenses.helpers;

import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

/**
 * Created by agonmati on 2/11/18.
 */

public class DateHelper {
    public static boolean isSameMonth(String d1,String d2){
//
        if (d1.split(" ")[1].equals(d2.split(" ")[1])){
            return true;
        }
        return false;
    }




}
