import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * 配達員
 */

public class Postman extends EndUser {
    public PostmanState state;
    public String id;
    public int[] location;
    public int maxDeliveryTime;
    public LocalDateTime waitTime;

    public Postman(PostmanState state, String id, int[] location) {
        this.state = state;
        this.id = id;
        this.location = location;
        this.maxDeliveryTime = 0;
        this.incomes = new ArrayList<Income>();
    }

    public void addWages(LocalDateTime dateTime, int money) {
        this.incomes.add(new Income(dateTime, money));
    }

}
