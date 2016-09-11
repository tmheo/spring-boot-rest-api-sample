package tmheo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tmheo.entity.Person;
import tmheo.util.BeanUtils;

import java.io.Serializable;

/**
 * Created by taemyung on 2016. 9. 10..
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonResponse implements Serializable {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;

    public PersonResponse(Person person) {
        BeanUtils.copyNotNullProperties(person, this);
    }
}
