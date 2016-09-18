package tmheo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import tmheo.entity.Person;

import java.io.Serializable;
import java.util.List;

import static java.util.stream.Collectors.toList;


/**
 * Created by taemyung on 2016. 9. 18..
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonListResponse implements Serializable {

    private Long totalElements;
    private Integer totalPages;
    private Integer numberOfElements;
    private List<PersonResponse> list;

    public PersonListResponse(Page<Person> personList) {
        totalElements = personList.getTotalElements();
        totalPages = personList.getTotalPages();
        numberOfElements = personList.getNumberOfElements();
        list = personList.getContent().stream().map(PersonResponse::new).collect(toList());
    }

}
