package studio.crud.crudframework.web.rest

import org.springframework.boot.context.properties.ConfigurationProperties
import studio.crud.crudframework.crud.configuration.properties.CrudFrameworkProperties

@ConfigurationProperties(CrudControllerProperties.CONFIGURATION_PREFIX)
class CrudControllerProperties {
    /**
     * The base URL for the CRUD Controller
     */
    var url = "/crud"
    companion object {
        const val CONFIGURATION_PREFIX = "${CrudFrameworkProperties.CONFIGURATION_PREFIX}.rest"
    }
}