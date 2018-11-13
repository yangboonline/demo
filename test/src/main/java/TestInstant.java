import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author yangbo
 * @date 2018/6/4
 */
public class TestInstant {

    public static void test0() {
        MultiMapTest.User user1 = MultiMapTest.User.builder().age("1").name("1").phone("1").build();
        MultiMapTest.User user2 = MultiMapTest.User.builder().age("2").name("2").phone("2").build();
        MultiMapTest.User user3 = MultiMapTest.User.builder().age("3").name("3").phone("3").build();
        MultiMapTest.User user4 = MultiMapTest.User.builder().age("4").name("4").phone("4").build();
        List<MultiMapTest.User> list = ImmutableList.of(user1, user2, user3, user4);
        Map<String, MultiMapTest.User> collect = list.stream().collect(Collectors.toMap(MultiMapTest.User::getAge, Function.identity()));
        System.out.println();
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

    public static void test3() {
        LocalDate localDate = LocalDate.of(2018, Month.NOVEMBER, 11);
        System.out.println(localDate.until(LocalDate.now(), ChronoUnit.DAYS));
    }

    public static void test4() {
        ZoneOffset zoneOffset = ZoneOffset.ofHours(-12);
        LocalDate startDate = LocalDateTime.now(zoneOffset.normalized()).plusDays(-1).toLocalDate();
        LocalDate endDate = LocalDateTime.now(zoneOffset.normalized()).plusDays(364).toLocalDate();

        List<LocalDate> list = Lists.newArrayList();
        list.add(startDate);
        while (startDate.isBefore(endDate)) {
            startDate = startDate.plusDays(1);
            list.add(startDate);
        }
        System.out.println();
    }

}
