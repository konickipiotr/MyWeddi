package com.myweddi.utils;

import com.myweddi.user.User;

public class AccessUtil {

    public static boolean accessDenied(User user, Long weddingid){
        return !user.getWeddingid().equals(weddingid);
    }

    public static boolean writeAccessDenied(User user, Long weddingid){
        if(!user.getWeddingid().equals(weddingid))
            return true;
        return !user.getRole().equals("HOST");
    }
}
