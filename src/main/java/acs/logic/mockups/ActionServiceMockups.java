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
import org.springframework.stereotype.Service;

import acs.boundaries.ActionBoundary;
import acs.boundaries.ActionID;
import acs.data.ActionEntity;
import acs.logic.ActionService;
import acs.logic.util.ActionEntityConverter;

//@Service
public class ActionServiceMockups implements ActionService {

	private Map<String, ActionEntity> allActions;
	private String projectName;
	private ActionEntityConverter convertor;

	@Autowired
	public ActionServiceMockups(ActionEntityConverter entityConverter) {
		this.convertor = entityConverter;
	}

	@PostConstruct
	public void init() {
		this.allActions = Collections.synchronizedMap(new HashMap<String, ActionEntity>());
		System.err.println("**** project name: " + projectName);
	}

	@Value("${spring.application.name:demo}")
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	@Override
	public Object invokeAction(ActionBoundary action) {
		checkNull(action);
		action.setActionId(new ActionID(this.projectName, UUID.randomUUID().toString()));
		action.setCreatedTimestamp(new Date());
		ActionEntity actionEntity = this.convertor.toEntity(action);
		allActions.put(actionEntity.getActionId().toString(), actionEntity);
		return this.convertor.fromEntity(actionEntity);
		
	}

	private Boolean checkNull(ActionBoundary action) {
		if (action.equals(null)) {
			throw new RuntimeException("Action cannot be null");
		}
		if (Stream.of(action.getActionAttributes(), action.getElement(), action.getInvokedBy(), action.getType())
				.anyMatch(Objects::isNull)) {
			throw new RuntimeException("Action attributes cannot be null");
		}
		return true;
	}

	@Override
	public List<ActionBoundary> getAllActions(String adminDomain, String adminEmail) {
		return this.allActions.values().stream().map(this.convertor::fromEntity).collect(Collectors.toList());
	}

	@Override
	public void deleteAllActions(String adminDomain, String adminEmail) {
		allActions.clear();
	}

}
