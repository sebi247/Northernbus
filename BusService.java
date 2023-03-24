import java.math.BigDecimal;
import java.sql.Timestamp;

public class BusService {
    private String departureLocation;
    private String arrivalLocation;
    private Timestamp departureTime;
    private Timestamp arrivalTime;
    private BigDecimal price;

    public BusService(String departureLocation, String arrivalLocation, Timestamp departureTime, Timestamp arrivalTime, BigDecimal price) {
        this.departureLocation = departureLocation;
        this.arrivalLocation = arrivalLocation;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.price = price;
    }

    public String getDepartureLocation() {
        return departureLocation;
    }

    public String getArrivalLocation() {
        return arrivalLocation;
    }

    public Timestamp getDepartureTime() {
        return departureTime;
    }

    public Timestamp getArrivalTime() {
        return arrivalTime;
    }

    public BigDecimal getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return "BusService{" +
                "departureLocation='" + departureLocation + '\'' +
                ", arrivalLocation='" + arrivalLocation + '\'' +
                ", departureTime=" + departureTime +
                ", arrivalTime=" + arrivalTime +
                ", price=" + price +
                '}';
    }
}
