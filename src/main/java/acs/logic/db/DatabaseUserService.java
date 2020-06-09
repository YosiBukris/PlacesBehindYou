package acs.logic.db;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import acs.boundaries.UserBoundary;
import acs.dal.UserDao;
import acs.data.UserEntity;
import acs.data.entityProperties.UserIDEntity;
import acs.data.entityProperties.UserRole;
import acs.logic.ElementNotFoundEception;
import acs.logic.EnhancedUserService;
import acs.logic.util.UserEntityConverter;

@Service
public class DatabaseUserService implements EnhancedUserService {

	private String projectName;
	private UserDao userDao;
	private UserEntityConverter convertor;
	private final String SortingPaginationParam = "username";

	@Autowired
	public DatabaseUserService(UserDao userDao, UserEntityConverter convertor) {
		super();
		this.userDao = userDao;
		this.convertor = convertor;
	}
	
	@PostConstruct
	public void init() {
		System.err.println("**** project name: " + projectName);
	}
	
	@Value("${spring.application.name:demo}")
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	
	@Override
	@Transactional 
	public UserBoundary createUser(UserBoundary user) {
		checkNull(user);
		checkUserRole(user);
		checkEmail(user);
		UserEntity userEntity = this.convertor.toEntity(user);
		userEntity.getUserId().setDomain(projectName);
		this.userDao.save(userEntity);
		return this.convertor.fromEntity(userEntity);
	}

	@Override
	@Transactional(readOnly = true)
	public UserBoundary login(String userDomain, String userEmail) {
		UserEntity userEntity = this.userDao.findById(new UserIDEntity(userDomain, userEmail)).
				orElseThrow(()->new RuntimeException("could not find user by ID"));
		return this.convertor.fromEntity(userEntity);
	}

	@Override
	@Transactional(readOnly = true)
	public UserBoundary updateUser(String userDomain, String userEmail, UserBoundary update) {
		checkNull(update);
		checkUserRole(update);

			UserEntity updateUserEntity = this.convertor.toEntity(update);
			UserEntity userEntity = this.userDao.findById(new UserIDEntity(userDomain, userEmail)).
					orElseThrow(()->new RuntimeException("could not find user by ID"));
					
			if(updateUserEntity.getAvatar() != null)
				userEntity.setAvatar(updateUserEntity.getAvatar());
			if(userEntity.getRole().equals(UserRole.ADMIN)) {
				if(updateUserEntity.getRole() != null)
					userEntity.setRole(updateUserEntity.getRole());
			}else {
				throw new RuntimeException("Cannot change Role if you are not ADMIN");
			}
			if(updateUserEntity.getUsername() != null)
				userEntity.setUsername(updateUserEntity.getUsername());
			this.userDao.save(userEntity);
			return this.convertor.fromEntity(userEntity);
	}

	@Override
	@Transactional(readOnly = true)
	public List<UserBoundary> getAllUsers(String adminDomain, String adminEmail) {
		UserEntity temp = userDao.findById(new UserIDEntity(adminDomain, adminEmail)).
				orElseThrow(()->new ElementNotFoundEception("could not find user by ID "+ adminDomain+adminEmail));
		if(temp.getRole().equals(UserRole.ADMIN)) {

		return StreamSupport.stream(this.userDao.findAll()
				.spliterator(),false)
				.map(this.convertor::fromEntity)
				.collect(Collectors.toList());
	}
		else
		throw new RuntimeException("Only Admin can use this function!");
	}
		

	@Override
	@Transactional
	public void deleteAllUsers(String adminDomain, String adminEmail) {
		UserEntity temp = userDao.findById(new UserIDEntity(adminDomain, adminEmail)).
				orElseThrow(()->new ElementNotFoundEception("could not find user by ID"));
		if(temp.getRole().equals(UserRole.ADMIN)) 
		this.userDao.deleteAll();			
	}
	
	
	private Boolean checkNull(UserBoundary user) {
		if (user.equals(null)) {
			throw new RuntimeException("User cannot be null");
		}
		if (Stream.of(user.getAvatar(), user.getRole(),user.getUserId(), user.getUsername()).anyMatch(Objects::isNull)) {
			throw new RuntimeException("User attributes cannot be null");
		}
		return true;
	}

	@Transactional
	private Boolean checkUserRole(UserBoundary user) {
		if (!Arrays.stream(UserRole.values()).anyMatch((role) -> role.equals(user.getRole()))) {
			throw new RuntimeException("User role is not valid" + user.getRole() );
		}
		return true;
	}
	
	@Transactional
	private Boolean checkEmail(UserBoundary user) {
	       String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+ 
                   "[a-zA-Z0-9_+&*-]+)*@" + 
                   "(?:[a-zA-Z0-9-]+\\.)+[a-z" + 
                   "A-Z]{2,7}$";           
	       Pattern pat = Pattern.compile(emailRegex); 
	       if (user.getUserId().getEmail() == null) 
	    	   throw new RuntimeException("User E-mail can't be null"); 
	       if(!pat.matcher(user.getUserId().getEmail()).matches()) 
	    	   throw new RuntimeException("User E-mail is not valid: " + user.getUserId().getEmail());
	       return true;
	}

	@Override
	@Transactional(readOnly = true)
	public List<UserBoundary> exportAllUsers(String adminDomain, String adminEmail, int size, int page) {
		if(checkRoleValidation(adminDomain, adminEmail).equals(UserRole.ADMIN)) {
		return this.userDao
				.findAll(PageRequest.of(page, size, Direction.ASC, SortingPaginationParam))
			.stream()
			.map(this.convertor::fromEntity)
			.collect(Collectors.toList());	
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

	@Override
	public void deleteSpecificUser(String adminDomain, String adminEmail, String userDomain, String userEmail) {
		if(checkRoleValidation(adminDomain, adminEmail).equals(UserRole.ADMIN)) {
			userDao.deleteById(new UserIDEntity(userDomain, userEmail));
		}else {
			throw new RuntimeException("User role is not valid");
		}
			
		
	}

}
