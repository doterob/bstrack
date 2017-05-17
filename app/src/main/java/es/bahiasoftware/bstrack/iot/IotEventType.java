package es.bahiasoftware.bstrack.iot;

public enum IotEventType {

    ARRIVE_HOME("userArrivedHome"),
    LEFT_HOME("userLeftHome"),
    TO_HOME("userIsOnTheWayHome"),
    ARRIVE_WORK("userArrivedToWork"),
    LEFT_WORK("userLeftWork"),
    TO_WORK("userIsOnTheWayToWork"),
    START_WALKING("userStartedWalking"),
    END_WALKING("userFinishedWalking"),
    START_RUNNING("userStartedRunning"),
    END_RUNNING("userFinishedRunning"),
    CURRENT_LOCATION("userCurrentLocation"),
    CONNECTED("connected"),
    DISCONNECTED("disconnected");

    private final String name;

    private IotEventType(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }
}
