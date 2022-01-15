package studio.crud.crudframework.crud.handler;

import studio.crud.crudframework.crud.dataaccess.model.DataAccessorDTO;
import studio.crud.crudframework.crud.exception.CrudDeleteException;
import studio.crud.crudframework.crud.hooks.HooksDTO;
import studio.crud.crudframework.crud.hooks.delete.*;
import studio.crud.crudframework.crud.hooks.interfaces.DeleteHooks;
import studio.crud.crudframework.crud.model.EntityMetadataDTO;
import studio.crud.crudframework.exception.WrapException;
import studio.crud.crudframework.model.BaseCrudEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ClassUtils;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;

@WrapException(CrudDeleteException.class)
public class CrudDeleteHandlerImpl implements CrudDeleteHandler {

	@Autowired
	private CrudHelper crudHelper;

	@Resource(name = "crudDeleteHandler")
	private CrudDeleteHandler crudDeleteHandlerProxy;

	@Override
	public <ID extends Serializable, Entity extends BaseCrudEntity<ID>> void deleteInternal(ID id, Class<Entity> clazz,
			HooksDTO<CRUDPreDeleteHook<ID, Entity>, CRUDOnDeleteHook<ID, Entity>, CRUDPostDeleteHook<ID, Entity>> hooks,
			DataAccessorDTO accessorDTO) {

		crudHelper.checkEntityImmutability(clazz);
		crudHelper.checkEntityDeletability(clazz);

		List<DeleteHooks> deleteHooksList = crudHelper.getHooks(DeleteHooks.class, clazz);

		if(deleteHooksList != null && !deleteHooksList.isEmpty()) {
			for(DeleteHooks<ID, Entity> deleteHooks : deleteHooksList) {
				hooks.getPreHooks().add(0, deleteHooks::preDelete);
				hooks.getOnHooks().add(0, deleteHooks::onDelete);
				hooks.getPostHooks().add(0, deleteHooks::postDelete);
			}
		}

		for(CRUDPreDeleteHook<ID, Entity> preHook : hooks.getPreHooks()) {
			preHook.run(id);
		}

		EntityMetadataDTO metadataDTO = crudHelper.getEntityMetadata(clazz);

		Entity entity;

		if(metadataDTO.getDeleteableType() == EntityMetadataDTO.DeleteableType.Hard) {
			entity = crudDeleteHandlerProxy.deleteHardTransactional(id, clazz, hooks.getOnHooks(), accessorDTO);
		} else {

			entity = crudDeleteHandlerProxy.deleteSoftTransactional(id, metadataDTO.getDeleteField().getName(), clazz, hooks.getOnHooks(), accessorDTO);
		}

		crudHelper.evictEntityFromCache(entity);

		for(CRUDPostDeleteHook<ID, Entity> postHook : hooks.getPostHooks()) {
			postHook.run(entity);
		}
	}

	@Override
	@Transactional(readOnly = false)
	public <ID extends Serializable, Entity extends BaseCrudEntity<ID>> Entity deleteHardTransactional(ID id, Class<Entity> clazz, List<CRUDOnDeleteHook<ID, Entity>> onHooks, DataAccessorDTO accessorDTO) {
		Entity entity = crudHelper.getEntityById(id, clazz, null, accessorDTO, true);
		if(crudHelper.isEntityDeleted(entity)) {
			throw new CrudDeleteException("Entity of type [ " + clazz.getSimpleName() + " ] does not exist or cannot be deleted");
		}

		for(CRUDOnDeleteHook<ID, Entity> onHook : onHooks) {
			onHook.run(entity);
		}

		crudHelper.getCrudDaoForEntity(clazz).hardDeleteById(id, clazz);
		return entity;
	}

	@Override
	@Transactional(readOnly = false)
	public <ID extends Serializable, Entity extends BaseCrudEntity<ID>> Entity deleteSoftTransactional(ID id, String deleteField, Class<Entity> clazz, List<CRUDOnDeleteHook<ID, Entity>> onHooks,
			DataAccessorDTO accessorDTO) {
		Entity entity = crudHelper.getEntityById(id, clazz, null, accessorDTO, true);

		if(crudHelper.isEntityDeleted(entity)) {
			throw new CrudDeleteException("Entity of type [ " + clazz.getSimpleName() + " ] does not exist or cannot be deleted");
		}


		for(CRUDOnDeleteHook<ID, Entity> onHook : onHooks) {
			onHook.run(entity);
		}
		crudHelper.getCrudDaoForEntity(clazz).softDeleteById(id, deleteField, clazz);

		return entity;
	}

}
