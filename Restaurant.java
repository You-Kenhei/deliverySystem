import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

/**
 * レストラン
 */
public class Restaurant extends EndUser {
    public String id;
    public int[] location;
    public LocalTime[][] openingHours;

    public Restaurant(String id, int[] location, LocalTime[][] openingHours) {
        this.id = id;
        this.location = location;
        this.openingHours = openingHours;
        this.incomes = new ArrayList<Income>();
    }

    /**
     * 指定された時刻においてこのレストランが営業中かどうかを確認する
     */
    public boolean isOpening(LocalDateTime pickUpDateTime) {
        if (this.openingHours.length == 0) return true;
        LocalTime pickUpTime = pickUpDateTime.toLocalTime();
        for (LocalTime[] openingHour : openingHours) {
            if ((pickUpTime.isAfter(openingHour[0]) || pickUpTime.equals(openingHour[0])) && pickUpTime.isBefore(openingHour[1])) {
                return false;
            }
        }
        return true;
    }

    public void addSales(LocalDateTime dateTime, int money) {
        this.incomes.add(new Income(dateTime, money));
    }
}
