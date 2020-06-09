package acs.logic.db;


import java.util.ArrayList;
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
import acs.boundaries.UserIdWrapper;
import acs.boundaries.ElementBoundary;
import acs.boundaries.ElementID;
import acs.boundaries.ElementIdBoundray;
import acs.boundaries.UserID;
import acs.dal.ElementDao;
import acs.dal.UserDao;
import acs.data.ElementEntity;
import acs.data.UserEntity;
import acs.data.entityProperties.Comment;
import acs.data.entityProperties.Description;
import acs.data.entityProperties.Picture;
import acs.data.entityProperties.Rate;
import acs.data.entityProperties.UserIDEntity;
import acs.data.entityProperties.UserRole;
import acs.logic.ElementNotFoundEception;
import acs.logic.EnhancedElementService;
import acs.logic.util.ElementEntityConverter;

@Service
public class DatabaseElementService implements EnhancedElementService {
	
	private final String COMMENT = "comments";
	private final String DESCRIPTION = "description";
	private final String RATE = "rate";
	private final String NUM_RATERS = "numRaters";
	private final String PICTURE = "pictures";
	private final String SortingPaginationParam = "name";
	private final String SortingPaginationParamType = "type";
	private String projectName;
	private ElementDao elementDao;
	private UserDao userDao;
	private ElementEntityConverter convertor;

	@Autowired
	public DatabaseElementService(ElementDao elementDao, UserDao userDao, ElementEntityConverter converter) {
		super();
		this.elementDao = elementDao;
		this.convertor = converter;
		this.userDao = userDao;
	}

	@Value("${spring.application.name:demo}")
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	@PostConstruct
	public void init() {
		System.err.println("**** project name: " + projectName);
	}

	private Boolean checkNull(ElementBoundary element) {
		if (element.equals(null)) {
			throw new ElementNotFoundEception("Element cannot be null");
		}
		if (Stream.of(element.getActive(), element.getElementAttributes(), element.getLocation(), element.getName(),
				element.getType()).anyMatch(Objects::isNull)) {
			throw new ElementNotFoundEception("Element attributes cannot be null " + element.getActive()
					+ element.getElementAttributes() + element.getLocation() + element.getName());
		}
		return true;
	}

	@Override
	@Transactional
	public ElementBoundary create(String managerDomain, String managerEmail, ElementBoundary element) {
		checkNull(element);
		UserEntity temp = userDao.findById(new UserIDEntity(managerDomain, managerEmail))
				.orElseThrow(() -> new ElementNotFoundEception("could not find User by ID"));
		if (temp.getRole().equals(UserRole.MANAGER)) {
			UserIdWrapper createdBy = new UserIdWrapper(new UserID(managerDomain, managerEmail));
			element.setCreatedBy(createdBy);
			element.setCreateTimestamp(new Date());
			ElementID id = new ElementID(this.projectName, UUID.randomUUID().toString());
			element.setElementId(id);
			this.elementDao.save(this.convertor.toEntity(element));
			return element;
		}
		throw new ElementNotFoundEception("User Role is not valid , only MANAGER can Create Element");
	}

	@Override
	@Transactional
	public ElementBoundary update(String managerDomain, String managerEmail, String elementDomain, String elementId,
			ElementBoundary update) {

		UserEntity tempUser = userDao.findById(new UserIDEntity(managerDomain, managerEmail))
				.orElseThrow(() -> new ElementNotFoundEception("could not find element by ID"));
		if (tempUser.getRole().equals(UserRole.MANAGER)) {

			ElementBoundary temp = this.getSpecipicElement(managerDomain, managerEmail, elementDomain, elementId);
			if (temp != null) {
				if (update.getActive() != null)
					temp.setActive(update.getActive());
				if (update.getElementAttributes() != null) {
					if(update.getElementAttributes().get(COMMENT)!=null)
						temp.getElementAttributes().put(COMMENT, update.getElementAttributes().get(COMMENT));
					if(update.getElementAttributes().get(DESCRIPTION)!=null)
						temp.getElementAttributes().put(DESCRIPTION, update.getElementAttributes().get(DESCRIPTION));
					if(update.getElementAttributes().get(PICTURE)!=null)
						temp.getElementAttributes().put(PICTURE, update.getElementAttributes().get(PICTURE));
					if(update.getElementAttributes().get(RATE)!=null)
						temp.getElementAttributes().put(RATE, update.getElementAttributes().get(RATE));
				}
				if (update.getLocation() != null)
					temp.setLocation(update.getLocation());
				if (update.getName() != null)
					temp.setName(update.getName());
				if (update.getType() != null)
					temp.setType(update.getType());
			}
			this.elementDao.save(convertor.toEntity(temp));

			return temp;
		}
		return null;
	}

	@Override
	@Transactional(readOnly = true)
	public List<ElementBoundary> getAll(String userDomain, String userEmail) {
		if (checkRoleValidation(userDomain, userEmail).equals(UserRole.PLAYER)) {
			return StreamSupport.stream(this.elementDao.findAll().spliterator(), false).map(this.convertor::fromEntity)
					.filter(c -> c.getActive() == true).collect(Collectors.toList());
		} else {
			return StreamSupport.stream(this.elementDao.findAll().spliterator(), false).map(this.convertor::fromEntity)
					.collect(Collectors.toList());
		}
	}

	@Override
	@Transactional(readOnly = true)
	public ElementBoundary getSpecipicElement(String userDomain, String userEmail, String elementDomain,
			String elementID) {
		String id = elementDomain+"_"+elementID;
		ElementEntity temp = this.elementDao.findById(id)
				.orElseThrow(() -> new ElementNotFoundEception("could not find element by ID " + id));
		if (temp.getActive() == false)
			if (checkRoleValidation(userDomain, userEmail).equals(UserRole.PLAYER))
				throw new ElementNotFoundEception("user role is not valid for 'not active' element");
		return convertor.fromEntity(temp);
	}

	@Override
	@Transactional
	public void deleteAllElements(String adminDomain, String adminEmail) {
		if (checkRoleValidation(adminDomain, adminEmail).equals(UserRole.ADMIN)) {
		this.elementDao.deleteAll();
		}else {
			throw new RuntimeException("You are not ADMIN, Only admin can delete all elements!");
		}
	}

	@Override
	@Transactional
	public void bindElementChildToParent(String managerDomain, String managerEmail, String elementDomain,
			String elementID, ElementIdBoundray elementIdBoundray) {
		UserEntity tempUser = userDao.findById(new UserIDEntity(managerDomain, managerEmail))
				.orElseThrow(() -> new ElementNotFoundEception("could not find element by ID"));
		if (tempUser.getRole().equals(UserRole.MANAGER)) {
		ElementEntity parent = this.elementDao.findById(elementDomain+"_"+elementID)
				.orElseThrow(() -> new ElementNotFoundEception("could not find parent element by ID" + elementID));
		ElementEntity child = this.elementDao
				.findById(elementIdBoundray.getDomain()+"_"+elementIdBoundray.getId())
				.orElseThrow(() -> new ElementNotFoundEception(
						"could not find child element by ID" + elementIdBoundray.getId()));
		parent.getChildrens().add(child);
		child.setParent(parent);
		this.elementDao.save(child);
		this.elementDao.save(parent);
		}
	}

	@Override
	@Transactional(readOnly = true)
	public List<ElementBoundary> getAllElementChildrens(String userDomain, String userEmail, String parentDomain,
			String parentId, int size, int page) {
		if (checkRoleValidation(userDomain, userEmail).equals(UserRole.PLAYER)) {
			return this.elementDao
					.findAllByParent_elementIdAndActive(parentDomain+"_"+parentId,true,
							PageRequest.of(page, size, Direction.ASC, SortingPaginationParam))
					.stream().map(this.convertor::fromEntity)
					.collect(Collectors.toList());
		}
		return this.elementDao
				.findAllByParent_elementId(parentDomain+"_"+parentId,
						PageRequest.of(page, size, Direction.ASC, SortingPaginationParam))
				.stream().map(this.convertor::fromEntity).collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public List<ElementBoundary> getAllChildrensParents(String userDomain, String userEmail, String childDomain,
			String childId, int size, int page) {
		if (checkRoleValidation(userDomain, userEmail).equals(UserRole.PLAYER))
			return this.elementDao
					.findAllByChildrens_elementIdAndActive(childDomain+"_"+childId,true,
							PageRequest.of(page, size, Direction.ASC, SortingPaginationParam))
					.stream().map(this.convertor::fromEntity)
					.collect(Collectors.toList());
		else
			return this.elementDao
					.findAllByChildrens_elementId(childDomain+"_"+childId,
							PageRequest.of(page, size, Direction.ASC, SortingPaginationParam))
					.stream().map(this.convertor::fromEntity).collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public List<ElementBoundary> getAll(String userDomain, String userEmail, int size, int page) {
		if (checkRoleValidation(userDomain, userEmail).equals(UserRole.PLAYER))
			return this.elementDao.findAllByActive(true,PageRequest.of(page, size, Direction.ASC, SortingPaginationParam))
					.stream()
					.map(this.convertor::fromEntity)
					.collect(Collectors.toList());
		else
			return this.elementDao.findAll(PageRequest.of(page, size, Direction.ASC, SortingPaginationParam))
					.getContent()
					.stream()
					.map(this.convertor::fromEntity)
					.collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public List<ElementBoundary> getElementByName(String userDomain, String userEmail, String name, int size,
			int page) {
		if (checkRoleValidation(userDomain, userEmail).equals(UserRole.PLAYER))
			return this.elementDao
					.findAllByNameAndActive(name,true, PageRequest.of(page, size, Direction.ASC, SortingPaginationParam)).stream()
					.map(this.convertor::fromEntity).collect(Collectors.toList());
		else
			return this.elementDao
					.findAllByName(name, PageRequest.of(page, size, Direction.ASC, SortingPaginationParam)).stream()
					.map(this.convertor::fromEntity).collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public List<ElementBoundary> getElementByType(String userDomain, String userEmail, String type, int size,
			int page) {
		if (checkRoleValidation(userDomain, userEmail).equals(UserRole.PLAYER))
			return this.elementDao
					.findAllByTypeAndActive(type,true, PageRequest.of(page, size, Direction.ASC, SortingPaginationParamType)).stream()
					.map(this.convertor::fromEntity).collect(Collectors.toList());
		else
			return this.elementDao
					.findAllByType(type, PageRequest.of(page, size, Direction.ASC, SortingPaginationParamType)).stream()
					.map(this.convertor::fromEntity).collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public UserRole checkRoleValidation(String domain, String email) {
		UserEntity tempUser = userDao.findById(new UserIDEntity(domain, email))
				.orElseThrow(() -> new RuntimeException("could not find User by ID"));
		return tempUser.getRole();
	}

	private double calculateDistanceByTwoPoints(double lat1, double lat2, double lon1, double lon2) {
	    return Math.sqrt((Math.pow(lat1 - lat2, 2) +  
                Math.pow(lon1 - lon2, 2)));
	}

	@Override
	@Transactional(readOnly = true)
	public List<ElementBoundary> getElementByLocation(String userDomain, String userEmail, Float lat, Float lng,
			Double distance, int size, int page) {
		if (checkRoleValidation(userDomain, userEmail).equals(UserRole.PLAYER)) {
			 return this.elementDao.findAll(PageRequest.of(page, size, Direction.ASC, SortingPaginationParam))
					 .getContent()
					.stream().map(this.convertor::fromEntity)
					.filter(element -> calculateDistanceByTwoPoints(element.getLocation().getLat(), lat,
							element.getLocation().getlng(), lng) <= distance && element.getActive())
					.collect(Collectors.toList());
		}
		return this.elementDao.findAll(PageRequest.of(page, size, Direction.ASC, SortingPaginationParam))
				.getContent()
				.stream().map(this.convertor::fromEntity)
				.filter(element -> calculateDistanceByTwoPoints(element.getLocation().getLat(), lat,
						element.getLocation().getlng(), lng) <= distance)
				.collect(Collectors.toList());
	}

	@Override
	public void addCommentToElement(String managerDomain, String managerEmail, String elementDomain, String elementID,
			Comment comment) {
		UserEntity user = userDao.findById(new UserIDEntity(managerDomain, managerEmail))
				.orElseThrow(() -> new ElementNotFoundEception("could not find User by ID"));
		String id = elementDomain+"_"+elementID;
		ElementEntity element = this.elementDao.findById(id)
				.orElseThrow(() -> new ElementNotFoundEception("could not find element by ID" + id));

		Comment thisComment = new Comment(user.getUsername(),comment.getComment());
		List<Comment> allComments = (List<Comment>) element.getElementAttributes().get(COMMENT);
		if (allComments == null)
			allComments = new ArrayList<Comment>();
		allComments.add(thisComment);
		element.getElementAttributes().put(COMMENT, allComments);
		// update the element in the DB.
		elementDao.save(element);
	}

	@Override
	public void addDescriptionElement(String managerDomain, String managerEmail, String elementDomain, String elementID,
			Description description) {
		
		String id = elementDomain+"_"+elementID;
		ElementEntity element = this.elementDao.findById(id)
				.orElseThrow(() -> new ElementNotFoundEception("could not find element by ID" + id));

		if (element.getElementAttributes().get(DESCRIPTION) == null) {
			element.getElementAttributes().put(DESCRIPTION, description.getDescription());
			elementDao.save(element);
		}
		else
			throw new ElementNotFoundEception("Cant edit description if alreday exits!");
		
	}

	@Override
	public void addPictureElement(String managerDomain, String managerEmail, String elementDomain, String elementID,
			Picture picture) {
	
		UserEntity user = userDao.findById(new UserIDEntity(managerDomain, managerEmail))
				.orElseThrow(() -> new ElementNotFoundEception("could not find User by ID"));
		String id = elementDomain+"_"+elementID;
		ElementEntity element = this.elementDao.findById(id)
				.orElseThrow(() -> new ElementNotFoundEception("could not find element by ID" + id));
		
		List<Picture> allPictures = (List<Picture>) element.getElementAttributes().get(PICTURE);
		if (allPictures == null)
			allPictures = new ArrayList<Picture>();
		allPictures.add(picture);
		element.getElementAttributes().put(PICTURE, allPictures);
		// update the element in the DB.
		elementDao.save(element);
		
	}

	@Override
	public void addRateElement(String mangerDomain, String mangerEmail, String elementDomain, String elementID,
			Rate rate) {

		String id = elementDomain+"_"+elementID;
		ElementEntity element = this.elementDao.findById(id)
				.orElseThrow(() -> new ElementNotFoundEception("could not find element by ID" + id));

		if (rate == null)
			rate.setRate(0);
		Integer NumOfRaters = (Integer) element.getElementAttributes().get(NUM_RATERS);
		if (NumOfRaters == null) {
			NumOfRaters = 0;
			rate.setRate(0);
		}
		NumOfRaters++;
		Integer thisRate = (Integer) rate.getRate();
		if(thisRate<0 || thisRate>10){
			throw new ElementNotFoundEception("Rate must be between 0-10!");
		}
		int newRate = (rate.getRate() +thisRate)/NumOfRaters;
		element.getElementAttributes().put(RATE, newRate);
		element.getElementAttributes().put(NUM_RATERS, NumOfRaters);
		// update the element in the DB.
		elementDao.save(element);	
	}

	@Override
	public void deleteSpecificElement(String adminDomain, String adminEmail, String elementDomain, String elementID) {
		if (checkRoleValidation(adminDomain, adminEmail).equals(UserRole.ADMIN)) {
			this.elementDao.deleteById(elementDomain+"_"+elementID);
		}else {
			throw new RuntimeException("You are not ADMIN, Only admin can delete all elements!");
		}
		
	}
	


}