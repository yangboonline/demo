/**
 * @authon yangbo
 * @date 2019/8/25
 */
public class TestString {

    public static void main(String[] args) {
        String a = new String("a");
        a.intern();
        String b = "a";
        System.out.println(a == b);

        //String s0 = "aa";
        String s1 = new String("a") + new String("a");
        s1.intern();
        String s2 = "aa";
        System.out.println(s1 == s2);
    }

}
