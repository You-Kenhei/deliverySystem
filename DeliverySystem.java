import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.time.LocalDateTime;

public class DeliverySystem {
    public LinkedList<Request> requests;
    public LocalDateTime globalTime;
    public HashMap<String, Postman> postmanMap;
    public HashMap<String, Restaurant> restaurantMap;
    public DateTimeFormatter dTPrintFormatter;

    /**
     * システムの初期化
     */
    public DeliverySystem() {
        requests = new LinkedList<Request>();
        postmanMap = new HashMap<String, Postman>();
        restaurantMap = new HashMap<String, Restaurant>();
        dTPrintFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    }

    /**
     * リクエストのタイプに基づいて処理を行う
     */
    public void run() throws FileNotFoundException {
        getInput();
        while (!requests.isEmpty()) {
            Request req = requests.removeFirst();
            globalTime = req.DateTime;
            switch (req.type) {
                case "set_available" -> {
                    changeState(req, PostmanState.AVAILABLE);
                    break;
                }
                case "set_unavailable" -> {
                    changeState(req, PostmanState.UNAVAILABLE);
                    break;
                }
                case "set_max_delivery_time" -> {
                    Postman postman = postmanMap.get(req.id);
                    if (postman == null) {
                        Postman newPostman = new Postman(PostmanState.UNAVAILABLE, req.id, null);
                        postmanMap.put(req.id, newPostman);
                        newPostman.maxDeliveryTime = req.maxDeliveryTime;
                    }
                    break;
                }
                case "order" -> {
                    tryAssign(req);
                    break;
                }
                case "calculate_sales" -> {
                    Restaurant restaurant = req.restaurant;
                    int sales = restaurant.calculateIncomes(req.dateTimeDuration);
                    System.out.println(globalTime.format(dTPrintFormatter) + " SALES " + sales);
                    break;
                }
                case "calculate_wages" -> {
                    String id = req.id;
                    int wages = postmanMap.get(id).calculateIncomes(req.dateTimeDuration);
                    System.out.println(globalTime.format(dTPrintFormatter) + " WAGES " + wages);
                    break;
                }
            }
        }
    }

    /**
     * 注文の割り当て
     */
    public void tryAssign(Request req) {
        if (!req.restaurant.isOpening(globalTime)) {
            System.out.println(globalTime.format(dTPrintFormatter) + " ERROR CLOSED TIME");
            return;
        }

        Postman bestOne = null;
        Integer bestDistance = null;
        for (Postman postman : postmanMap.values()) {
            if (postman.state == PostmanState.AVAILABLE) {
                int toRestaurant = getDistance(postman.location, req.restaurant.location);
                int distance = toRestaurant + getDistance(req.restaurant.location, req.location);
                int timeNeed = getTimeNeed(distance);
                if (postman.maxDeliveryTime == 0 || timeNeed <= postman.maxDeliveryTime) {
                    LocalDateTime tempTime = getFinishTime(getTimeNeed(toRestaurant));
                    if (req.restaurant.isOpening(tempTime)) {
                        if (bestDistance == null || distance < bestDistance) {
                            bestOne = postman;
                            bestDistance = distance;
                        } else if (distance == bestDistance) {
                            bestOne = postman.waitTime.isBefore(bestOne.waitTime) ? postman : bestOne;
                            bestDistance = distance;
                        }
                    }
                }
            }
        }

        if (bestOne != null) {
            int deliveryPrice = getDeliveryPrice(bestDistance);
            System.out.println(globalTime.format(dTPrintFormatter) + " " + bestOne.id + " " + deliveryPrice);

            bestOne.addWages(globalTime, deliveryPrice);
            req.restaurant.addSales(globalTime, req.price - deliveryPrice);
            bestOne.state = PostmanState.UNAVAILABLE;
            return;
        }
        System.out.println(globalTime.format(dTPrintFormatter) + " ERROR NO DELIVERY PERSON");
    }

    /**
     * 離に基づいて必要な時間を計算する
     */
    public int getTimeNeed(int distance) {
        return (int) Math.ceil(distance / (10000.0 / 60));
    }

    /**
     * 2点間の距離を計算する
     */
    public int getDistance(int[] from, int[] to) {
        return Math.abs(from[0] - to[0]) + Math.abs(from[1] - to[1]);
    }

    /**
     * timeNeed時間後の時刻を計算する
     */
    public LocalDateTime getFinishTime(int timeNeed) {
        return globalTime.plusMinutes(timeNeed);
    }

    /**
     * 送料を計算する
     */
    public int getDeliveryPrice(int distance) {
        if (0 <= distance && distance < 100) {
            return 300;
        } else if (100 <= distance && distance < 1000) {
            return 600;
        } else if (1000 <= distance && distance < 10000) {
            return 900;
        } else {
            return 1200;
        }
    }

    /**
     * 配達員の状態を変更する
     */
    private void changeState(Request req, PostmanState reqState) {
        Postman postman = postmanMap.get(req.id);
        if (postman == null) {
            postman = new Postman(PostmanState.AVAILABLE, req.id, req.location);
            postmanMap.put(req.id, postman);
        }
        if (reqState == PostmanState.AVAILABLE) {
            postman.location = req.location;
            postman.state = PostmanState.AVAILABLE;
            postman.waitTime = globalTime;
        } else if (postman.state != PostmanState.AVAILABLE) {
            System.out.println(globalTime.format(dTPrintFormatter) + " ERROR CANNOT SET UNAVAILABLE");
        } else {
            postman.state = PostmanState.UNAVAILABLE;
        }

    }

    /**
     * 入力をリクエストに解析する
     */
    private void getInput() throws FileNotFoundException {
        Scanner sc = new Scanner(System.in);
        DateTimeFormatter tFormatter = DateTimeFormatter.ofPattern("HH:mm");
        DateTimeFormatter dTFormatter = DateTimeFormatter.ofPattern("yyyy-MM-ddHH:mm");
        int restNum = sc.nextInt();
        sc.nextLine();
        for (int i = 0; i < restNum; i++) {
            String[] raw = sc.nextLine().split(" ");
            String restId = raw[0];
            int[] location = {Integer.parseInt(raw[1]), Integer.parseInt(raw[2])};
            LocalTime[][] timeArr = new LocalTime[raw.length - 3][2];
            for (int j = 0; j < raw.length - 3; j++) {
                String[] times = raw[j + 3].split("-");
                timeArr[j][0] = LocalTime.parse(times[0], tFormatter);
                timeArr[j][1] = LocalTime.parse(times[1], tFormatter);
            }
            restaurantMap.put(restId, new Restaurant(restId, location, timeArr));
        }
        while (sc.hasNext()) {
            String[] raw = sc.nextLine().split(" ");
            LocalDateTime dateTime = LocalDateTime.parse(raw[0] + raw[1], dTFormatter);
            String type = raw[2];
            String id = raw[3];
            switch (type) {
                case "order" -> {
                    int[] location = {Integer.parseInt(raw[5]), Integer.parseInt(raw[6])};
                    requests.add(new Request(dateTime, type, restaurantMap.get(id), Integer.parseInt(raw[4]), location));
                    break;
                }
                case "set_available" -> {
                    int[] location = {Integer.parseInt(raw[4]), Integer.parseInt(raw[5])};
                    requests.add(new Request(dateTime, type, id, location, PostmanState.AVAILABLE));
                    break;

                }
                case "set_unavailable" -> {
                    requests.add(new Request(dateTime, type, id, PostmanState.UNAVAILABLE));
                    break;
                }
                case "set_max_delivery_time" -> {
                    requests.add(new Request(dateTime, type, id, Integer.parseInt(raw[4])));
                    break;
                }
                default -> {
                    LocalDateTime from = LocalDateTime.parse(raw[4] + raw[5], dTFormatter);
                    LocalDateTime to = LocalDateTime.parse(raw[6] + raw[7], dTFormatter);
                    LocalDateTime[] duration = {from, to};
                    if (type.equals("calculate_sales")) {
                        requests.add(new Request(dateTime, type, restaurantMap.get(id), duration));
                    } else {
                        requests.add(new Request(dateTime, type, id, duration));
                    }
                }
            }
        }
    }
}
