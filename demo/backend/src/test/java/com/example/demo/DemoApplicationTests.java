package com.example.demo;
import com.example.demo.controller.UserController;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
@SpringBootTest
class DemoApplicationTests {

	@Autowired
	private UserController userController;

	@Test
	void contextLoads() {
		assertThat(userController).isNotNull();
	}

	

}
