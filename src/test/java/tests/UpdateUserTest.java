package tests;

import com.github.javafaker.Faker;
import dto.CreateUserRequest;
import dto.UserResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static tests.BaseTest.postRequest;
import static tests.BaseTest.putRequest;

public class UpdateUserTest {
    Faker faker = new Faker();

    @Test
    public void updateLastName() {
        String userEmail = faker.internet().emailAddress();
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();
        CreateUserRequest reqBodyBuilder = CreateUserRequest.builder()
                .firstName(firstName)
                .lastName(lastName)
                .email(userEmail)
                .build();
        Response createUserResponse = postRequest("/user/create", 200, reqBodyBuilder);
        UserResponse createdUser = createUserResponse.getBody().as(UserResponse.class);
        String userID = createdUser.getId();

        String newLastName = faker.name().lastName();

        CreateUserRequest updateRequest = new CreateUserRequest();
        updateRequest.setLastName(newLastName);

        Response updateUserResponse = putRequest("/user/" + userID, 200, updateRequest);
        UserResponse updatedUser = updateUserResponse.getBody().as(UserResponse.class);

        assertEquals(newLastName, updatedUser.getLastName());
    }

    // вариант преподавателя:
    @Test
    public void updateLastNameNew(){
        //1. Crete new user
        String userEmail = faker.internet().emailAddress();
        String firstName = faker.name().firstName();
        String lastname = faker.name().lastName();
        CreateUserRequest reqBodyBuilder = CreateUserRequest.builder()
                .firstName(firstName)
                .lastName(lastname)
                .email(userEmail).build();
        Response response = postRequest("/user/create", 200, reqBodyBuilder);
        UserResponse createdUser = response.getBody().as(UserResponse.class);
        String userID = createdUser.getId();

        CreateUserRequest body = CreateUserRequest.builder().lastName("White").build();
        //2. Change LastName
        Response updateResponse = putRequest("/user/"+userID , 200, body);
        //3. Check that lastName has changed
        UserResponse updatedUserInfo =  updateResponse.getBody().as(UserResponse.class);
        assertEquals("White", updatedUserInfo.getLastName());
    }

    @Test
    public void testUpdateUserLastNameToSingleCharacter() {

        String userEmail = faker.internet().emailAddress();
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();
        CreateUserRequest reqBodyBuilder = CreateUserRequest.builder()
                .firstName(firstName)
                .lastName(lastName)
                .email(userEmail)
                .build();
        Response createUserResponse = postRequest("/user/create", 200, reqBodyBuilder);
        UserResponse createdUser = createUserResponse.getBody().as(UserResponse.class);
        String userID = createdUser.getId();

        CreateUserRequest updateRequest = new CreateUserRequest();
        updateRequest.setLastName("Y");

        Response updateUserResponse = putRequest("/user/" + userID, 400, updateRequest);
        UserResponse updatedUser = updateUserResponse.getBody().as(UserResponse.class);

        assertEquals("Y", updatedUser.getLastName());
    }

    @Test
    public void testUpdateUserInvalidGender() {

        String userEmail = faker.internet().emailAddress();
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();
        CreateUserRequest reqBodyBuilder = CreateUserRequest.builder()
                .firstName(firstName)
                .lastName(lastName)
                .email(userEmail)
                .build();
        Response createUserResponse = postRequest("/user/create", 200, reqBodyBuilder);
        UserResponse createdUser = createUserResponse.getBody().as(UserResponse.class);
        String userID = createdUser.getId();

        CreateUserRequest updateRequest = new CreateUserRequest();
        updateRequest.setGender("www");

        Response updateUserResponse = putRequest("/user/" + userID, 400, updateRequest);

        String expectedErrorMessage = "`www` is not a valid enum value for path `gender`.";
        assertEquals(expectedErrorMessage, updateUserResponse.getBody().jsonPath().getString("message"));
    }

}

