import cn.hutool.core.lang.UUID;

/**
 * @author yangbo
 * @date 2021/5/28
 */
public class TestUUID {

    public static void main(String[] args) {
        String key = "583ec8d4-fb46-11e1-82cb-f4ce4684ea4c";
        key = "b3fa705a-f441-4e7e-a2e1-f87d9d8957e1";
        UUID uuid = UUID.fromString(key);
        System.out.println(UUIDUtil.getUnsignedMostSignBits(uuid));
    }

}
