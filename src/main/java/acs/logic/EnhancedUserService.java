package acs.logic;

import java.util.List;


import acs.boundaries.UserBoundary;

public interface EnhancedUserService extends UserService {


	List<UserBoundary> exportAllUsers(String adminDomain, String adminEmail,
			int size, int page);

	void deleteSpecificUser(String adminDomain, String adminEmail, String userDomain, String userEmail);

}
