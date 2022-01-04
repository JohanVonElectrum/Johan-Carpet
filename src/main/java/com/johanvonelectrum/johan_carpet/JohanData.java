package com.johanvonelectrum.johan_carpet;

import com.johanvonelectrum.johan_carpet.models.CsInfo;

import java.util.HashMap;

public class JohanData {
    private static JohanData instance = null;
    private HashMap<String, CsInfo> csInfoMap = new HashMap<>();

    public static JohanData getInstance() {
        if (instance == null) {
            instance = new JohanData();
        }

        return instance;
    }
    public void setCsInfoMap(HashMap<String, CsInfo> csInfoMap) {
        this.csInfoMap = csInfoMap;
    }
    public HashMap<String, CsInfo> getCsInfoMap() {
        return csInfoMap;
    }
}
