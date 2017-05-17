package es.bahiasoftware.bstrack.iot;

import java.util.Observer;

public interface IoTListener {

    void notify(IoTEvent event);
}
