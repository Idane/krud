package dev.krud.crudframework.crud.hooks.create.from;

import dev.krud.crudframework.crud.hooks.base.CRUDHook;
import dev.krud.crudframework.model.BaseCrudEntity;

import java.io.Serializable;

@FunctionalInterface
public interface CRUDPreCreateFromHook<ID extends Serializable, Entity extends BaseCrudEntity<ID>> extends CRUDHook {

	void run(Object object);
}
