package tests;

import com.github.javafaker.Faker;
import dto.CreateUserRequest;
import dto.UserResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static tests.BaseTest.*;


public class DeleteUserTest {
    Faker faker = new Faker();

    @Test
    public void deleteExistingUser() {
        //Pre steps
        String userEmail = faker.internet().emailAddress();
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();

        CreateUserRequest reqBodyBuilder = CreateUserRequest.builder()
                .firstName(firstName)
                .lastName(lastName)
                .email(userEmail)
                .build();
        Response response = postRequest("/user/create", 200, reqBodyBuilder);

        UserResponse responseBody = response.getBody().as(UserResponse.class);
        String userId = responseBody.getId();

        //Test steps
        String deleteEndpoint = "/user/" + userId;
        Response deleteUserResponse = deleteRequest(deleteEndpoint, 200);

        assertEquals(userId, deleteUserResponse.getBody().jsonPath().getString("id"));

    }

    //пример преподавателя:
    @Test
    public void deleteExistingUserNew() {
        //Pre-Steps
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
        //Test steps
        Response deleteResponse = deleteRequest("/user/" + userID, 200);
        String deletedUserIdFromResponse =
                deleteResponse.body().jsonPath().getString("id");
        assertEquals(userID, deletedUserIdFromResponse);
    }

    @Test
    public void testDeleteDeletedUser() {

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

        Response deleteUserResponse = deleteRequest("/user/" + userID, 200);
        String deletedUserIdFromResponse = deleteUserResponse.getBody().jsonPath().getString("id");
        assertEquals(userID, deletedUserIdFromResponse);

        Response deleteDeletedUserResponse = deleteRequest("/user/" + userID, 404);
        assertEquals("RESOURCE_NOT_FOUND", deleteDeletedUserResponse.getBody().jsonPath().getString("error"));
    }

    @Test
    public void testDeleteInvalidUser() {

        Response deleteInvalidUserResponse = deleteRequest("/user/65df1", 400);

        assertEquals("PARAMS_NOT_VALID", deleteInvalidUserResponse.getBody().jsonPath().getString("error"));
    }
        //пример преподавателя:
//        String invalidUserId = "213426";
//        Response deletedUserResponse = deleteRequest("/user/" + invalidUserId, 400);
//        String errorMessage = deletedUserResponse.body().jsonPath().getString("error");
//        assertEquals("PARAMS_NOT_VALID", errorMessage);
//    }
}

