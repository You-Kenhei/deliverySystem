import java.time.LocalDateTime;

/**
 * 請求書をフォーマットするためのクラス
 */
public class Income {
    public LocalDateTime dateTime;
    public int money;

    public Income(LocalDateTime dateTime, int money) {
        this.dateTime = dateTime;
        this.money = money;
    }
}
