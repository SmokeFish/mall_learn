package com.mmall.common;

/**
 * Created by think9 on 19-12-1.
 */
public class Const {

    public static final String CURRENT_USER = "currentUser";
    public static final String EMAIL = "email";
    public static final  String USERNAME = "username";

    //A interface inClass whith a teem of constants
    public interface Role{
        int ROLE_CUSTOMER = 0;
        int ROLE_ADMIN = 1;
    }
}
