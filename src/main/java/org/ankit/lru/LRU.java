package org.ankit.lru;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Created by ankit on 22/12/16.
 */
public class LRU<T> {

    private Map<Long, T> storage;
    private long size;
    private LinkedList<Long> storageLocation;

    public LRU(long size) throws LRUException {
        super();
        if (size < 1) {
            throw new LRUException("LRU size too small");
        }
        this.size = size;
        this.storage = new HashMap<Long, T>();
        this.storageLocation = new LinkedList<Long>();
    }


    public LRU() {
        super();
        this.size = Long.MAX_VALUE;
        this.storage = new HashMap<Long, T>();
        this.storageLocation = new LinkedList<Long>();
    }

    public long getCacheMaxSize() {
        return size;
    }

    public long getCacheSize() {
        return storage.size();
    }

    public T get(long key) {
        T resp = storage.get(key);
        if (resp == null)
            return null;
        storageLocation.remove(key);
        storageLocation.addLast(key);
        return resp;
    }

    public boolean put(long key, T payload) {
        evict();
        T resp = get(key);
        if (resp == null)
            storageLocation.addLast(key);
        storage.put(key, payload);
        return true;
    }

    private void evict() {
        while (storage.size() > size) {
            Long junk = storageLocation.removeFirst();
            storage.remove(junk);
        }
    }

    public void resizeCache(long size) throws LRUException {
        if (size < 1) {
            throw new LRUException("LRU size too small");
        }
        this.size = size;
        evict();
    }

    public void purgeCache() {
        storage = new HashMap<Long, T>();
        storageLocation = new LinkedList<Long>();
    }

    public boolean remove(long key) {
        if (storage.containsKey(key)) {
            storage.remove(key);
            storageLocation.remove(key);
            return true;
        }
        return false;
    }
}
