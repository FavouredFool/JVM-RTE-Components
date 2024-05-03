package org.task1;

import java.util.List;

public class CacheProxy implements ICaching {

    // This class always provides HotelRetrieval with Methods that can be called, because it is never null. It has a reference to the real org.task1.Cache.

    ICaching _subject;

    @Override
    public void cacheResult(String key, List<Hotel> hotelsToCache) {

        if(_subject == null) return;

        _subject.cacheResult(key, hotelsToCache);
    }

    @Override
    public List<Hotel> getResult(String key) {

        if(_subject == null) return null;

        return _subject.getResult(key);
    }

    @Override
    public void clearCache() {

        if(_subject == null) return;

        _subject.clearCache();
    }

    @Override
    public boolean containsKey(String key) {

        if(_subject == null) return false;

        return _subject.containsKey(key);
    }
}
