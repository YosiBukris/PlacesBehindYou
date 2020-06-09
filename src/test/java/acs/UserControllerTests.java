package acs;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import javax.annotation.PostConstruct;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.web.client.RestTemplate;

import acs.boundaries.NewUserDetails;
import acs.boundaries.UserBoundary;
import acs.data.entityProperties.UserRole;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class UserControllerTests {

	private int port;
	private String url;
	private RestTemplate restTemplate;

	@LocalServerPort
	public void setPort(int port) {
		this.port = port;
	}

	@PostConstruct
	public void init() {
		this.url = "http://localhost:" + port + "/acs/users";
		this.restTemplate = new RestTemplate();
	}

	@BeforeEach
	public void setup()	{
		// Create Single user Before all tests
		this.restTemplate.postForObject(this.url,
				new NewUserDetails("test@gmail.com", UserRole.ADMIN, "test", ":)"), UserBoundary.class);
	}

	@AfterEach
	public void teardown() {
		// Delete Specific user from DB with domain and Email after all tests
		this.restTemplate
		.delete("http://localhost:" + port + "/acs/admin/users/2020b.or.zaidman.placebehindyou/test@gmail.com/2020b.or.zaidman.placebehindyou/test@gmail.com");
	}

	@Test
	public void testLoginNonExistUserWithEmptyDatabaseReturnStatusDifferenceFrom2xx() throws Exception {
		// GIVEN non exists user
		// WHEN he tries to login

		// THEN the server returns status != 2xx
		assertThrows(Exception.class, () -> this.restTemplate.getForObject(this.url + "/login/{userDomain}/{userEmail}",
				UserBoundary.class, "2020b.or.zaidman.placebehindyou", "test123@gmail.com"));
	}

	@Test
	public void testLoginExistUserWithDatabaseContainingThatUserRetreivesThatUser() {
		// GIVEN the database contains a user with email: test3@gmail.com and domain:
		// 2020b.or.zaidman.placebehindyou
		UserBoundary newUser = this.restTemplate.postForObject(this.url,
				new NewUserDetails("test3@gmail.com", UserRole.PLAYER, "test", ":)"), UserBoundary.class);

		// WHEN I GET /login/acs/users/2020b.or.zaidman.placebehindyou/test3@gmail.com
		UserBoundary actualUser = this.restTemplate.getForObject(this.url + "/login/{userDomain}/{userEmail}",
				UserBoundary.class, "2020b.or.zaidman.placebehindyou", "test3@gmail.com");

		// THEN the server returns the same user in the database
		assertThat(actualUser).usingRecursiveComparison().isEqualTo(newUser);
	}
	
	
	@Test
	public void testUpdateExistUserWithDatabaseContainingThatUserRetreviesUpdatedUserInDB() {
		// GIVEN the database contains a user with email: test@gmail.com and domain:
		// 2020b.or.zaidman.placebehindyou
		UserBoundary newUser = this.restTemplate.postForObject(this.url,
				new NewUserDetails("test2@gmail.com", UserRole.ADMIN, "test", ":)"), UserBoundary.class);

		// WHEN I PUT /login/acs/users/2020b.or.zaidman.placebehindyou/test@gmail.com
		newUser.setUsername("new Test");
		newUser.setRole(UserRole.PLAYER);
		this.restTemplate.put(this.url  + "/{userDomain}/{userEmail}", newUser, "2020b.or.zaidman.placebehindyou", "test2@gmail.com");
		// THEN the server returns the same user in the database but updated with the new details
		UserBoundary actualUser = this.restTemplate.getForObject(this.url + "/login/{userDomain}/{userEmail}",
				UserBoundary.class, "2020b.or.zaidman.placebehindyou", "test2@gmail.com");
		assertThat(actualUser).usingRecursiveComparison().isEqualTo(newUser);
	}
	
	@Test
	public void testUpdateExistUserWithDatabaseContainingThatUserRetreviesAfterChangeToRoleThatHeCantChange() {
		// GIVEN the database contains a user with email: test@gmail.com and domain:
		// 2020b.or.zaidman.placebehindyou
		UserBoundary newUser = this.restTemplate.postForObject(this.url,
				new NewUserDetails("test2@gmail.com", UserRole.PLAYER, "test", ":)"), UserBoundary.class);

		// WHEN I try to update user without ADMIN permissions /login/acs/users/2020b.or.zaidman.placebehindyou/test@gmail.com
		newUser.setUsername("new Test");
		newUser.setRole(UserRole.ADMIN);
		// THEN Exception been thrown
		assertThrows(Exception.class, () -> 
		this.restTemplate.put(this.url  + "/{userDomain}/{userEmail}", newUser, "2020b.or.zaidman.placebehindyou", "test2@gmail.com"));
	}
	

	


}
