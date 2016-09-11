package tmheo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by taemyung on 2016. 9. 10..
 */
@Entity
@Table(name = "person")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Person implements Serializable {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    public Person(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

}
