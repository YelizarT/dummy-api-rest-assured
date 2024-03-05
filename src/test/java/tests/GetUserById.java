package tests;

import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static tests.BaseTest.getRequest;
import static tests.BaseTest.postRequestWithoutBody;

public class GetUserById {

    String requestUserId = "60d0fe4f5311236168a109ce";
    String invalidUserId = "2323";

    @Test
    public void invalidUserId() {
        Response response = getRequest("/user/" + invalidUserId, 400);
        assertEquals("PARAMS_NOT_VALID", response.body().jsonPath().getString("error"));

    }

    @Test
    public void inCorrectMethod() {
        Response response = postRequestWithoutBody("/user/" + requestUserId, 404);
        assertEquals("PATH_NOT_FOUND", response.body().jsonPath().get("error"));
    }

}

