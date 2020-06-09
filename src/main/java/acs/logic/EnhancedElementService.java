package acs.logic;

import java.util.List;

import acs.boundaries.ElementBoundary;
import acs.boundaries.ElementIdBoundray;
import acs.data.entityProperties.Comment;
import acs.data.entityProperties.Description;
import acs.data.entityProperties.Picture;
import acs.data.entityProperties.Rate;

public interface EnhancedElementService extends ElementService {
	
	public void bindElementChildToParent(String mangerDomain, String mangerEmail, String elementDomain,
			String elementID, ElementIdBoundray elementIdBoundray);
	
	public List<ElementBoundary> getAllElementChildrens(String userDomain, String userEmail, String parentDomain,
			String parentId, int size, int page);
	public List<ElementBoundary> getAllChildrensParents(String userDomain,String userEmail,String childDomain,
			String childId,int size,int page);
	
	public List<ElementBoundary> getElementByName(String userDomain, String userEmail,
			String name, int size, int page);
	public List<ElementBoundary> getElementByType(String userDomain, String userEmail, String name, int size, int page);
	
	public List<ElementBoundary> getElementByLocation(String userDomain, String userEmail, Float lat, Float lng,
			Double distance, int size, int page);
	
	public List<ElementBoundary> getAll(String userDomain, String userEmail, int size, int page);

	public void addCommentToElement(String mangerDomain, String mangerEmail, String elementDomain, String elementID,
			Comment comment);

	public void addDescriptionElement(String mangerDomain, String mangerEmail, String elementDomain, String elementID,
			Description description);

	public void addPictureElement(String mangerDomain, String mangerEmail, String elementDomain, String elementID,
			Picture picture);

	public void addRateElement(String mangerDomain, String mangerEmail, String elementDomain, String elementID,
			Rate rate);

	public void deleteSpecificElement(String adminDomain, String adminEmail, String elementDomain, String elementID);

	
}
