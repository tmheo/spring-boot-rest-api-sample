package tmheo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import tmheo.entity.Person;
import tmheo.repository.PersonRepository;

@SpringBootApplication
@EnableCaching
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(PersonRepository personRepository) {
        return args -> {
            personRepository.save(new Person("first name1", "last name1", "test1@test.com"));
            personRepository.save(new Person("first name2", "last name2", "test2@test.com"));
            personRepository.save(new Person("first name3", "last name3", "test3@test.com"));
        };
    }

}
