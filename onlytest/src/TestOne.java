import org.junit.Test;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class TestOne {
    public enum PostmanState {
        AVAILABLE;
    }

    @Test
    public void test1() {
        int n = 3;
        for (int i = 3; i < n; i++) {
            System.out.println("qwe");
        }
    }

    @Test
    public void test2() {
        int[] n = null;
        System.out.println(n);
    }

    @Test
    public void test3() {
        System.out.println(PostmanState.AVAILABLE);
    }

    @Test
    public void test4() {
        Map<String, String> stringStringHashMap = new HashMap<>();
        String s = stringStringHashMap.get("asdf");
        System.out.println(s);

    }
    @Test
    public void test5(){
        int n;
        System.out.println();
    }
    @Test
    public void test6(){
        LocalDateTime t1 = LocalDateTime.of(2022, 11, 11, 11, 11);
        LocalDateTime t2 = LocalDateTime.of(2022, 11, 11, 11, 11);
        System.out.println(t1.isAfter(t2));
    }
}

