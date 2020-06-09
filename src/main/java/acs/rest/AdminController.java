package acs.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import acs.boundaries.ActionBoundary;
import acs.boundaries.UserBoundary;
import acs.logic.EnhancedActionService;
import acs.logic.EnhancedElementService;
import acs.logic.EnhancedUserService;

@RestController
public class AdminController {
	
	private final String deafaultPageSize = "10";
	private final String deafaultPageNum = "0";
	private EnhancedUserService userService;
	private EnhancedElementService elementService;
	private EnhancedActionService actionService;

	@Autowired
	public AdminController(EnhancedUserService userService, EnhancedElementService elementService, EnhancedActionService actionService) {
		this.userService = userService;
		this.elementService = elementService;
		this.actionService = actionService;
	}
	
	@RequestMapping(path = "/acs/admin/users/{adminDomain}/{adminEmail}", 
			method = RequestMethod.GET, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	public UserBoundary[] exportAllUsers(
			@PathVariable("adminDomain") String adminDomain,
			@PathVariable("adminEmail") String adminEmail,			
			@RequestParam(name = "size", required = false, defaultValue = deafaultPageSize) int size,
			@RequestParam(name = "page", required = false, defaultValue = deafaultPageNum) int page) {
		List<UserBoundary> temp = this.userService.exportAllUsers(adminDomain, adminEmail, size, page);
		return temp.toArray(new UserBoundary[0]);
	}

	@RequestMapping(path = "/acs/admin/actions/{adminDomain}/{adminEmail}",
			method = RequestMethod.GET, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	public List<ActionBoundary> exportAllActions(
			@PathVariable("adminDomain") String adminDomain,
			@PathVariable("adminEmail") String adminEmail,
			@RequestParam(name = "size", required = false, defaultValue = deafaultPageSize) int size,
			@RequestParam(name = "page", required = false, defaultValue = deafaultPageNum) int page) {
		return this.actionService.exportAllActions(adminDomain, adminEmail, size, page);
	}

	@RequestMapping(path = "/acs/admin/users/{adminDomain}/{adminEmail}", method = RequestMethod.DELETE)
	public void deleteAllUsers(@PathVariable("adminDomain") String adminDomain,
			@PathVariable("adminEmail") String adminEmail) {
		this.userService.deleteAllUsers(adminDomain, adminEmail);
	}

	@RequestMapping(path = "/acs/admin/actions/{adminDomain}/{adminEmail}", method = RequestMethod.DELETE)
	public void deleteAllActions(@PathVariable("adminDomain") String adminDomain,
			@PathVariable("adminEmail") String adminEmail) {
		this.actionService.deleteAllActions(adminDomain, adminEmail);
	}

	@RequestMapping(path = "/acs/admin/elements/{adminDomain}/{adminEmail}", method = RequestMethod.DELETE)
	public void deleteAllElements(@PathVariable("adminDomain") String adminDomain,
			@PathVariable("adminEmail") String adminEmail) {
		this.elementService.deleteAllElements(adminDomain, adminEmail);
	}
	
	@RequestMapping(path = "/acs/admin/users/{adminDomain}/{adminEmail}/{userDomain}/{userEmail}", method = RequestMethod.DELETE)
	public void deleteSpecificUser(
			@PathVariable("adminDomain") String adminDomain,
			@PathVariable("adminEmail") String adminEmail,
			@PathVariable("userDomain") String userDomain,
			@PathVariable("userEmail") String userEmail
			) {
		userService.deleteSpecificUser(adminDomain, adminEmail, userDomain, userEmail);
	}
	
	
	@RequestMapping(path = "/acs/admin/elements/{adminDomain}/{adminEmail}/{elementDomain}/{elementID}", method = RequestMethod.DELETE)
	public void deleteSpecificElement(
			@PathVariable("adminDomain") String adminDomain,
			@PathVariable("adminEmail") String adminEmail,
			@PathVariable("elementDomain") String elementDomain,
			@PathVariable("elementID") String elementID
			) {
		elementService.deleteSpecificElement(adminDomain, adminEmail, elementDomain, elementID);
	}

}
