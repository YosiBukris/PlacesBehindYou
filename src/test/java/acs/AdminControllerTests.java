package acs;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import acs.boundaries.ActionBoundary;
import acs.boundaries.ElementBoundary;
import acs.boundaries.Location;
import acs.boundaries.NewUserDetails;
import acs.boundaries.UserBoundary;
import acs.data.entityProperties.UserRole;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class AdminControllerTests {

	private int port;
	private String url;
	private RestTemplate restTemplate;

	@LocalServerPort
	public void setPort(int port) {
		this.port = port;
	}

	@PostConstruct
	public void init() {
		this.url = "http://localhost:" + this.port + "/acs/admin";
		this.restTemplate = new RestTemplate();
	}

	@BeforeEach
	public void setup() {
		//Create admin user
		this.restTemplate.postForObject("http://localhost:" + this.port + "/acs/users",
				new NewUserDetails("admin@gmail.com", UserRole.ADMIN, "admin", ":)"), UserBoundary.class);
		// Create Single user and element Before all tests
		this.restTemplate.postForObject("http://localhost:" + this.port + "/acs/users",
				new NewUserDetails("testing@gmail.com", UserRole.MANAGER, "test", ":)"), UserBoundary.class);
		ElementBoundary test = new ElementBoundary();
		test.setActive(true);
		test.setElementAttributes(new HashMap<String,Object>());
		test.setLocation(new Location(13,13));
		test.setName("test");
		test.setType("testType");
		this.restTemplate.postForObject("http://localhost:" + this.port + "/acs/elements" + "/{mangerDomain}/{mangerEmail}", test
				, ElementBoundary.class, "2020b.or.zaidman.placebehindyou", "testing@gmail.com");
	}

	@AfterEach
	public void teardown() {
		//On comment to not erase DB every single time
		/*
		this.restTemplate.delete(this.url + "/elements/{adminDomain}/{adminEmail}", "2020b.or.zaidman.placebehindyou", "admin@gmail.com");
		this.restTemplate.delete(this.url + "/users/{adminDomain}/{adminEmail}", "2020b.or.zaidman.placebehindyou", "admin@gmail.com");
*/
	}

	@Test
	public void testRetrieveAllActionsFromServerWithEmptyDatabaseThrows4xxError() throws Exception {
		//GIVEN Empty DB
		this.restTemplate.delete(this.url + "/actions/{adminDomain}/{adminEmail}", "2020b.or.zaidman.placebehindyou", "admin@gmail.com");
		//WHEN ADMIN try to retrieve all actions
		//THEN an exception been thrown
		this.restTemplate.delete(this.url + "/actions/{adminDomain}/{adminEmail}", "2020b.or.zaidman.placebehindyou", "admin@gmail.com");
		assertThrows(HttpClientErrorException.class, () ->
		this.restTemplate.getForObject(this.url+ "/actions/{adminDomain}/{adminEmail}", ActionBoundary[].class , "2020b.or.zaidman.placebehindyou" , "test@gmail.com"));
	}
	
	@Test
	public void testGetAllUsersAfterDbHasBeenDeletedThrows5xxError() {
		//GIVEN no users in DB
		this.restTemplate.delete(this.url + "/users/{adminDomain}/{adminEmail}", "2020b.or.zaidman.placebehindyou", "admin@gmail.com");
		//WHEN ADMIN try to retrieve all users
		//THEN an exception been thrown
		assertThrows(HttpServerErrorException.class, () ->
		this.restTemplate.getForObject(this.url+ "/users/{adminDomain}/{adminEmail}", ActionBoundary[].class , "2020b.or.zaidman.placebehindyou" , "test@gmail.com"));
	}
	
	
	@Test
	public void testRetrieveAllUsersFromServerWith5UsersInDatabaseReturnsAllOfThem() {
		//GIVEN 5 users in the DB
		//Clear DB
		this.restTemplate.delete(this.url + "/users/{adminDomain}/{adminEmail}", "2020b.or.zaidman.placebehindyou", "admin@gmail.com");
		//Add 5 users 
		List<UserBoundary> storedUsers = new ArrayList<>();
		for (int i=0; i<5; i++) {
			String mail = i+"@gmail.com";
			String userName = String.valueOf(i);
		storedUsers.add(this.restTemplate.postForObject("http://localhost:" + this.port + "/acs/users",
				new NewUserDetails(mail, UserRole.ADMIN, userName, ":)"), UserBoundary.class));
		}
		//WHEN ADMIN try to export all users
		UserBoundary[] actualUsers = this.restTemplate.getForObject(this.url+ "/users/{adminDomain}/{adminEmail}", UserBoundary[].class , "2020b.or.zaidman.placebehindyou" , "1@gmail.com");
		//THEN he gets all of them
		assertThat(actualUsers).usingRecursiveFieldByFieldElementComparator()
		.containsExactlyInAnyOrderElementsOf(storedUsers);
	}
	
}