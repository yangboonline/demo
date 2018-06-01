import lombok.Builder;
import lombok.Data;
import lombok.ToString;

/**
 * @author yangbo
 * @date 2018/5/30
 */
@Data
@Builder
@ToString
public class User {

    private String name;
    private String age;
    private String phone;

}
