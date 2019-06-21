package com.yjp.erp.util;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author wyf
 * @date 2019/4/2 下午 1:39
 **/
public class CookieUtils {

    public static void deleteCookie(String key, HttpServletRequest request, HttpServletResponse response){
        Cookie[] allCookie= request.getCookies();
        if(allCookie!=null&&allCookie.length!=0)
        {
            for(int i=0;i<allCookie.length;i++)
            {
                String keyname= allCookie[i].getName();
                if(key.equals(keyname))
                {
                    allCookie[i].setValue("");
                    allCookie[i].setMaxAge(0);
                    response.addCookie(allCookie[i]);
                }
            }
        }
    }
    public static void addCookie(String key,String value, HttpServletResponse response, int time){
        addCookie(key,value,false, response, time);
    }
    public static void addCookie(String key,String value, HttpServletResponse response){
        addCookie(key,value,false, response,60*60*24*7);
    }
    public static void addCookie(String key,String value,boolean jiami, HttpServletResponse response){
        addCookie(key,value,jiami, response,60*60*24*7);
    }
    public static void addCookie(String key,String value,boolean jiami, HttpServletResponse response, int time){
        if(jiami){
            value = Aes.encrypt(value);
        }else{
            value =new String(Base64Utils.encode(value.getBytes()));
        }
        Cookie myCookie=new Cookie(key,value);
        myCookie.setMaxAge(time);
        // 多个路径下可以共享cookie
        myCookie.setPath("/");
        response.addCookie(myCookie);
    }
    public static String getCookie(String key,HttpServletRequest request){
        return getCookie(key,false,request);
    }
    public static String getCookie(String key,boolean jiami, HttpServletRequest request){
        Cookie[] allCookie= request.getCookies();
        if(allCookie!=null&&allCookie.length!=0)
        {
            for(int i=0;i<allCookie.length;i++)
            {
                String keyname= allCookie[i].getName();
                if((key).equals(keyname))
                {
                    if(allCookie[i].getValue()==null) {
                        return "";
                    } else if(jiami) {
                        return Aes.desEncrypt(allCookie[i].getValue());
                    } else {
                        return new String(Base64Utils.decode(allCookie[i].getValue()));
                    }
                }

            }
        }
        return "";
    }
}
