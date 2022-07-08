package org.wd.rfq;

import com.google.common.collect.Maps;

import java.util.HashMap;
import java.util.Map;

public class DataManager {
    private Long currentTimestamp;
    private final Map<String, Double> currentMidMap;
    public static final String SWAP_SPREAD_KEY = "swapSpread";
    public static final String BOND_YIELD_KEY = "bondYield";

    private static final DataManager DATA_MANAGER = new DataManager();

    private DataManager() {
        currentMidMap = new HashMap<>();
        currentMidMap.put(SWAP_SPREAD_KEY, null);
        currentMidMap.put(BOND_YIELD_KEY, null);
        currentTimestamp = System.currentTimeMillis();
    }

    public static DataManager getInstance() {
        return DATA_MANAGER;
    }

    public Long getCurrentTimestamp() {
        return currentTimestamp;
    }

    synchronized public void setCurrentTimestamp(long timestamp) {
        currentTimestamp = timestamp;
    }

    synchronized public void setMid(String key, Double value) {
        currentMidMap.put(key, value);
    }

    public Double getSwapRateMid() {
        return currentMidMap.get(SWAP_SPREAD_KEY) + currentMidMap.get(BOND_YIELD_KEY);
    }
}
