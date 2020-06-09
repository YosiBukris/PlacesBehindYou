package acs.logic.mockups;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import acs.boundaries.UserIdWrapper;
import acs.boundaries.ElementBoundary;
import acs.boundaries.ElementID;
import acs.boundaries.UserID;
import acs.data.ElementEntity;
import acs.data.entityProperties.LocationEntity;
import acs.logic.ElementService;
import acs.logic.util.ElementEntityConverter;

//@Service
public class ElementServiceMockups implements ElementService {

	private Map<String, ElementEntity> allElements;
	private String projectName;
	private ElementEntityConverter convertor;

	@Autowired
	public ElementServiceMockups(ElementEntityConverter entityConvertor) {
		this.convertor = entityConvertor;
	}

	@Value("${spring.application.name:demo}")
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	@PostConstruct
	public void init() {
		this.allElements = Collections.synchronizedMap(new HashMap<String, ElementEntity>());
		System.err.println("**** project name: " + projectName);
	}

	private Boolean checkNull(ElementBoundary element) {
		if (element.equals(null)) {
			throw new RuntimeException("Element cannot be null");
		}
		if (Stream.of(element.getActive(), element.getElementAttributes(), element.getLocation(), element.getName(),
				element.getType()).anyMatch(Objects::isNull)) {
			throw new RuntimeException("Element attributes cannot be null");
		}
		return true;
	}

	@Override
	public ElementBoundary create(String managerDomain, String managerEmail, ElementBoundary element) {
		checkNull(element);
		UserIdWrapper createdBy = new UserIdWrapper((new UserID(managerDomain, managerEmail)));
		element.setCreatedBy(createdBy);
		element.setCreateTimestamp(new Date());
		ElementID id = new ElementID(this.projectName, UUID.randomUUID().toString());
		element.setElementId(id);
		ElementEntity elementEntity = this.convertor.toEntity(element);
		allElements.put(element.getElementId().getDomain()+element.getElementId().getId() ,elementEntity);
		return this.convertor.fromEntity(elementEntity);
	}

	@Override
    public ElementBoundary update(String managerDomain, String managerEmail, String elementDomain, String elementID,
            ElementBoundary update) {
        checkNull(update);
        ElementEntity elementEntity = this.findElementInMap(elementDomain+ elementID);
        if(update.getActive()!=null)
        	elementEntity.setActive(update.getActive());
			if(update.getElementAttributes()!=null)
				elementEntity.setElementAttributes(update.getElementAttributes());
			if(update.getLocation()!=null)
				elementEntity.setLocation(new LocationEntity(update.getLocation().getLat(),update.getLocation().getlng()));
			if(update.getName()!=null)
				elementEntity.setName(update.getName());
			if(update.getType()!=null)
				elementEntity.setType(update.getType());
		this.allElements.replace(elementDomain+elementID, elementEntity);
        return this.convertor.fromEntity(elementEntity);

    }
	
	@Override
	public List<ElementBoundary> getAll(String userDomain, String userEmail) {
		return this.allElements.values().stream().map(this.convertor::fromEntity).collect(Collectors.toList());
	}

	@Override
	public ElementBoundary getSpecipicElement(String userDomain, String userEmail, String elementDomain,
			String elementID) {
		ElementEntity ElementEntity = findElementInMap(elementDomain + elementID);
		return this.convertor.fromEntity(ElementEntity);
	}

	@Override
	public void deleteAllElements(String adminDomain, String adminEmail) {
		allElements.clear();
	}

	private ElementEntity findElementInMap(String elementID) {
		return allElements.entrySet().stream().filter(ElementEntitySet -> ElementEntitySet.getKey().equals(elementID))
				.map(Map.Entry::getValue).findFirst()
				.orElseThrow(() -> new RuntimeException("Could not find Element by id and domain"));
	}

}
