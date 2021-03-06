package org.wd.rfq.concurrency;

import org.wd.rfq.DataManager;
import org.wd.rfq.event.RfqRequestEvent;
import org.wd.rfq.event.RfqResponseEvent;
import org.wd.rfq.logic.SpreadCalculator;

import java.util.concurrent.BlockingQueue;
import java.util.logging.Logger;

public class RfqResponseThread implements Runnable {
    private Thread t;
    private final String threadName;
    private final DataManager dataManager;
    private final BlockingQueue<RfqRequestEvent> eventQueue;
    private final SpreadCalculator spreadCalculator;

    private static final Logger LOGGER = Logger.getGlobal();

    public RfqResponseThread(String threadName, DataManager dataManager, BlockingQueue<RfqRequestEvent> eventQueue,
                             SpreadCalculator spreadCalculator) {
        LOGGER.info("Creating thread: " + threadName);
        this.threadName = threadName;
        this.dataManager = dataManager;
        this.eventQueue = eventQueue;
        this.spreadCalculator = spreadCalculator;
    }

    @Override
    public void run() {
        while(true) {
            try {
                RfqRequestEvent rfqRequestEvent = eventQueue.take();

                long rfqNotional = rfqRequestEvent.getNotional();
                String rfqId = rfqRequestEvent.getrequestId();

                Double currentSwapMid = dataManager.getSwapRateMid();
                Long currentSwapTimestamp = dataManager.getCurrentTimestamp();
                long currentTimestamp = System.currentTimeMillis();

                if (currentSwapMid == null || currentSwapTimestamp == null) {
                    LOGGER.info("Thread: " + threadName + ", no price availabe at timestamp: " + currentTimestamp + " - no RFQ response generated");
                }
                else {
                    RfqResponseEvent rfqResponseEvent = generateRfqResponse(rfqNotional, rfqId, currentSwapMid, currentTimestamp);
                    LOGGER.info("Thread: " + threadName + ", generated RFQ response: " + rfqResponseEvent);
                }
            } catch (InterruptedException e) {
                LOGGER.severe(e.toString());
            }
        }
    }

    private RfqResponseEvent generateRfqResponse(long rfqNotional, String requestId, double currentSwapMid, long currentTimestamp) {
        double spread = spreadCalculator.getSpread(rfqNotional);
        double paySwapRate = currentSwapMid + spread;
        double rcvSwapRate = currentSwapMid - spread;
        return new RfqResponseEvent(paySwapRate, rcvSwapRate, currentTimestamp, rfqNotional, requestId);
    }

    public void start() {
        LOGGER.info("Starting thread: " + threadName);
        if (t == null) {
            t = new Thread(this, threadName);
            t.start();
        }
    }

    @Override
    public String toString() {
        return "RfqResponseThread[" +
                "threadName: " + this.threadName +
                ", dataManager: " + this.dataManager +
                ", eventQueue: " + this.eventQueue +
                "]";
    }
}