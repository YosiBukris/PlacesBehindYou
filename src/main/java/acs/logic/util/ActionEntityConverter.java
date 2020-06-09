package acs.logic.util;

import org.springframework.stereotype.Component;

import acs.boundaries.ActionBoundary;
import acs.boundaries.ActionID;
import acs.boundaries.ElementID;
import acs.boundaries.ElementIdWrapper;
import acs.boundaries.UserID;
import acs.boundaries.UserIdWrapper;
import acs.data.ActionEntity;
import acs.data.entityProperties.ActionIDEntity;
import acs.data.entityProperties.ElementIDEntity;
import acs.data.entityProperties.ElementIdWrapperEntity;
import acs.data.entityProperties.UserIDEntity;
import acs.data.entityProperties.UserIdWrapperEntity;

@Component
public class ActionEntityConverter {
	
	public ActionBoundary fromEntity(ActionEntity entity) {
		ActionBoundary rv = new ActionBoundary();
		rv.setActionId(new ActionID(entity.getActionId().getDomain(),entity.getActionId().getID()));
		rv.setActionAttributes(entity.getActionAttributes());
		rv.setCreatedTimestamp(entity.getCreatedTimestamp());
		rv.setElement(new ElementIdWrapper(new ElementID
				(entity.getElement().getElementId().getDomain(), entity.getElement().getElementId().getId())));
		rv.setInvokedBy(new UserIdWrapper(new UserID
				(entity.getInvokedBy().getUserId().getDomain(),entity.getInvokedBy().getUserId().getEmail())));
		rv.setType(entity.getType());
		return rv;
	}

	public  ActionEntity toEntity( ActionBoundary actionBoundery) {
		ActionEntity rv = new ActionEntity();
		rv.setActionId(new ActionIDEntity(actionBoundery.getActionId().getDomain(),actionBoundery.getActionId().getID()));
		rv.setActionAttributes(actionBoundery.getActionAttributes());
		rv.setCreatedTimestamp(actionBoundery.getCreatedTimestamp());
		rv.setElement(new ElementIdWrapperEntity(new ElementIDEntity
				(actionBoundery.getElement().getElementId().getDomain(), actionBoundery.getElement().getElementId().getId())));
		rv.setInvokedBy(new UserIdWrapperEntity(new UserIDEntity
				(actionBoundery.getInvokedBy().getUserId().getDomain(),actionBoundery.getInvokedBy().getUserId().getEmail())));
		rv.setType(actionBoundery.getType());
		return rv;
	}

}
