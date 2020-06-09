package acs.dal;
import java.util.List;


import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import acs.data.ActionEntity;
import acs.data.entityProperties.ActionIDEntity;
@Repository
public interface ActionDao extends MongoRepository<ActionEntity,ActionIDEntity>{


	public List<ActionEntity> findAllByType(
			@Param("type") String type,
			Pageable pageable); 

	public List<ActionEntity> findByInvokedBy(
			@Param("invokedBy") String invokedBy,
			Pageable pageable); 
	
}