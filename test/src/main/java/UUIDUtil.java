import cn.hutool.core.lang.UUID;

/**
 * @author yangbo
 * @date 2021/5/29
 */
public class UUIDUtil {

    /**
     * 返回此 UUID 的 128 位值中的最高有效 64 位 无符号值
     *
     * @param key 指定 {@code UUID} 字符串
     * @return 此 UUID 的 128 位值中的最高有效 64 位 无符号值
     */
    public static long getUnsignedMostSignBits(String key) {
        return UUID.fromString(key).getMostSignificantBits() & Long.MAX_VALUE;
    }

    /**
     * 返回此 UUID 的 128 位值中的最低有效 64 位 无符号值
     *
     * @param key 指定 {@code UUID} 字符串
     * @return 此 UUID 的 128 位值中的最低有效 64 位 无符号值
     */
    public static long getUnsignedLeastSignBits(String key) {
        return UUID.fromString(key).getLeastSignificantBits() & Long.MAX_VALUE;
    }

    /**
     * 返回此 UUID 的 128 位值中的最高有效 64 位 无符号值
     *
     * @param uuid 指定 {@code UUID}
     * @return 此 UUID 的 128 位值中的最高有效 64 位 无符号值
     */
    public static long getUnsignedMostSignBits(UUID uuid) {
        return uuid.getMostSignificantBits() & Long.MAX_VALUE;
    }

    /**
     * 返回此 UUID 的 128 位值中的最低有效 64 位 无符号值
     *
     * @param uuid 指定 {@code UUID}
     * @return 此 UUID 的 128 位值中的最低有效 64 位 无符号值
     */
    public static long getUnsignedLeastSignBits(UUID uuid) {
        return uuid.getLeastSignificantBits() & Long.MAX_VALUE;
    }

}
