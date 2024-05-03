package org.task1;

import java.util.List;

public class HotelRetrieval implements IHotelSearch, ICacheReference {

    ICacheReference _cacheReferenceProxy;
    IHotelSearch _hotelSearchProxy;
    ICaching _cacheProxy;
    DBAccess _dbAccess;

    public HotelRetrieval() {
        _hotelSearchProxy = new HotelRetrievalProxy(this);
        _cacheReferenceProxy = new HotelRetrievalCacheProxy(this);
        _dbAccess = new DBAccess();
    }

    @Override
    public List<Hotel> getAllHotels() {

        String key = "all";
        if (_cacheProxy.containsKey(key)) {
            return _cacheProxy.getResult(key);
        }
        else {
            List<Hotel> result = _dbAccess.getObjects(DBAccess.SearchParameter.NAME, "*");
            _cacheProxy.cacheResult(key, result);
            return result;
        }
    }

    @Override
    public List<Hotel> getHotelsByName(String name) {
        String key = "name " + name;
        if (_cacheProxy.containsKey(key)) {
            return _cacheProxy.getResult(key);
        }
        else {
            List<Hotel> result = _dbAccess.getObjects(DBAccess.SearchParameter.NAME, name);
            _cacheProxy.cacheResult(key, result);
            return result;
        }

    }

    @Override
    public List<Hotel> getHotelsByCity(String city) {

        String key = "city " + city;
        if (_cacheProxy.containsKey(key)) {
            return _cacheProxy.getResult(key);
        }
        else {
            List<Hotel> result = _dbAccess.getObjects(DBAccess.SearchParameter.CITY, city);
            _cacheProxy.cacheResult(key, result);
            return result;
        }

    }

    @Override
    public List<Hotel> getHotelsByIndex(int index) {

        String key = "index " + index;
        if (_cacheProxy.containsKey(key)) {
            return _cacheProxy.getResult(key);
        }
        else {
            List<Hotel> result = _dbAccess.getObjects(DBAccess.SearchParameter.INDEX, index + "");
            _cacheProxy.cacheResult(key, result);
            return result;
        }
    }

    @Override
    public void openSession() {
        _dbAccess.openConnection();
    }

    @Override
    public void closeSession() {
        _dbAccess.closeConnection();
    }

    public IHotelSearch getHotelSearchProxy() {
        return _hotelSearchProxy;
    }

    public ICacheReference getCacheReferenceProxy(){
        return _cacheReferenceProxy;
    }

    @Override
    public void SetCacheReference(ICaching cache) {
        _cacheProxy = cache;
    }

    @Override
    public void ClearCache() {
        _cacheProxy.clearCache();
    }
}
