package acs;

import static org.junit.jupiter.api.Assertions.assertThrows;

import javax.annotation.PostConstruct;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.web.client.RestTemplate;

import acs.boundaries.ActionBoundary;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ActionControllerTests {
	
	private int port;
	private String url;
	private RestTemplate restTemplate;

	@LocalServerPort
	public void setPort(int port) {
		this.port = port;
	}
	
	@PostConstruct
	public void init() {
		this.url = "http://localhost:" + this.port + "/acs/actions";
		this.restTemplate = new RestTemplate();
	}
	
	@BeforeEach
	public void setup() {
		this.restTemplate
				.delete("http://localhost:" + port + "/acs/admin/actions/2020b.or.zaidman.placebehindyou/test@gmail.com");
	}

	@AfterEach
	public void teardown() {
		this.restTemplate
				.delete("http://localhost:" + port + "/acs/admin/actions/2020b.or.zaidman.placebehindyou/test@gmail.com");
	}
	

	
	@Test
	public void testRetrieveSpecificActionFromServerWithEmptyDatabaseReturnStatusDifferenceFrom4xx() throws Exception {
		//GIVEN the DB is empty
		this.restTemplate
		.delete("http://localhost:" + port + "/acs/admin/actions/2020b.or.zaidman.placebehindyou/test@gmail.com");
		//WHEN there is a try to invoke an action
		//THEN an error been thrown
		assertThrows(Exception.class, () -> this.restTemplate.getForObject(this.url,
				ActionBoundary.class));
	}
}