import java.util.concurrent.ThreadLocalRandom;
import java.util.ArrayList;
import java.util.List;


public abstract class Sensor {
    private String id;
    private String ubicacion;
    private boolean activo;

    public Sensor(String id, String ubicacion, boolean activo) {
        this.id = id;
        this.ubicacion = ubicacion;
        this.activo = activo;
    }

    public abstract double tomarLectura();

    public String getId() {
        return id;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

   public String evaluarEstado() {
    if (activo) return "Operativo";
    return "Inactivo";
    }
}


public class SensorHumedadSuelo extends Sensor {
    private double humedadPct;

    public SensorHumedadSuelo(String id, String ubicacion, boolean activo) {
        super(id, ubicacion, activo);
        this.humedadPct = 0.0;
    }

    @Override
    public double tomarLectura() {
        this.humedadPct = ThreadLocalRandom.current().nextDouble(0.0, 100.0);
        return this.humedadPct;
    }

    public double getHumedadPct() {
        return humedadPct;
    }
}



public class EstacionMonitoreo {
    private List<Sensor> sensores;

    public EstacionMonitoreo() {
        this.sensores = new ArrayList<>();
    }

    public void agregarSensor(Sensor sensor) {
        if (sensor != null) {
            this.sensores.add(sensor);
        }
    }

    public void procesarLecturas() {
        for (Sensor sensor : sensores) {
            if (sensor.isActivo()) {
                double lectura = sensor.tomarLectura();
                System.out.println("Sensor [" + sensor.getId() + "] en " + sensor.getUbicacion() 
                        + " - Lectura: " + String.format("%.2f", lectura));
            }
        }
    }

    public List<Sensor> obtenerSensoresCriticos() {
        List<Sensor> criticos = new ArrayList<>();
        
        for (Sensor sensor : sensores) {
            if (!sensor.isActivo()) continue;

            double lectura = sensor.tomarLectura();
            
            if (sensor instanceof SensorHumedadSuelo && lectura < 20.0) {
                criticos.add(sensor);
            } else if (sensor instanceof SensorTemperatura && lectura > 38.0) {
                criticos.add(sensor);
            }
        }
        
        return criticos;
    }

    public List<Sensor> getSensores() {
        return sensores;
    }
}
