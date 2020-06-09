package acs.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import acs.boundaries.NewUserDetails;
import acs.boundaries.UserBoundary;
import acs.boundaries.UserID;
import acs.logic.EnhancedUserService;

@RestController
public class UserController {
	
	private EnhancedUserService userService;
	
	
	@Autowired
	public UserController(EnhancedUserService service) {
		this.userService = service;
	}
	

	@RequestMapping(path = "/acs/users",
			method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public UserBoundary createNewUser (
			@RequestBody NewUserDetails newUserDetails) {
				UserBoundary newUser = new UserBoundary(
				new UserID("",newUserDetails.getEmail()), 
				newUserDetails.getRole(), 
				newUserDetails.getUsername(), 
				newUserDetails.getAvatar());
		return userService.createUser(newUser);
	}
	
	@RequestMapping(path = "/acs/users/login/{userDomain}/{userEmail}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public UserBoundary login(
			@PathVariable("userDomain") String userDomain , 
			@PathVariable("userEmail") String userEmail) {
		return userService.login(userDomain, userEmail);
	}
	
	@RequestMapping(path = "/acs/users/{userDomain}/{userEmail}",
			method = RequestMethod.PUT,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public void updateUserInfo (
			@PathVariable("userDomain") String userDomain , 
			@PathVariable("userEmail") String userEmail,
			@RequestBody UserBoundary user
			) {
		userService.updateUser(userDomain, userEmail, user);
	}
	
	

}
