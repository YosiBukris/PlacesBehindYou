package acs.logic.db;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import javax.annotation.PostConstruct;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import acs.boundaries.ActionBoundary;
import acs.boundaries.ActionID;
import acs.dal.ActionDao;
import acs.dal.ElementDao;
import acs.dal.UserDao;
import acs.data.ActionEntity;
import acs.data.ElementEntity;
import acs.data.UserEntity;
import acs.data.entityProperties.UserIDEntity;
import acs.data.entityProperties.UserRole;
import acs.logic.ElementNotFoundEception;
import acs.logic.EnhancedActionService;
import acs.logic.util.ActionEntityConverter;

@Service
public class DatabaseActionService implements EnhancedActionService {

	private String projectName;
	private ActionDao actionDao;
	private UserDao userDao;
	private ElementDao elementDao;
	private ActionEntityConverter convertor;
	private final String SortingPaginationParam = "type";


	@PostConstruct
	public void init() {
		System.err.println("**** project name: " + projectName);
	}

	@Autowired
	public DatabaseActionService(ActionDao actionDao,UserDao userDao,ElementDao elementDao, ActionEntityConverter convertor) {
		super();
		this.actionDao = actionDao;
		this.convertor = convertor;
		this.userDao =  userDao;
		this.elementDao = elementDao;
	}

	@Value("${spring.application.name:demo}")
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	@Override
	@Transactional
	public Object invokeAction(ActionBoundary action) {
		UserEntity temp = userDao.findById(new UserIDEntity(action.getInvokedBy().getUserId().getDomain(), action.getInvokedBy().getUserId().getEmail())).
				orElseThrow(()->new ElementNotFoundEception("could not find element by ID"));

		ElementEntity tempq = elementDao.findById(action.getElement().getElementId().getDomain()+"_"+action.getElement().getElementId().getId()).
				orElseThrow(()->new ElementNotFoundEception("could not find an element that active"));
		if(tempq.getActive().equals(true)) {
			if(temp.getRole().equals(UserRole.PLAYER)) {
				checkNull(action);
				action.setActionId(new ActionID(this.projectName, UUID.randomUUID().toString()));
				action.setCreatedTimestamp(new Date());
				ActionEntity actionEntity = this.convertor.toEntity(action);
				actionDao.save(actionEntity);
				return action;
			}
			else
				throw new ElementNotFoundEception("Only Players can invoke an action!");
		}
		else
			throw new ElementNotFoundEception("could not find an element that active");
	}


	

	@Override
	@Transactional(readOnly = true)
	public List<ActionBoundary> getAllActions(String adminDomain, String adminEmail) {
		
		if(checkRoleValidation(adminDomain, adminEmail).equals(UserRole.ADMIN)) {

			return StreamSupport.stream(this.actionDao.findAll()
					.spliterator(),false)
					.map(this.convertor::fromEntity)
					.collect(Collectors.toList());
		}
		else
			return null;
	}


	@Override
	@Transactional
	public void deleteAllActions(String adminDomain, String adminEmail) {
		this.actionDao.deleteAll();
	}


	private Boolean checkNull(ActionBoundary action) {
		if (action.equals(null)) {
			throw new RuntimeException("Action cannot be null");
		}
		if (Stream.of(action.getActionAttributes(), action.getActionId(),action.getCreatedTimestamp(),
				action.getElement(),action.getInvokedBy(),action.getType()).anyMatch(Objects::isNull)) {
			throw new RuntimeException("Action attributes cannot be null");
		}
		return true;
	}

	@Override
	@Transactional(readOnly = true)
	public List<ActionBoundary> exportAllActions(String adminDomain, String adminEmail, int size, int page) {
		UserEntity temp = userDao.findById(new UserIDEntity(adminDomain, adminEmail)).
				orElseThrow(()->new ElementNotFoundEception("could not find element by ID"));
		if(temp.getRole().equals(UserRole.ADMIN)) {

			return this.actionDao.
					findAll(PageRequest.of(page, size, Direction.ASC, SortingPaginationParam))
					.getContent().stream().map(this.convertor::fromEntity).collect(Collectors.toList());
		}
		else
			return null;
	}
	
	@Transactional(readOnly = true)
	public UserRole checkRoleValidation(String domain, String email) {
		UserEntity tempUser = userDao.findById(new UserIDEntity(domain, email))
				.orElseThrow(() -> new RuntimeException("could not find User by ID"));
		return tempUser.getRole();
	}

}