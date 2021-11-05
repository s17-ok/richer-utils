import java.sql.Timestamp;
import java.util.Date;

/**
 * @author richer.sun
 * @description test
 * @date 2021/11/4 6:46 下午
 */
public class Test {
    public static void main(String[] args) {
        long currentTimeMillis = System.currentTimeMillis();
        System.out.println(currentTimeMillis);
        Timestamp timestamp = new Timestamp(currentTimeMillis);
        System.out.println(timestamp);

    }
}
