package org.wd.rfq.event;

import static org.wd.rfq.RfqSimulator.NOTIONAL_UNIT;

public class RfqResponseEvent {
    private final double paySwapRate;
    private final double receiveSwapRate;
    private final long timestamp;
    private final long notional;
    private final String requestId;


    public RfqResponseEvent(double paySwapRate, double receiveSwapRate, long timestamp, long notional, String requestId) {
        this.paySwapRate = paySwapRate;
        this.receiveSwapRate = receiveSwapRate;
        this.timestamp = timestamp;
        this.notional = notional;
        this.requestId = requestId;
    }

    @Override
    public String toString() {
        long notionalMillions = this.notional/NOTIONAL_UNIT;
        return "RfqResponseEvent[" +
                "requestId: " + this.requestId +
                ", timestamp: " + this.timestamp +
                ", notional (m): " + notionalMillions +
                ", pay (client perspective): " + (long)Math.floor(this.paySwapRate*1e6)/1e4 + "%" +
                ", receive (client perspective): " + (long)Math.floor(this.receiveSwapRate*1e6)/1e4 + "%" +
                "]";
    }
}
