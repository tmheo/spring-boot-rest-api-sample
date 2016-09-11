package tmheo.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tmheo.entity.Person;
import tmheo.model.PersonRequest;
import tmheo.model.PersonResponse;
import tmheo.service.PersonService;

import javax.validation.Valid;

/**
 * Created by taemyung on 2016. 9. 10..
 */
@RestController
@RequestMapping(value = "/api/person")
@Slf4j
public class PersonController {

    @Autowired
    private PersonService personService;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<PersonResponse> create(@RequestBody @Valid PersonRequest personRequest) {

        log.debug("create person request for person : {}", personRequest);

        Person person = personService.save(personRequest.convertToPerson());

        PersonResponse personResponse = new PersonResponse(person);

        log.debug("create person response for person : {}", personResponse);

        return new ResponseEntity<>(personResponse, HttpStatus.CREATED);

    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<PersonResponse> get(@PathVariable("id") Long id) {

        log.debug("get person request for id[{}]", id);

        Person person = personService.get(id);

        PersonResponse personResponse = new PersonResponse(person);

        log.debug("get person response for id[{}] : {}", id, personResponse);

        return new ResponseEntity<>(personResponse, HttpStatus.OK);

    }

}
