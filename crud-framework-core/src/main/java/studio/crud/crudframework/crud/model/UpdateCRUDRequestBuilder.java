package studio.crud.crudframework.crud.model;

import studio.crud.crudframework.crud.dataaccess.model.DataAccessorDTO;
import studio.crud.crudframework.crud.hooks.HooksDTO;

/**
 * {@inheritDoc}
 */
public class UpdateCRUDRequestBuilder<PreHook, OnHook, PostHook, ReturnType> extends CRUDRequestBuilder<PreHook, OnHook, PostHook, ReturnType> {

	private UpdateCRUDExecutor<PreHook, OnHook, PostHook, ReturnType> onExecute;

	public UpdateCRUDRequestBuilder(UpdateCRUDExecutor<PreHook, OnHook, PostHook, ReturnType> onExecute) {
		this.onExecute = onExecute;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ReturnType execute() {
		return this.onExecute.execute(new HooksDTO<>(preHooks, onHooks, postHooks), accessorDTO);
	}

	public interface UpdateCRUDExecutor<PreHook, OnHook, PostHook, EntityType> {

		EntityType execute(HooksDTO<PreHook, OnHook, PostHook> hooksDTO, DataAccessorDTO accessorDTO);
	}
}
