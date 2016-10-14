package tmheo.cache;

import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.CaffeineSpec;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by taemyung on 2016. 10. 15..
 */
public class MultiCaffeineCacheManager implements CacheManager {

    private final ConcurrentMap<String, Cache> cacheMap = new ConcurrentHashMap<>(16);

    private boolean dynamic = true;

    private final ConcurrentMap<String, Caffeine<Object, Object>> cacheBuilderMap = new ConcurrentHashMap<>(16);

    private final ConcurrentMap<String, CacheLoader<Object, Object>> cacheLoaderMap = new ConcurrentHashMap<>(16);

    private boolean allowNullValues = true;

    public MultiCaffeineCacheManager() {
    }

    public MultiCaffeineCacheManager(String... cacheNames) {
        setCacheNames(Arrays.asList(cacheNames));
    }

    public void setCacheNames(Collection<String> cacheNames) {
        if (cacheNames != null) {
            for (String name : cacheNames) {
                this.cacheMap.put(name, createCaffeineCache(name));
            }
            this.dynamic = false;
        } else {
            this.dynamic = true;
        }
    }

    public void setCaffeine(String name, Caffeine<Object, Object> caffeine) {
        Assert.notNull(name, "Cache name must not be null");
        Assert.notNull(caffeine, "Caffeine must not be null");
        doSetCaffeine(name, caffeine);
    }

    public void setCaffeineSpec(String name, CaffeineSpec caffeineSpec) {
        doSetCaffeine(name, Caffeine.from(caffeineSpec));
    }

    public void setCacheSpecification(String name, String cacheSpecification) {
        doSetCaffeine(name, Caffeine.from(cacheSpecification));
    }

    public void setCacheLoader(String name, CacheLoader<Object, Object> cacheLoader) {
        if (!ObjectUtils.nullSafeEquals(this.cacheLoaderMap.get(name), cacheLoader)) {
            this.cacheLoaderMap.put(name, cacheLoader);
            refreshKnownCaches(name);
        }
    }

    public void setAllowNullValues(boolean allowNullValues) {
        if (this.allowNullValues != allowNullValues) {
            this.allowNullValues = allowNullValues;
            refreshKnownCaches();
        }
    }

    public boolean isAllowNullValues() {
        return this.allowNullValues;
    }

    @Override
    public Collection<String> getCacheNames() {
        return Collections.unmodifiableSet(this.cacheMap.keySet());
    }

    @Override
    public Cache getCache(String name) {
        Cache cache = this.cacheMap.get(name);
        if (cache == null && this.dynamic) {
            synchronized (this.cacheMap) {
                cache = this.cacheMap.get(name);
                if (cache == null) {
                    cache = createCaffeineCache(name);
                    this.cacheMap.put(name, cache);
                }
            }
        }
        return cache;
    }

    protected Cache createCaffeineCache(String name) {
        return new CaffeineCache(name, createNativeCaffeineCache(name), isAllowNullValues());
    }

    protected com.github.benmanes.caffeine.cache.Cache<Object, Object> createNativeCaffeineCache(String name) {

        if (!this.cacheBuilderMap.containsKey(name)) {
            Caffeine<Object, Object> cacheBuilder = Caffeine.newBuilder();
            this.cacheBuilderMap.put(name, cacheBuilder);
        }

        if (this.cacheLoaderMap.containsKey(name)) {
            return this.cacheBuilderMap.get(name).build(this.cacheLoaderMap.get(name));
        } else {
            return this.cacheBuilderMap.get(name).build();
        }

    }

    private void doSetCaffeine(String name, Caffeine<Object, Object> cacheBuilder) {
        if (!ObjectUtils.nullSafeEquals(this.cacheBuilderMap.get(name), cacheBuilder)) {
            this.cacheBuilderMap.put(name, cacheBuilder);
            refreshKnownCaches(name);
        }
    }

    private void refreshKnownCaches(String name) {
        this.cacheMap.put(name, createCaffeineCache(name));
    }

    private void refreshKnownCaches() {
        for (Map.Entry<String, Cache> entry : this.cacheMap.entrySet()) {
            entry.setValue(createCaffeineCache(entry.getKey()));
        }
    }

}
