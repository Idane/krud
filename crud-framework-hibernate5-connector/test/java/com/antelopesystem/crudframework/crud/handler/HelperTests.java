package com.antelopesystem.crudframework.crud.handler;

import com.antelopesystem.crudframework.crud.model.TestEntity;
import org.junit.Test;

public class HelperTests extends BaseCrudTest {

	@Test(expected = IllegalStateException.class)
	public void testPersistCopyImmutablility() {
		TestEntity testEntityCopy = (TestEntity) readOnlyEntity1.saveOrGetCopy();
		testEntityCopy.setLongCurrency(1000L);

	}
}
