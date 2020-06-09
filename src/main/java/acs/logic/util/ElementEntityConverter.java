package acs.logic.util;

import java.util.HashSet;
import org.springframework.stereotype.Component;
import acs.boundaries.ElementBoundary;
import acs.boundaries.ElementID;
import acs.boundaries.Location;
import acs.boundaries.UserID;
import acs.boundaries.UserIdWrapper;
import acs.data.ElementEntity;
import acs.data.entityProperties.LocationEntity;
import acs.data.entityProperties.UserIDEntity;
import acs.data.entityProperties.UserIdWrapperEntity;

@Component
public class ElementEntityConverter {

	public ElementBoundary fromEntity(ElementEntity entity) {
		ElementBoundary rv = new ElementBoundary();
		String[] id = entity.getElementId().split("_");
		rv.setElementId(new ElementID(id[0],id[1]));
		rv.setElementAttributes((entity.getElementAttributes()));
		rv.setActive(entity.getActive());
		rv.setCreateTimestamp(entity.getCreacteTimestamp());
		rv.setCreatedBy(new UserIdWrapper(new UserID
				(entity.getCreatedBy().getUserId().getDomain(),entity.getCreatedBy().getUserId().getEmail())));
		rv.setLocation(new Location(entity.getLocation().getLat(),entity.getLocation().getlng()));
		rv.setName(entity.getName());
		rv.setType(entity.getType());
		return rv;
	}

	public ElementEntity toEntity(ElementBoundary elementBoundery) {
		ElementEntity rv = new ElementEntity();
		rv.setElementId(elementBoundery.getElementId().getDomain()+"_"+elementBoundery.getElementId().getId());
		rv.setElementAttributes((elementBoundery.getElementAttributes()));
		rv.setActive(elementBoundery.getActive());
		rv.setCreacteTimestamp(elementBoundery.getCreateTimestamp());
		rv.setCreatedBy(new UserIdWrapperEntity(new UserIDEntity
				(elementBoundery.getCreatedBy().getUserId().getDomain(),elementBoundery.getCreatedBy().getUserId().getEmail())));
		rv.setLocation(new LocationEntity(elementBoundery.getLocation().getLat(),elementBoundery.getLocation().getlng()));
		rv.setName(elementBoundery.getName());
		rv.setType(elementBoundery.getType());
		rv.setChildrens(new HashSet<ElementEntity>());
		return rv;
	}
	
	
}

