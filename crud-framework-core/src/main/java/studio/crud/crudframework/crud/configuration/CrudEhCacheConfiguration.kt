package studio.crud.crudframework.crud.configuration

import studio.crud.crudframework.crud.cache.CacheManagerAdapter
import studio.crud.crudframework.crud.cache.adapter.ehcache.EhCacheManagerAdapter
import net.sf.ehcache.CacheManager
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@ConditionalOnClass(CacheManager::class)
class CrudEhCacheConfiguration {
    @Bean
    @ConditionalOnBean(CacheManager::class)
    fun ehCacheManagerAdapter(cacheManager: CacheManager): CacheManagerAdapter {
        log.info("Using EhCache for caching operations")
        return EhCacheManagerAdapter(cacheManager)
    }

    companion object {
        private val log = LoggerFactory.getLogger(CrudEhCacheConfiguration::class.java)
    }
}