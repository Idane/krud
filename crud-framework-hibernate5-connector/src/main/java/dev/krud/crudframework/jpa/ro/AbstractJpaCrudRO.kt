package dev.krud.crudframework.jpa.ro

import java.io.Serializable

abstract class AbstractJpaCrudRO : Serializable {
    val id: Long? = null
}