package tmheo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tmheo.entity.Person;
import tmheo.repository.custom.PersonRepositoryCustom;

/**
 * Created by taemyung on 2016. 9. 10..
 */
public interface PersonRepository extends JpaRepository<Person, Long>, PersonRepositoryCustom {
}
