package org.task1;

public class HotelRetrievalCacheProxy implements ICacheReference {

    ICacheReference _subject;

    public HotelRetrievalCacheProxy(HotelRetrieval subject) {
        _subject = subject;
    }

    @Override
    public void SetCacheReference(ICaching cache) {
        _subject.SetCacheReference(cache);
    }

    @Override
    public void ClearCache() {
        _subject.ClearCache();
    }
}
