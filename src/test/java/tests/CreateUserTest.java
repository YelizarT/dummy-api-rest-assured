package tests;

import com.github.javafaker.Faker;
import dto.CreateUserRequest;
import dto.UserResponse;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;
import static tests.BaseTest.postRequest;

public class CreateUserTest {
    Faker faker = new Faker();

    @Test
    public void successCreateUser() {

        String userEmail = faker.internet().emailAddress();
        String firstName = faker.name().firstName();
        String lastname = faker.name().lastName();
        CreateUserRequest requestBody = new CreateUserRequest(
                firstName, lastname, userEmail, "", "", "");
        CreateUserRequest reqBodyBuilder = CreateUserRequest.builder()
                .firstName(firstName)
                .lastName(lastname)
                .email(userEmail)
                .build();

        UserResponse userResponse = postRequest("/user/create", 200, reqBodyBuilder)
                .body().jsonPath().getObject("", UserResponse.class);

        assertNotNull(userResponse.getId());
        assertNotNull(userResponse.getFirstName());
        assertNotNull(userResponse.getLastName());
//      assertNotNull(userResponse.getGender()); в запросе не отправляем, поэтому не сможем получить ответ
        assertNotNull(userResponse.getEmail());
        assertNotNull(userResponse.getRegisterDate());
        assertNotNull(userResponse.getUpdatedDate());
        //Check that email value from request equal to email value from response
        assertEquals(userEmail, userResponse.getEmail());
        //registerDate  == updatedDate
        assertEquals(userResponse.getRegisterDate(), userResponse.getUpdatedDate());
    }

    @Test
    public void successCreateUserAdditionalFields() {
        //Check that all fields are not empty includes
        //    "gender":"male",
        //    "title" : "mr",
        //    "phone" : "849765455322"
        Faker faker = new Faker();

        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();
        String email = faker.internet().emailAddress();

        CreateUserRequest request = new CreateUserRequest(firstName, lastName, email, "mr", "male", "849765455322");
        UserResponse response = postRequest("/user/create", 200, request)
                .body().jsonPath().getObject("", UserResponse.class);

        assertNotNull(response.getId());
        assertNotNull(response.getFirstName());
        assertNotNull(response.getLastName());
        assertNotNull(response.getEmail());
        assertNotNull(response.getGender());
        assertNotNull(response.getTitle());
        assertNotNull(response.getPhone());
    }

    @Test
    public void successCreateUserAdditionalFieldsBuilder() {

        Faker faker = new Faker();

        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();
        String email = faker.internet().emailAddress();

        CreateUserRequest reqBodyBuilder = CreateUserRequest.builder()
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .gender("male")
                .title("mr")
                .phone("849765455322")
                .build();
        UserResponse response = postRequest("/user/create", 200, reqBodyBuilder)
                .body().jsonPath().getObject("", UserResponse.class);

        assertNotNull(response.getId());
        assertNotNull(response.getFirstName());
        assertNotNull(response.getLastName());
        assertNotNull(response.getEmail());
        assertNotNull(response.getGender());
        assertNotNull(response.getTitle());
        assertNotNull(response.getPhone());
    }

    @Test
    public void createUserWithoutEmail() {
        Faker faker = new Faker();
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();

        CreateUserRequest requestBody = CreateUserRequest.builder()
                .firstName(firstName)
                .lastName(lastName)
                .build();
        Response response = postRequest("/user/create", 400,
                requestBody);
        assertTrue(response.getBody().asString().contains("Path `email` is required."));
    }

    @Test
    public void createUserWithoutFirstName() {
        String lastName = faker.name().lastName();
        String userEmail = faker.internet().emailAddress();

        CreateUserRequest requestBody = CreateUserRequest.builder()
                .lastName(lastName)
                .email(userEmail)
                .build();
        Response response = postRequest("/user/create", 400,
                requestBody);
        assertTrue(response.getBody().asString().contains("Path `firstName` is required."));
    }

    @Test
    public void createUserWithoutLastName() {
        String firstName = faker.name().firstName();
        String userEmail = faker.internet().emailAddress();

        CreateUserRequest requestBody = CreateUserRequest.builder()
                .firstName(firstName)
                .email(userEmail)
                .build();
        Response response = postRequest("/user/create", 400,
                requestBody);
        assertTrue(response.getBody().asString().contains("Path `lastName` is required."));
    }

    @Test
    public void createUserWithoutAllFields() {

        CreateUserRequest requestBody = CreateUserRequest.builder()
                .build();
        Response response = postRequest("/user/create", 400,
                requestBody);

        assertTrue(response.body().asString().contains("Path `lastName` is required."));
        assertTrue(response.body().asString().contains("Path `firstName` is required."));
        assertTrue(response.body().asString().contains("Path `email` is required."));

        //String expectedErrorMessage = "{\"error\":\"BODY_NOT_VALID\",\"data\":{\"lastName\":\"Path `lastName` is required.\",\"firstName\":\"Path `firstName` is required.\",\"email\":\"Path `email` is required.\"}}";
        //assertTrue(response.getBody().asString().contains(expectedErrorMessage));
    }

    @Test
    public void successCreateUserNew() {
        String userEmail = faker.internet().emailAddress();
        String firstName = faker.name().firstName();
        String lastname = faker.name().lastName();
        CreateUserRequest requestBody = new CreateUserRequest(
                firstName, lastname, userEmail, "", "", "");
        CreateUserRequest reqBodyBuilder = CreateUserRequest.builder()
                .firstName(firstName)
                .lastName(lastname)
                .email(userEmail)
                .build();

        Response response = postRequest("/user/create", 200, reqBodyBuilder);
        File schemaFile = new File("src/test/java/schemas/userFull.json");

        response.then()
                .assertThat()
                .body(JsonSchemaValidator.matchesJsonSchema(schemaFile));
    }
}


