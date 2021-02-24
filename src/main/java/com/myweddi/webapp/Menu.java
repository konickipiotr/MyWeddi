package com.myweddi.webapp;

import java.util.Map;
import java.util.TreeMap;

public class Menu {
    public static final Map<String, String> adminMenu = new TreeMap<>();
    public static final Map<String, String> ownerMenu = new TreeMap<>();
    public static final Map<String, String> guestMenu = new TreeMap<>();
    public static final Map<String, String> djMenu = new TreeMap<>();
    public static final Map<String, String> photojMenu = new TreeMap<>();
    static {
        adminMenu.put("home", "/admin/");

        guestMenu.put("home", "/");
        guestMenu.put("Informacje", "/");
        guestMenu.put("Galeria", "/");
        guestMenu.put("STOŁY", "/");
        guestMenu.put("PREZENTY", "/");
    }
}