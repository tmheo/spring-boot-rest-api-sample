package tmheo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Email;
import tmheo.entity.Person;
import tmheo.util.BeanUtils;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created by taemyung on 2016. 9. 10..
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonRequest implements Serializable {

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    @Email
    private String email;

    public Person convertToPerson() {
        Person person = new Person();

        BeanUtils.copyNotNullProperties(this, person);

        return person;
    }

}
