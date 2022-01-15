package studio.crud.crudframework.crud.cache.adapter.ehcache

import studio.crud.crudframework.crud.cache.CrudCache
import net.sf.ehcache.Ehcache
import net.sf.ehcache.Element

class CrudEhCacheImpl(private val vendorCache: Ehcache) : CrudCache {
    override fun get(key: Any): Any? {
        return vendorCache.get(key)?.objectValue
    }

    override fun put(key: Any, value: Any?) {
        val element = Element(key, value)
        vendorCache.put(element)
    }

    override fun remove(key: Any) {
        vendorCache.remove(key)
    }

    override fun removeAll() {
        vendorCache.removeAll()
    }

    override fun unwrap(): Any {
        return vendorCache
    }
}