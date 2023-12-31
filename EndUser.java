import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * エンドユーザー、配達員とレストランの親クラス
 */
public class EndUser {
    public ArrayList<Income> incomes;

    /**
     * エンドユーザーに統一された売上の集計機能を提供する
     */
    public int calculateIncomes(LocalDateTime[] dateTimes) {
        LocalDateTime from = dateTimes[0];
        LocalDateTime to = dateTimes[1];
        int result = 0;
        boolean littleTrick = false;
        for (Income income : incomes) {
            LocalDateTime dateTime = income.dateTime;
            if (littleTrick || from.isBefore(dateTime) || from.isEqual(dateTime)) {
                littleTrick = true;
            } else if (littleTrick && (to.isBefore(dateTime) || to.isEqual(dateTime))) {
                break;
            }
            result += income.money;
        }
        return result;
    }
}
