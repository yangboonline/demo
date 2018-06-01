import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.GsonBuilder;

import java.util.List;
import java.util.Map;

/**
 * @author yangbo
 * @date 2018/5/31
 */
public class MultiMapTest {

    public static void main(String[] args) {
        User user02 = User.builder().name("1").age("3").phone("7").build();
        User user11 = User.builder().name("2").age("6").phone("13").build();
        User user03 = User.builder().name("1").age("3").phone("8").build();
        User user06 = User.builder().name("1").age("4").phone("10").build();
        User user07 = User.builder().name("2").age("5").phone("11").build();
        User user05 = User.builder().name("1").age("4").phone("9").build();
        User user08 = User.builder().name("2").age("5").phone("11").build();
        User user04 = User.builder().name("1").age("4").phone("9").build();
        User user10 = User.builder().name("2").age("6").phone("13").build();
        User user01 = User.builder().name("1").age("3").phone("7").build();
        User user09 = User.builder().name("2").age("5").phone("12").build();
        User user12 = User.builder().name("2").age("6").phone("14").build();
        List<User> list = ImmutableList.of(user01, user02, user03, user04, user05, user06, user07, user08, user09, user10, user11, user12);

        Map<String, Map<String, Map<String, List<User>>>> first = Maps.newHashMap();
        list.forEach(user -> {
            Map<String, Map<String, List<User>>> second = first.get(user.getName());
            if (null == second) {
                second = Maps.newHashMap();
                Map<String, List<User>> third = Maps.newHashMap();
                List<User> users = Lists.newArrayList();
                first.put(user.getName(), second);
                second.put(user.getAge(), third);
                third.put(user.getPhone(), users);
                users.add(user);
            } else {
                Map<String, List<User>> third = second.get(user.getAge());
                if (null == third) {
                    third = Maps.newHashMap();
                    List<User> users = Lists.newArrayList();
                    second.put(user.getAge(), third);
                    third.put(user.getPhone(), users);
                    users.add(user);
                } else {
                    List<User> users = third.get(user.getPhone());
                    if (null == users) {
                        users = Lists.newArrayList();
                        third.put(user.getPhone(), users);
                    }
                    users.add(user);
                }
            }
        });
        System.out.println(new GsonBuilder().setPrettyPrinting().create().toJson(first));
    }

}
