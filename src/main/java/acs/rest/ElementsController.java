package acs.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import acs.boundaries.ElementBoundary;
import acs.boundaries.ElementIdBoundray;
import acs.data.entityProperties.Comment;
import acs.data.entityProperties.Description;
import acs.data.entityProperties.Picture;
import acs.data.entityProperties.Rate;
import acs.logic.EnhancedElementService;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

@RestController
public class ElementsController {
	private final String deafaultPageSize = "10";
	private final String deafaultPageNum = "0";
	private EnhancedElementService elementService;

	@Autowired
	public ElementsController(EnhancedElementService service) {
		this.elementService = service;
	}

	@RequestMapping(path = "/acs/elements/{userDomain}/{userEmail}/{elemetDomain}/{elementId}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ElementBoundary RetrieveSpecificElement(
			@PathVariable("userDomain") String domain, 
			@PathVariable("userEmail") String email,  
			@PathVariable("elemetDomain") String elemetDomain,
			@PathVariable("elementId") String id) {
		return this.elementService.getSpecipicElement(domain, email, elemetDomain, id);
	}

	@RequestMapping(path = "/acs/elements/{userDomain}/{userEmail}",
			method = RequestMethod.GET,			
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ElementBoundary[] GetAllElements(
			@PathVariable("userDomain") String domain, 
			@PathVariable("userEmail") String email,
			@RequestParam(name = "size", required = false, defaultValue = deafaultPageSize) int size,
			@RequestParam(name = "page", required = false, defaultValue = deafaultPageNum) int page) {
		List<ElementBoundary> temp = this.elementService.getAll(domain, email, size, page);
		return temp.toArray(new ElementBoundary[0]);
	}

	@RequestMapping(path = "/acs/elements/{managerDomain}/{managerEmail}",
			method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ElementBoundary createANewElement (
			@PathVariable("managerDomain") String domain,
			@PathVariable("managerEmail") String email, 
			@RequestBody ElementBoundary
			input) {
		return this.elementService.create(domain, email, input);
	}


	@RequestMapping(path = "/acs/elements/{managerDomain}/{managerEmail}/{elementDomain}/{elementID}",
			method = RequestMethod.PUT,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public void update (
			@PathVariable("managerDomain") String managerDomain,
			@PathVariable("managerEmail") String managerEmail,
			@PathVariable("elementDomain") String elementDomain,
			@PathVariable("elementID") String elementID,
			@RequestBody ElementBoundary update) {
		this.elementService.update(managerDomain, managerEmail, elementDomain, elementID, update);
	}

	@RequestMapping(path = "/acs/elements/{managerDomain}/{managerEmail}/{elementDomain}/{elementID}/children",
			method = RequestMethod.PUT,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public void bindElementChildToParent (
			@PathVariable("managerDomain") String managerDomain,
			@PathVariable("managerEmail") String managerEmail,
			@PathVariable("elementDomain") String elementDomain,
			@PathVariable("elementID") String elementID,
			@RequestBody ElementIdBoundray elementIdBoundray) {
		this.elementService.bindElementChildToParent(managerDomain, managerEmail, elementDomain, elementID, elementIdBoundray);
	}

	@RequestMapping(path = "/acs/elements/{userDomain}/{userEmail}/{parentDomain}/{parantId}/children",
			method = RequestMethod.GET,			
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ElementBoundary[] getAllElementChildrens(
			@PathVariable("userDomain") String userDomain, 
			@PathVariable("userEmail") String userEmail,
			@PathVariable("parentDomain") String parentDomain,
			@RequestParam(name = "size", required = false, defaultValue = deafaultPageSize) int size,
			@RequestParam(name = "page", required = false, defaultValue = deafaultPageNum) int page,
			@PathVariable("parantId") String parantId) {
		return this.elementService.getAllElementChildrens(userDomain, userEmail,parentDomain,parantId, size, page)
				.toArray(new ElementBoundary[0]);	
	}

	@RequestMapping(path = "/acs/elements/{userDomain}/{userEmail}/{childDomain}/{childId}/parents",
			method = RequestMethod.GET,			
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ElementBoundary[] getAllElementsParents(
			@PathVariable("userDomain") String userDomain, 
			@PathVariable("userEmail") String userEmail,
			@PathVariable("childDomain") String childDomain,
			@PathVariable("childId") String childId,
			@RequestParam(name = "size", required = false, defaultValue = deafaultPageSize) int size,
			@RequestParam(name = "page", required = false, defaultValue = deafaultPageNum) int page) {
		return this.elementService.getAllChildrensParents(userDomain, userEmail,childDomain,childId, size, page)
				.toArray(new ElementBoundary[0]);	
	}
	@RequestMapping(path = "/acs/elements/{userDomain}/{userEmail}/search/byName/{name}",
			method = RequestMethod.GET,			
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ElementBoundary[] searchElementByName(
			@PathVariable("userDomain") String userDomain, 
			@PathVariable("userEmail") String userEmail,
			@PathVariable("name") String name,
			@RequestParam(name = "size", required = false, defaultValue = deafaultPageSize) int size,
			@RequestParam(name = "page", required = false, defaultValue = deafaultPageNum) int page) {
		return this.elementService.getElementByName(userDomain, userEmail,name, size, page)
				.toArray(new ElementBoundary[0]);	
	}
	@RequestMapping(path = "/acs/elements/{userDomain}/{userEmail}/search/byType/{type}",
			method = RequestMethod.GET,			
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ElementBoundary[] searchElementByType(
			@PathVariable("userDomain") String userDomain, 
			@PathVariable("userEmail") String userEmail,
			@PathVariable("type") String type,
			@RequestParam(name = "size", required = false, defaultValue = deafaultPageSize) int size,
			@RequestParam(name = "page", required = false, defaultValue = deafaultPageNum) int page) {
		return this.elementService.getElementByType(userDomain, userEmail,type, size, page)
				.toArray(new ElementBoundary[0]);	
	}

	@RequestMapping(path = "/acs/elements/{userDomain}/{userEmail}/search/near/{lat}/{lng}/{distance}",
			method = RequestMethod.GET,			
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ElementBoundary[] searchElementByLocation(
			@PathVariable("userDomain") String userDomain, 
			@PathVariable("userEmail") String userEmail,
			@PathVariable("lat") Float lat,
			@PathVariable("lng") Float lng,
			@PathVariable("distance") Double distance,
			@RequestParam(name = "size", required = false, defaultValue = deafaultPageSize) int size,
			@RequestParam(name = "page", required = false, defaultValue = deafaultPageNum) int page) {
		return this.elementService.getElementByLocation(userDomain, userEmail, lat, lng, distance, size, page)
				.toArray(new ElementBoundary[0]);	
	}

	@RequestMapping(path = "/acs/elements/{managerDomain}/{managerEmail}/{elementDomain}/{elementID}/comment",
			method = RequestMethod.PUT,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public void addCommentToElement (
			@PathVariable("managerDomain") String managerDomain,
			@PathVariable("managerEmail") String managerEmail,
			@PathVariable("elementDomain") String elementDomain,
			@PathVariable("elementID") String elementID,
			@RequestBody Comment comment) {
		this.elementService.addCommentToElement(managerDomain, managerEmail, elementDomain, elementID, comment);
	}
	@RequestMapping(path = "/acs/elements/{managerDomain}/{managerEmail}/{elementDomain}/{elementID}/description",
			method = RequestMethod.PUT,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public void addDescriptionToElement (
			@PathVariable("managerDomain") String managerDomain,
			@PathVariable("managerEmail") String managerEmail,
			@PathVariable("elementDomain") String elementDomain,
			@PathVariable("elementID") String elementID,
			@RequestBody Description description) {
		this.elementService.addDescriptionElement(managerDomain, managerEmail, elementDomain, elementID, description);
	}

	@RequestMapping(path = "/acs/elements/{managerDomain}/{managerEmail}/{elementDomain}/{elementID}/picture",
			method = RequestMethod.PUT,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public void addPictureToElement (
			@PathVariable("managerDomain") String managerDomain,
			@PathVariable("managerEmail") String managerEmail,
			@PathVariable("elementDomain") String elementDomain,
			@PathVariable("elementID") String elementID,
			@RequestBody Picture picture ) {
		this.elementService.addPictureElement(managerDomain, managerEmail, elementDomain, elementID, picture);
	}

	@RequestMapping(path = "/acs/elements/{managerDomain}/{managerEmail}/{elementDomain}/{elementID}/rate",
			method = RequestMethod.PUT,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public void addRateToElement (
			@PathVariable("managerDomain") String managerDomain,
			@PathVariable("managerEmail") String managerEmail,
			@PathVariable("elementDomain") String elementDomain,
			@PathVariable("elementID") String elementID,
			@RequestBody Rate rate ) {
		this.elementService.addRateElement(managerDomain, managerEmail, elementDomain, elementID, rate);
	}
}