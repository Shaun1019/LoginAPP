package com.example.demo;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.registration.UserRepository;
import com.registration.dto.User;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class UserRepositoryTest {

	@Autowired
	private UserRepository repo;
	
	@Autowired
	private TestEntityManager entitymanager;
 
	@Test
	public void testCreateUser() {
		User user = new User();
		user.setEmail("hello@gmail.com");
		user.setPassword("pass");
		user.setFirstName("hello");
		user.setLastName("you");
		
		
		User savedUser = repo.save(user);
		
		User existuser = entitymanager.find(User.class, savedUser.getId());
		
		
		assertThat(existuser.getEmail()).isEqualTo(user.getEmail());
		
		}
	
	@Test
	public void testFindUserByEmail() {
		String email = "hello@gmail.com";
		
		User user = repo.findByEmail(email);
		
		assertThat(user).isNotNull();
	}
}
