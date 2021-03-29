package com.myweddi.conf;

import java.time.ZoneId;

public class Global {

    public static ZoneId zid = ZoneId.of("Europe/Warsaw");
    public final static String photosPath = "photos/";

    //public final static String appPath = System.getProperty("user.home") + "/myweddi/";
    public final static String appPath = "/home/piterk/myweddi/";
    public static final String domain = "http://localhost:8081";

//    public static final String domain = "http://80.211.245.217:8081/";
//    public final static String appPath = "home/cloud/myweddi/";
}
