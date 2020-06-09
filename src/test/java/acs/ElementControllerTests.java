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


import acs.boundaries.ElementBoundary;
import acs.boundaries.Location;
import acs.boundaries.NewUserDetails;
import acs.boundaries.UserBoundary;

import acs.data.entityProperties.UserRole;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ElementControllerTests {
	
	private int port;
	private String url;
	private RestTemplate restTemplate;

	@LocalServerPort
	public void setPort(int port) {
		this.port = port;
	}
	
	@PostConstruct
	public void init() {
		this.url = "http://localhost:" + this.port + "/acs/elements";
		this.restTemplate = new RestTemplate();
	}
	
	@BeforeEach
	public void setup() {
		// Create Single user and element Before all tests
		this.restTemplate.postForObject("http://localhost:" + this.port + "/acs/users",
				new NewUserDetails("testing@gmail.com", UserRole.MANAGER, "test", ":)"), UserBoundary.class);
		ElementBoundary test = new ElementBoundary();
		test.setActive(true);
		test.setElementAttributes(new HashMap<String,Object>());
		test.setLocation(new Location(13,13));
		test.setName("test");
		test.setType("testType");
		this.restTemplate.postForObject(this.url + "/{mangerDomain}/{mangerEmail}", test
				, ElementBoundary.class, "2020b.or.zaidman.placebehindyou", "testing@gmail.com");
	}

	@AfterEach
	// Delete after every test the element and the user from the setup
	public void teardown() {
		// Create ADMIN to delete the users & elements for the unit testing
		this.restTemplate.postForObject("http://localhost:" + this.port + "/acs/users",
				new NewUserDetails("admin@gmail.com", UserRole.ADMIN, "admin", ":)"), UserBoundary.class);
		
		ElementBoundary[] elementToDelete = this.restTemplate.getForObject(this.url + "/{userDomain}/{userEmail}/search/byName/{name}",
				ElementBoundary[].class, "2020b.or.zaidman.placebehindyou", "testing@gmail.com","test");
		if(elementToDelete.length != 0)
		{
		this.restTemplate
				.delete("http://localhost:" + port + "/acs/admin/elements/{adminDomain}/{adminEmail}/{elementDomain}/{elementID}", "2020b.or.zaidman.placebehindyou", "admin@gmail.com", "2020b.or.zaidman.placebehindyou", elementToDelete[0].getElementId().getId());
		}
		this.restTemplate
		.delete("http://localhost:" + port + "/acs/admin/users/{adminDomain}/{adminEmail}/{userDomain}/{userEmail}", "2020b.or.zaidman.placebehindyou", "admin@gmail.com","2020b.or.zaidman.placebehindyou", "testing@gmail.com");
	}
	
	@Test //works good
	public void testRetrieveSpecificElementExistsInDB() {
		//GIVEN an existing element in DB -> using the element we created in the setup() function
		ElementBoundary[] elementToFind = this.restTemplate.getForObject(this.url + "/{userDomain}/{userEmail}/search/byName/{name}",
				ElementBoundary[].class, "2020b.or.zaidman.placebehindyou", "testing@gmail.com", "test");
		String idToFind =elementToFind[0].getElementId().getId();
		//WHEN the user try to find the element
		ElementBoundary retrievedElement =this.restTemplate.getForObject(this.url + "/{userDomain}/{userEmail}/{elemetDomain}/{elementID}",
				ElementBoundary.class, "2020b.or.zaidman.placebehindyou", "test@gmail.com", "2020b.or.zaidman.placebehindyou" , idToFind);
		//THEN the server returns the wanted element
		assertThat(retrievedElement).usingRecursiveComparison().isEqualTo(elementToFind[0]);
	}
	
	
	@Test //works good
	public void testRetrieveSpecificElementFromServerWithEmptyDatabaseReturnStatusDifferenceFrom4xx() throws Exception {
		// GIVEN the DB is empty
		this.restTemplate
		.delete("http://localhost:" + port + "/acs/admin/elements/{adminDomain}/{adminEmail}", "2020b.or.zaidman.placebehindyou", "admin@gmail.com");
		// WHEN user tries to retrieve specific element
		// THEN the server returns status != 2xx
		assertThrows(HttpClientErrorException.class, () -> this.restTemplate.getForObject(this.url + "/{userDomain}/{userEmail}/{elementDomain}/{elementID}",
				ElementBoundary.class, "2020b.or.zaidman.placebehindyou", "test2@gmail.com", "2020b.or.zaidman.placebehindyou" , "test2"));
	}
	
	
	
	@Test //works good
	public void testGetAllElementsFromServerWithEmptyDatabaseReturnStatus5xxAndAnEmptyArray() throws Exception {
		// GIVEN the server is empty
		this.restTemplate
		.delete("http://localhost:" + port + "/acs/admin/elements/{adminDomain}/{adminEmail}", "2020b.or.zaidman.placebehindyou", "admin@gmail.com");
		// WHEN user tries to retrieve an element
		// THEN an error been thrown
		assertThrows(HttpServerErrorException.class, () ->
		this.restTemplate
		.getForObject(this.url + "/{userDomain}/{userEmail}", 
						 ElementBoundary[].class, "2020b.or.zaidman.placebehindyou", "test@gmail.com"));
		
	}
	
	
	@Test //works
	public void testNonManagerCreateElementReturnError4xx() throws Exception {
	// GIVEN userRole != MANAGER
		this.restTemplate.postForObject("http://localhost:" + this.port + "/acs/users",
				new NewUserDetails("testing@gmail.com", UserRole.PLAYER, "test", ":)"), UserBoundary.class);
		ElementBoundary test = new ElementBoundary();
		test.setActive(true);
		test.setElementAttributes(new HashMap<String,Object>());
		test.setLocation(new Location(13,13));
		test.setName("test");
		test.setType("testType");
	//WHEN the user tries to create an element
	//THEN error been thrown
		assertThrows(HttpClientErrorException.class, () ->
		this.restTemplate.postForObject(this.url + "/{mangerDomain}/{mangerEmail}", test
		, ElementBoundary.class, "2020b.or.zaidman.placebehindyou", "testing@gmail.com"));
	}
	
	
	@Test //works
	public void testUpdateNonExistsElementThrowsElementNotFoundException4xxError() {
		//GIVEN Element doesn't exist
		String elementDomain = "2020b.or.zaidman.placebehindyou";
		String nonExistsElementId = "123";
		//WHEN user tries to update that element
		//THEN element not found exception been thrown
		assertThrows(HttpClientErrorException.class, () ->
		this.restTemplate.put(this.url + "/{mangerDomain}/{mangerEmail}/{elementDomain}/{elementID}", 
				elementDomain, "2020b.or.zaidman.placebehindyou", "test@gmail.com", elementDomain, nonExistsElementId));
		
	}
	

	@Test //works
	public void testGetAllElementsFromServerWith10ElementsInDatabaseReturnsAllElementsStoredInDatabase() {
		//Create Manager
		this.restTemplate.postForObject("http://localhost:" + this.port + "/acs/users",
				new NewUserDetails("testing@gmail.com", UserRole.MANAGER, "test", ":)"), UserBoundary.class);
		// GIVEN the database contains a 10 elements
		ElementBoundary test = new ElementBoundary();
		//Clear all elements:
		this.restTemplate
		.delete("http://localhost:" + port + "/acs/admin/elements/{adminDomain}/{adminEmail}", "2020b.or.zaidman.placebehindyou", "admin@gmail.com");
		
		List<ElementBoundary> storedElements = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			test.setActive(true);
			test.setElementAttributes(new HashMap<String,Object>());
			test.setLocation(new Location(123+i,123+i));
			test.setName("test"+i);
			test.setType("testType"+i);
			storedElements.add(
				this.restTemplate
				  .postForObject(
						this.url  + "/{mangerDomain}/{mangerEmail}" ,
						test, 
						ElementBoundary.class, "2020b.or.zaidman.placebehindyou", "testing@gmail.com")
				  );
		}

		 ElementBoundary[] actualElements = this.restTemplate.getForObject(this.url + "/{userDomain}/{userEmail}",
				 ElementBoundary[].class, "2020b.or.zaidman.placebehindyou", "testing@gmail.com");

		
		assertThat(actualElements).usingRecursiveFieldByFieldElementComparator()
		.containsExactlyInAnyOrderElementsOf(storedElements);
	}
	
	@Test
	public void testAddRateToNonExistsElementThrows4xxError() {
		//GIVEN Element doesn't exist
		String elementDomain = "2020b.or.zaidman.placebehindyou";
		String nonExistsElementId = "123";
		//WHEN user tries to rate that element
		//THEN element not found exception been thrown
		assertThrows(HttpClientErrorException.class, () ->
		this.restTemplate.put(this.url + "/{managerDomain}/{managerEmail}/{elementDomain}/{elementID}/rate", 
				elementDomain, "2020b.or.zaidman.placebehindyou", "test@gmail.com", elementDomain, nonExistsElementId, 5));
	}




	
}
