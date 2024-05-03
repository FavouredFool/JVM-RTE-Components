package org.task1;

import java.util.*;

public class Cache implements ICaching {

    Map<String, List<Hotel>> _cacheStructure = new HashMap<>();
    ICaching _proxy;

    public Cache() {
        _proxy = new CacheProxy();
    }

    @Override
    public void cacheResult(String key, List<Hotel> hotelsToCache) {
        _cacheStructure.put(key, hotelsToCache);
    }

    @Override
    public List<Hotel> getResult(String key) {
        return _cacheStructure.get(key);
    }

    @Override
    public void clearCache() {
        _cacheStructure.clear();
    }

    @Override
    public boolean containsKey(String key) {
        return _cacheStructure.containsKey(key);
    }

    public ICaching getCacheProxy() {
        return _proxy;
    }
}
