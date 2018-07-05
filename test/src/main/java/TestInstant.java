import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @author yangbo
 * @date 2018/6/4
 */
public class TestInstant {

    public static void main(String[] args) {
        LocalDate BASE_DATE = LocalDate.of(2018, 7, 1);
        LocalDate now = LocalDateTime.now().toLocalDate();
        System.out.println(now.toEpochDay() - BASE_DATE.toEpochDay());
    }

    public static void test1() {
        ZonedDateTime zonedDateTime = Instant.ofEpochMilli(1528076720000L).atZone(ZoneId.systemDefault());
        LocalDateTime localDateTime = zonedDateTime.toLocalDateTime();
        String formatDateTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(localDateTime);
        System.out.println(formatDateTime);
    }

    public static void test2() {
        LocalDateTime now = LocalDateTime.now();
        LocalDate localDate = now.toLocalDate().plusDays(1);
        Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        System.out.println(date);
        Instant instant = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant();
        System.out.println(Date.from(instant));
    }

}
