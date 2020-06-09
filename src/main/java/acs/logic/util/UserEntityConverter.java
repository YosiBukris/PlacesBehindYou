package acs.logic.util;

import org.springframework.stereotype.Component;

import acs.boundaries.UserBoundary;
import acs.boundaries.UserID;
import acs.data.UserEntity;
import acs.data.entityProperties.UserIDEntity;


@Component
public class UserEntityConverter {

	public UserBoundary fromEntity(UserEntity entity) {
		UserBoundary rv = new UserBoundary();
		rv.setUserId(new UserID(entity.getUserId().getDomain(),entity.getUserId().getEmail()));
		rv.setRole(entity.getRole());
		rv.setUsername(entity.getUsername());
		rv.setAvatar(entity.getAvatar());
		return rv;
	}

	public UserEntity toEntity(UserBoundary userBoundery) {
		UserEntity rv = new UserEntity();
		rv.setUserId(new UserIDEntity(userBoundery.getUserId().getDomain(),userBoundery.getUserId().getEmail()));
		rv.setRole(userBoundery.getRole());
		rv.setUsername(userBoundery.getUsername());
		rv.setAvatar(userBoundery.getAvatar());
		return rv;
	}
	
	
}

