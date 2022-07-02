package org.wd.rfq.event;

import java.util.UUID;

public class RfqRequestEvent {
    private final long notional;
    private final long timestamp;
    private final String requestId;

    public RfqRequestEvent(long notional, long timestamp) {
        this.notional = notional;
        this.timestamp = timestamp;
        this.requestId = UUID.randomUUID().toString();
    }

    public long getNotional() {
        return notional;
    }

    public String getrequestId() {
        return requestId;
    }

    @Override
    public String toString() {
        long notionalMillions = this.notional/1000000;
        return "RfqRequestEvent[requestId: " + requestId + ", timestamp: " + this.timestamp + ", notional (m): " + notionalMillions + "]";
    }
}
