package com.myweddi.webapp;

import com.myweddi.utils.Duo;
import org.springframework.data.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Menu {
    public static final List<Duo> adminMenu = new ArrayList<>();
    public static final List<Duo> hostMenu = new ArrayList<>();
    public static final List<Duo> guestMenu = new ArrayList<>();
    public static final List<Duo> djMenu = new ArrayList<>();
    public static final List<Duo> photojMenu = new ArrayList<>();

    static {
        adminMenu.add(new Duo("home", "/admin/"));

        guestMenu.add(new Duo("Strona Główna", "/home"));
        guestMenu.add(new Duo("Informacje", "/info"));
        guestMenu.add(new Duo("Prezenty", "/guest/gift"));
        guestMenu.add(new Duo("Stoły", "/guest/tables"));

        hostMenu.add(new Duo("Strona Główna", "/home"));
        hostMenu.add(new Duo("Informacje", "/host/info"));
        hostMenu.add(new Duo("Prezenty", "/host/gift"));
        hostMenu.add(new Duo("Stoły", "/host/tables"));
        hostMenu.add(new Duo("Goście", "/host/guest"));




    }
}
