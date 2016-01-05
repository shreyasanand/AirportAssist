package wireless.uta.com.airportAssist.dataobjects;

/**
 * Enum: Status
 * Enumerated data object to hold the Flight status codes and their meanings.
 *
 * Author: Shreyas
 */
public enum Status {
    ACTIVE,
    CANCELLED,
    DIVERTED,
    DATA_SOURCE_NEEDED,
    LANDED,
    NOT_OPERATIONAL,
    REDIRECTED,
    SCHEDULED,
    UNKNOWN;

    public static Status get(String code){
        switch(code) {
            case "A": return ACTIVE;
            case "C": return CANCELLED;
            case "D": return DIVERTED;
            case "DN": return DATA_SOURCE_NEEDED;
            case "L": return LANDED;
            case "NO": return NOT_OPERATIONAL;
            case "R": return REDIRECTED;
            case "S": return SCHEDULED;
            case "U": return UNKNOWN;
            default: return null;
        }
    }

}
