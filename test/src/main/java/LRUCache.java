import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @authon yangbo
 * @date 2019/8/25
 */
public class LRUCache<K, V> extends LinkedHashMap<K, V> {

    private static final int MAX_ENTRIES = 100;

    private LRUCache() {
        super(MAX_ENTRIES, 0.75f, true);
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > MAX_ENTRIES;
    }

}
