package es.bahiasoftware.bstrack.iot;

import java.sql.Timestamp;

public class IoTEvent {

    private final IotEventType type;
    private final Timestamp time;
    private final Object data;

    public  IoTEvent(IotEventType type, Timestamp time, Object data){
        this.type = type;
        this.time = time;
        this.data = data;
    }

    public IotEventType getType() {
        return type;
    }

    public Timestamp getTime() {
        return time;
    }

    public Object getData() {
        return data;
    }
}
