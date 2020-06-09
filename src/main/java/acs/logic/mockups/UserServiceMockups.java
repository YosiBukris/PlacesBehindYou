package acs.logic.mockups;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import acs.boundaries.UserBoundary;
import acs.data.UserEntity;
import acs.data.entityProperties.UserRole;
import acs.logic.UserService;
import acs.logic.util.UserEntityConverter;

//@Service
public class UserServiceMockups implements UserService {

	private Map<String, UserEntity> allUsers;
	private String projectName;
	private UserEntityConverter convertor;

	@Autowired
	public UserServiceMockups(UserEntityConverter entityConverter) {
		this.convertor = entityConverter;
	}

	@Value("${spring.application.name:demo}")
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	@PostConstruct
	public void init() {
		this.allUsers = Collections.synchronizedMap(new HashMap<String, UserEntity>());
		System.err.println("**** project name: " + projectName);
	}
	
	private Boolean checkNull(UserBoundary user) {
		if (user.equals(null)) {
			throw new RuntimeException("User cannot be null");
		}
		if (Stream.of(user.getUserId(), user.getRole(), user.getUsername(), user.getAvatar()).anyMatch(Objects::isNull)) {
			throw new RuntimeException("User fields cannot be null");
		}
		return true;
	}
	
	private Boolean checkUserRole(UserBoundary user) {
		if (!Arrays.stream(UserRole.values()).anyMatch((role) -> role.equals(user.getRole()))) {
			throw new RuntimeException("User role is not valid");
		}
		return true;
	}

	@Override
	public UserBoundary createUser(UserBoundary user) {
		checkNull(user);
		checkUserRole(user);
		UserEntity userEntity = this.convertor.toEntity(user);
		userEntity.getUserId().setDomain(projectName);
		allUsers.put(userEntity.getUserId().toString(), userEntity);
		return this.convertor.fromEntity(userEntity);

	}
	
	private UserEntity findUserInMap(String userDomain, String userEmail) {
		return allUsers.entrySet().stream()
				.filter(userEntitySet -> userEntitySet.getKey().equals(userDomain + userEmail)).map(Map.Entry::getValue)
				.findFirst().orElseThrow(() -> new RuntimeException("Could not find user by id and domain"));
	}


	@Override
	public UserBoundary login(String userDomain, String userEmail) {
		UserEntity userEntity = findUserInMap(userDomain,userEmail);
		return this.convertor.fromEntity(userEntity);
	}

	@Override
	public UserBoundary updateUser(String userDomain, String userEmail, UserBoundary update) {
		checkNull(update);
		checkUserRole(update);
		UserEntity updateUserEntity = this.convertor.toEntity(update);
		UserEntity userEntity = findUserInMap(userDomain,userEmail);
		userEntity.setAvatar(updateUserEntity.getAvatar());
		userEntity.setRole(updateUserEntity.getRole());
		userEntity.setUsername(updateUserEntity.getUsername());
		return this.convertor.fromEntity(userEntity);
	}

	@Override
	public List<UserBoundary> getAllUsers(String adminDomain, String adminEmail) {
		return this.allUsers.values().stream().map(this.convertor::fromEntity).collect(Collectors.toList());
	}

	@Override
	public void deleteAllUsers(String adminDomain, String adminEmail) {
		allUsers.clear();
	}

}
