package com.example.davidhuang.bombregisterlogindemo;

import android.text.TextUtils;

/**
 * 附加输入合法性检测：虽然bmob SDK后台有合法性检测，
 * 但没有相应的密码检测、手机号检测表达式可能不是与我的
 * 想法一致、可以实现更清晰的不合法输入信息反馈
 */

public class InputLeagalCheck {
    /**
     *密码检测：6-16位字母或者数字，但不能是纯数字或纯密码
     */
    public static boolean isPassword(String input){
        String regex="^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,16}$";//密码正则表达式
        if(TextUtils.isEmpty(input)){
            return false;
        }else{
            return input.matches(regex);
        }
    }
    /**
     *手机号检测：第一位为1，后面10位0-9数字
     */
    public static boolean isPhoneNum(String input){
        String regex = "[1]\\d{10}";//手机号正则表达式
        if(TextUtils.isEmpty(input)){
            return false;
        }else{
            return input.matches(regex);
        }
    }
    /**
     *Email检测
     */
    public static boolean isEmail(String input){
        String regex = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";//Email正则表达式
        if(TextUtils.isEmpty(input)){
            return false;
        }else{
            return input.matches(regex);
        }
    }



}
