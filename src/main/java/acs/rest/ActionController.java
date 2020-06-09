package acs.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import acs.boundaries.ActionBoundary;
import acs.logic.ActionService;

@RestController
public class ActionController {
	private ActionService actionService;
	
	@Autowired
	public ActionController(ActionService service) {
		this.actionService = service;
	}
	
	@RequestMapping(path = "/acs/actions",
			method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public Object invokAnAction (
			@RequestBody ActionBoundary action) {
		return this.actionService.invokeAction(action);
	}

}
