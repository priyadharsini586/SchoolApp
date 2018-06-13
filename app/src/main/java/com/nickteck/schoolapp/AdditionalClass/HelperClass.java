package com.nickteck.schoolapp.AdditionalClass;

import java.util.regex.Pattern;

/**
 * Created by admin on 6/13/2018.
 */

public class HelperClass {

    public static boolean isValidMobile(String phone) {
        boolean check=false;
        if(!Pattern.matches("[a-zA-Z]+", phone)) {
            if(phone.length() < 6 || phone.length() > 13) {
                // if(phone.length() != 10) {
                check = false;

            } else {
                check = true;
            }
        } else {
            check=false;
        }
        return check;
    }
}
