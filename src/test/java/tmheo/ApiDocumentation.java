package tmheo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import tmheo.entity.Person;
import tmheo.model.PersonRequest;
import tmheo.service.PersonService;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by taemyung on 2016. 9. 10..
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = DemoTestApplication.class)
@WebAppConfiguration
public class ApiDocumentation {

    @Rule
    public final JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("target/generated-snippets");

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PersonService personService;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
                .apply(documentationConfiguration(this.restDocumentation))
                .alwaysDo(print())
                .build();
    }

    @Test
    public void personCreateExample() throws Exception {

        PersonRequest personRequest = new PersonRequest();
        personRequest.setFirstName("first name");
        personRequest.setLastName("last name");
        personRequest.setEmail("test@test.com");

        this.mockMvc.perform(
                post("/api/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(personRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id", is(notNullValue())))
                .andExpect(jsonPath("firstName", is(personRequest.getFirstName())))
                .andExpect(jsonPath("lastName", is(personRequest.getLastName())))
                .andExpect(jsonPath("email", is(personRequest.getEmail())))
                .andDo(document("person-create-example",
                        requestFields(
                                fieldWithPath("firstName").description("The first name of the person"),
                                fieldWithPath("lastName").description("The last name of the person"),
                                fieldWithPath("email").description("The email of the person")),
                        responseFields(
                                fieldWithPath("id").description("The id of the person"),
                                fieldWithPath("firstName").description("The first name of the person"),
                                fieldWithPath("lastName").description("The last name of the person"),
                                fieldWithPath("email").description("The email of the person"))));

    }

    @Test
    public void personGetExample() throws Exception {

        Person person = new Person();
        person.setFirstName("first name");
        person.setLastName("last name");
        person.setEmail("test@test.com");

        person = personService.save(person);

        this.mockMvc.perform(
                get("/api/person/{id}", person.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id", is(person.getId().intValue())))
                .andExpect(jsonPath("firstName", is(person.getFirstName())))
                .andExpect(jsonPath("lastName", is(person.getLastName())))
                .andExpect(jsonPath("email", is(person.getEmail())))
                .andDo(document("person-get-example",
                        pathParameters(
                                parameterWithName("id").description("The id of the person")),
                        responseFields(
                                fieldWithPath("id").description("The id of the person"),
                                fieldWithPath("firstName").description("The first name of the person"),
                                fieldWithPath("lastName").description("The last name of the person"),
                                fieldWithPath("email").description("The email of the person"))));

    }

}
