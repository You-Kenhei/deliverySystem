import java.time.LocalDateTime;

/**
 * リクエスト
 */
public class Request {
    public LocalDateTime DateTime;
    public String type;
    public String id = null;
    public Restaurant restaurant;
    public int[] location = null;
    public int price;
    public int maxDeliveryTime;
    public LocalDateTime[] dateTimeDuration = null;
    public PostmanState state;

    public Request(LocalDateTime dateTime, String type, String id, PostmanState postmanState) {
        DateTime = dateTime;
        this.type = type;
        this.id = id;
        this.state = postmanState;
    }

    public Request(LocalDateTime dateTime, String type, String id, int maxDeliveryTime) {
        DateTime = dateTime;
        this.type = type;
        this.id = id;
        this.maxDeliveryTime = maxDeliveryTime;
    }

    public Request(LocalDateTime dateTime, String type, String id, int[] location, PostmanState postmanState) {
        this.DateTime = dateTime;
        this.type = type;
        this.id = id;
        this.location = location;
        this.state = postmanState;
    }

    public Request(LocalDateTime dateTime, String type, Restaurant restaurant, int price, int[] location) {
        DateTime = dateTime;
        this.type = type;
        this.restaurant = restaurant;
        this.price = price;
        this.location = location;
    }

    public Request(LocalDateTime dateTime, String type, Restaurant restaurant, LocalDateTime[] dateTimeDuration) {
        DateTime = dateTime;
        this.type = type;
        this.restaurant = restaurant;
        this.dateTimeDuration = dateTimeDuration;
    }

    public Request(LocalDateTime dateTime, String type, String id, LocalDateTime[] dateTimeDuration) {
        DateTime = dateTime;
        this.type = type;
        this.id = id;
        this.dateTimeDuration = dateTimeDuration;
    }
}
