package org.task1;

import java.util.List;

public interface ICaching {
    void cacheResult(String key, List<Hotel> hotelsToCache);
    List<Hotel> getResult(String key);
    void clearCache();
    boolean containsKey(String key);
}
