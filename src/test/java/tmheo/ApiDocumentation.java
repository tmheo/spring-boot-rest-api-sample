package tmheo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.constraints.ConstraintDescriptions;
import org.springframework.restdocs.headers.HeaderDescriptor;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.StringUtils;
import org.springframework.web.context.WebApplicationContext;
import tmheo.entity.Person;
import tmheo.model.PersonRequest;
import tmheo.service.PersonService;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by taemyung on 2016. 9. 10..
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
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

    private HeaderDescriptor[] commonHeaders = new HeaderDescriptor[]{
            headerWithName("Authorization").description("OAuth2 JWT Token for authorization")
    };

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
                .apply(documentationConfiguration(this.restDocumentation).uris()
                                .withScheme("https")
                                .withHost("example.com")
                                .withPort(443)
                ).alwaysDo(print())
                .build();
    }

    @Test
    public void errorExample() throws Exception {

        this.mockMvc.perform(
                get("/api/person/123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer your_oauth2_jwt_token"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("error", is("resource not found error")))
                .andExpect(jsonPath("error_description", is(notNullValue())))
                .andDo(document("error-example",
                                requestHeaders(commonHeaders),
                                responseFields(
                                        fieldWithPath("error").description("The HTTP error that occurred, e.g. `resource not found error`"),
                                        fieldWithPath("error_description").description("A description of the cause of the error")
                                )
                        )
                );

    }

    @Test
    public void personCreateExample() throws Exception {

        PersonRequest personRequest = new PersonRequest();
        personRequest.setFirstName("first name");
        personRequest.setLastName("last name");
        personRequest.setEmail("test@test.com");

        ConstrainedFields fields = new ConstrainedFields(PersonRequest.class);

        this.mockMvc.perform(
                post("/api/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer your_oauth2_jwt_token")
                        .content(this.objectMapper.writeValueAsString(personRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id", is(notNullValue())))
                .andExpect(jsonPath("firstName", is(personRequest.getFirstName())))
                .andExpect(jsonPath("lastName", is(personRequest.getLastName())))
                .andExpect(jsonPath("email", is(personRequest.getEmail())))
                .andDo(document("person-create-example",
                                requestHeaders(commonHeaders),
                                requestFields(
                                        fields.withPath("firstName").description("The first name of the person"),
                                        fields.withPath("lastName").description("The last name of the person"),
                                        fields.withPath("email").description("The email of the person")
                                ),
                                responseFields(
                                        fieldWithPath("id").description("The id of the person"),
                                        fieldWithPath("firstName").description("The first name of the person"),
                                        fieldWithPath("lastName").description("The last name of the person"),
                                        fieldWithPath("email").description("The email of the person")
                                )
                        )
                );

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
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer your_oauth2_jwt_token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id", is(person.getId().intValue())))
                .andExpect(jsonPath("firstName", is(person.getFirstName())))
                .andExpect(jsonPath("lastName", is(person.getLastName())))
                .andExpect(jsonPath("email", is(person.getEmail())))
                .andDo(document("person-get-example",
                                requestHeaders(commonHeaders),
                                pathParameters(
                                        parameterWithName("id").description("The id of the person")
                                ),
                                responseFields(
                                        fieldWithPath("id").description("The id of the person"),
                                        fieldWithPath("firstName").description("The first name of the person"),
                                        fieldWithPath("lastName").description("The last name of the person"),
                                        fieldWithPath("email").description("The email of the person")
                                )
                        )
                );

    }

    @Test
    public void personUpdateExample() throws Exception {

        Person person = new Person();
        person.setFirstName("first name");
        person.setLastName("last name");
        person.setEmail("test@test.com");

        person = personService.save(person);

        PersonRequest personRequest = new PersonRequest();
        personRequest.setFirstName("updated first name");
        personRequest.setLastName("updated last name");
        personRequest.setEmail("updated_test@test.com");

        ConstrainedFields fields = new ConstrainedFields(PersonRequest.class);

        this.mockMvc.perform(
                put("/api/person/{id}", person.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer your_oauth2_jwt_token")
                        .content(this.objectMapper.writeValueAsString(personRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id", is(person.getId().intValue())))
                .andExpect(jsonPath("firstName", is(personRequest.getFirstName())))
                .andExpect(jsonPath("lastName", is(personRequest.getLastName())))
                .andExpect(jsonPath("email", is(personRequest.getEmail())))
                .andDo(document("person-update-example",
                                requestHeaders(commonHeaders),
                                pathParameters(
                                        parameterWithName("id").description("The id of the person")
                                ),
                                requestFields(
                                        fields.withPath("firstName").description("The first name of the person"),
                                        fields.withPath("lastName").description("The last name of the person"),
                                        fields.withPath("email").description("The email of the person")
                                ),
                                responseFields(
                                        fieldWithPath("id").description("The id of the person"),
                                        fieldWithPath("firstName").description("The first name of the person"),
                                        fieldWithPath("lastName").description("The last name of the person"),
                                        fieldWithPath("email").description("The email of the person")
                                )
                        )
                );

    }

    @Test
    public void personDeleteExample() throws Exception {

        Person person = new Person();
        person.setFirstName("first name");
        person.setLastName("last name");
        person.setEmail("test@test.com");

        person = personService.save(person);

        this.mockMvc.perform(
                delete("/api/person/{id}", person.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer your_oauth2_jwt_token"))
                .andExpect(status().isNoContent())
                .andDo(document("person-delete-example",
                                requestHeaders(commonHeaders),
                                pathParameters(
                                        parameterWithName("id").description("The id of the person")
                                )
                        )
                );

    }

    @Test
    public void personListExample() throws Exception {

        this.mockMvc.perform(
                get("/api/person?page=0&size=10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer your_oauth2_jwt_token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("totalElements", notNullValue()))
                .andDo(document("person-list-example",
                                requestHeaders(commonHeaders),
                                requestParameters(
                                        parameterWithName("page").description("The page of the person list. starts with zero"),
                                        parameterWithName("size").description("The size of the page")
                                ),
                                responseFields(
                                        fieldWithPath("totalElements").description("The total elements of the person"),
                                        fieldWithPath("totalPages").description("The total pages of the person list"),
                                        fieldWithPath("numberOfElements").description("The number of elements in the current page"),
                                        fieldWithPath("list.[]").description("The list of the person"),
                                        fieldWithPath("list.[].id").description("The id of the person"),
                                        fieldWithPath("list.[].firstName").description("The first name of the person"),
                                        fieldWithPath("list.[].lastName").description("The last name of the person"),
                                        fieldWithPath("list.[].email").description("The email of the person")
                                )
                        )
                );

    }

    private static class ConstrainedFields {

        private final ConstraintDescriptions constraintDescriptions;

        ConstrainedFields(Class<?> input) {
            this.constraintDescriptions = new ConstraintDescriptions(input);
        }

        private FieldDescriptor withPath(String path) {
            return fieldWithPath(path).attributes(key("constraints").value(StringUtils
                    .collectionToDelimitedString(this.constraintDescriptions
                            .descriptionsForProperty(path), ". ")));
        }
    }

}
