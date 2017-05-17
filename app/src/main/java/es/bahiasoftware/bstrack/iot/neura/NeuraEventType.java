package es.bahiasoftware.bstrack.iot.neura;

import es.bahiasoftware.bstrack.iot.IoTEvent;
import es.bahiasoftware.bstrack.iot.IotEventType;

enum  NeuraEventType {

    ARRIVE_HOME("userArrivedHome", IotEventType.ARRIVE_HOME),
    LEFT_HOME("userLeftHome", IotEventType.LEFT_HOME),
    TO_HOME("userIsOnTheWayHome", IotEventType.TO_HOME),
    ARRIVE_WORK("userArrivedToWork", IotEventType.ARRIVE_WORK),
    LEFT_WORK("userLeftWork", IotEventType.LEFT_WORK),
    TO_WORK("userIsOnTheWayToWork", IotEventType.TO_WORK),
    START_WALKING("userStartedWalking", IotEventType.START_WALKING),
    END_WALKING("userFinishedWalking", IotEventType.END_WALKING),
    START_RUNNING("userStartedRunning", IotEventType.START_RUNNING),
    END_RUNNING("userFinishedRunning", IotEventType.END_RUNNING);

    private final String neuraName;
    private final IotEventType iotEventType;

    private NeuraEventType(String name, IotEventType type){
        this.neuraName = name;
        this.iotEventType = type;
    }

    public String getNeuraName(){
        return neuraName;
    }

    public String getIotTypeName(){
        return iotEventType.getName();
    }

    public static IotEventType getByName(String name){
        for(NeuraEventType type : values()){
            if(type.neuraName.equals(name)){
                return type.iotEventType;
            }
        }
        return null;
    }
}
