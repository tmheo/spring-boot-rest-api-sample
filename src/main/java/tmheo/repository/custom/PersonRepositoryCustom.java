package tmheo.repository.custom;

import tmheo.entity.Person;

import java.util.List;

/**
 * Created by taemyung on 2016. 9. 10..
 */
public interface PersonRepositoryCustom {

    List<Person> searchByFirstName(String firstName);

}
