package tmheo.repository.custom;

import org.springframework.data.jpa.repository.support.QueryDslRepositorySupport;
import tmheo.entity.Person;
import tmheo.entity.QPerson;

import java.util.List;

/**
 * Created by taemyung on 2016. 9. 10..
 */
public class PersonRepositoryImpl extends QueryDslRepositorySupport implements PersonRepositoryCustom {

    public PersonRepositoryImpl() {
        super(Person.class);
    }

    @Override
    public List<Person> searchByFirstName(String firstName) {
        QPerson person = QPerson.person;

        return from(person)
                .where(person.firstName.containsIgnoreCase(firstName))
                .fetch();
    }

}
