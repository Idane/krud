package com.antelopesystem.crudframework.jpa.model;

import com.antelopesystem.crudframework.fieldmapper.annotation.MappedField;
import com.antelopesystem.crudframework.fieldmapper.transformer.DateToLongTransformer;
import com.antelopesystem.crudframework.jpa.ro.BaseJpaRO;
import com.antelopesystem.crudframework.jpa.ro.BaseUpdatableJpaRO;

import javax.persistence.*;
import java.util.Date;

/**
 * Base class for Hibernate entities.
 */
@MappedSuperclass
public abstract class JpaBaseUpdatebleEntity extends JpaBaseEntity {

	//------------------------ Constants -----------------------
	//------------------------ Fields --------------------------
	// the last time the entity was edited.
	@MappedField(target = BaseUpdatableJpaRO.class, transformer = DateToLongTransformer.class)
	private Date lastUpdateTime;

	//------------------------ Public methods ------------------
	//------------------------ Constructors --------------------
	//------------------------ Field's handlers ----------------
	@Version
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "last_update_time", nullable = false)
	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}
}
