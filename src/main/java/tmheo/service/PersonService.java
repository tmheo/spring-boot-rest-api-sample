package tmheo.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tmheo.entity.Person;
import tmheo.exception.ResourceNotFoundException;
import tmheo.repository.PersonRepository;

/**
 * Created by taemyung on 2016. 9. 10..
 */
@Service
@Transactional
@Slf4j
public class PersonService {

    @Autowired
    private PersonRepository personRepository;

    public Person save(Person person) {

        log.debug("save person request for person : {}", person);

        person = personRepository.save(person);

        log.debug("save person response for person : {}", person);

        return person;

    }

    public Person get(Long id) {

        log.debug("get person request for id[{}]", id);

        Person person = personRepository.findOne(id);

        if (person == null) {
            throw new ResourceNotFoundException(String.valueOf(id));
        }

        log.debug("get person response for id[{}] : {}", id, person);

        return person;

    }

}
