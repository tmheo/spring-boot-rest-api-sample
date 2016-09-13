package tmheo.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import tmheo.DemoTestApplication;
import tmheo.entity.Person;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * Created by taemyung on 2016. 9. 10..
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = DemoTestApplication.class)
@WebAppConfiguration
public class PersonRepositoryTest {

    @Autowired
    private PersonRepository personRepository;

    @Test
    public void testSave() throws Exception {

        // Given
        Person person = new Person();
        person.setFirstName("first name");
        person.setLastName("last name");
        person.setEmail("test@test.com");

        // When
        personRepository.save(person);

        // Then
        assertThat(person.getId(), notNullValue());

    }

    @Test
    public void testSearchByFirstName() throws Exception {

        // Given
        int count = 5;

        for (int i = 1; i <= count; i++) {
            Person person = new Person();
            person.setFirstName("first name" + i);
            person.setLastName("last name" + i);
            person.setEmail("test" + i + "@test.com");

            personRepository.save(person);
        }

        // When
        List<Person> personList = personRepository.searchByFirstName("first name");

        // Then
        assertThat(personList.size() >= count, is(true));

    }

}
