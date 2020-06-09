package acs.dal;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import acs.data.UserEntity;
import acs.data.entityProperties.UserIDEntity;

@Repository
public interface UserDao extends MongoRepository<UserEntity,UserIDEntity>{
	
	public List<UserEntity> findAllByUsername(
			@Param("username") String username,
			Pageable pageable); 

}
