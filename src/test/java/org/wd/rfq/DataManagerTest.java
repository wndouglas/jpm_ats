package org.wd.rfq;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DataManagerTest {
    private DataManager dataManager;

    @BeforeEach
    void setDataManager() {
        dataManager = new DataManager();
    }

    @Test
    void dataManagerStoresValues() {
        final Double bondYield = 1.2542;
        final Double swapSpread = 1.4314;

        dataManager.setMid(DataManager.BOND_YIELD_KEY, bondYield);
        dataManager.setMid(DataManager.SWAP_SPREAD_KEY, swapSpread);

        assertEquals(dataManager.getMid(DataManager.BOND_YIELD_KEY), bondYield);
        assertEquals(dataManager.getMid(DataManager.SWAP_SPREAD_KEY), swapSpread);
    }

    @Test
    void dataManagerIsInitiallyEmpty() {
        assertNull(dataManager.getMid(DataManager.BOND_YIELD_KEY));
        assertNull(dataManager.getMid(DataManager.SWAP_SPREAD_KEY));
    }

    @Test
    void dataManagerReturnsSwapRate() {
        final Double bondYield = 1.2542;
        final Double swapSpread = 1.4314;

        final Double swapRate = bondYield + swapSpread;

        dataManager.setMid(DataManager.BOND_YIELD_KEY, bondYield);
        dataManager.setMid(DataManager.SWAP_SPREAD_KEY, swapSpread);

        assertEquals(swapRate, dataManager.getSwapRateMid());
    }
}
