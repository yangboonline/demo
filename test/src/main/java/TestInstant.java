import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author yangbo
 * @date 2018/6/4
 */
public class TestInstant {

    public static void main(String[] args) {
        ZonedDateTime zonedDateTime = Instant.ofEpochMilli(1528076720000L).atZone(ZoneId.systemDefault());
        LocalDateTime localDateTime = zonedDateTime.toLocalDateTime();
        String formatDateTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(localDateTime);
        System.out.println(formatDateTime);
    }

}
