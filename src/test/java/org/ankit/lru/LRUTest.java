package org.ankit.lru;

import junit.framework.Assert;
import org.junit.Test;

/**
 * Created by ankit on 22/12/16.
 */
public class LRUTest {
    @Test
    public void getCacheMaxSize() throws Exception {
        LRU<Integer> lru = new LRU<Integer>();
        Assert.assertEquals(Long.MAX_VALUE, lru.getCacheMaxSize());
        lru = new LRU<Integer>(3);
        Assert.assertEquals(3, lru.getCacheMaxSize());
    }

    @Test
    public void getCacheSize() throws Exception {
        LRU<Integer> lru = new LRU<Integer>();
        Assert.assertEquals(0, lru.getCacheSize());
        lru.put(1, 4);
        lru.put(2, 3);
        Assert.assertEquals(2, lru.getCacheSize());
    }

    @Test
    public void get() throws Exception {
        LRU<Integer> lru = new LRU<Integer>(3);
        lru.put(1, 4);
        lru.put(2, 5);
        lru.put(3, 6);
        lru.put(4, 7);
        lru.put(5, 8);
        Assert.assertEquals(new Integer(7), lru.get(4));
        Assert.assertEquals(new Integer(8), lru.get(5));
        Assert.assertEquals(null, lru.get(1));
    }

    @Test
    public void put() throws Exception {
        LRU<Integer> lru = new LRU<Integer>(3);
        lru.put(1, 4);
        Assert.assertEquals(1, lru.getCacheSize());
    }

    @Test
    public void resizeCache() throws Exception {
        LRU<Integer> lru = new LRU<Integer>(5);
        Assert.assertEquals(5, lru.getCacheMaxSize());
        lru.put(1, 4);
        lru.put(2, 5);
        lru.put(3, 6);
        lru.put(4, 7);
        lru.put(5, 8);
        Assert.assertEquals(new Integer(4), lru.get(1));    //Since we accessed key: 1 hence it will not be removed, see below asserts
        lru.resizeCache(3);
        Assert.assertEquals(3, lru.getCacheMaxSize());
        Assert.assertEquals(new Integer(4), lru.get(1));
        Assert.assertEquals(null, lru.get(2));
        Assert.assertEquals(null, lru.get(3));
        Assert.assertEquals(new Integer(7), lru.get(4));
        Assert.assertEquals(new Integer(8), lru.get(5));
    }

    @Test(expected = LRUException.class)
    public void resizeCacheException() throws Exception {
        LRU<Integer> lru = new LRU<Integer>(5);
        try {
            lru.resizeCache(0);
            throw new Exception("Didn't get LRU Exception");
        } catch (LRUException e) {
            throw e;
        }
    }

    @Test(expected = LRUException.class)
    public void initException() throws Exception {
        try {
            LRU<Integer> lru = new LRU<Integer>(-1);
            throw new Exception("Didn't get LRU Exception");
        } catch (LRUException e) {
            throw e;
        }
    }

    @Test
    public void purgeCache() throws Exception {
        LRU<Integer> lru = new LRU<Integer>();
        lru.put(1, 4);
        lru.put(2, 3);
        Assert.assertEquals(2, lru.getCacheSize());
        Assert.assertEquals(new Integer(3), lru.get(2));
        lru.purgeCache();
        Assert.assertEquals(0, lru.getCacheSize());
        Assert.assertEquals(null, lru.get(2));
    }

    @Test
    public void remove() throws Exception {
        LRU<Integer> lru = new LRU<Integer>();
        lru.put(1, 4);
        lru.put(2, 3);
        Assert.assertEquals(2, lru.getCacheSize());
        Assert.assertEquals(new Integer(3), lru.get(2));
        boolean status = lru.remove(2);
        Assert.assertEquals(1, lru.getCacheSize());
        Assert.assertEquals(null, lru.get(2));
        Assert.assertEquals(true, status);
        status = lru.remove(123);
        Assert.assertEquals(false, status);
    }

}