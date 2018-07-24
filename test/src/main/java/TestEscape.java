import org.apache.commons.lang3.StringEscapeUtils;

/**
 * @author yangbo
 * @date 2018/7/24
 */
public class TestEscape {

    public static void main(String[] args) {
        System.out.println(StringEscapeUtils.unescapeJava("a\nb\nc"));
    }

}
