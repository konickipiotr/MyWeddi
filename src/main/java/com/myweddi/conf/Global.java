package com.myweddi.conf;

import java.time.ZoneId;

public class Global {

    public static final String domain = "http://localhost:8080";
    public static ZoneId zid = ZoneId.of("Europe/Warsaw");
    public final static String appPath = System.getProperty("user.home") + "/myweddi/";
    public final static String photosPath = "photos/";
}
