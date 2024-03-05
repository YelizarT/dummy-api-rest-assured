package tests;

import dto.User;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import lombok.Data;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static tests.BaseTest.getRequest;
import static tests.BaseTest.getRequestWithoutAppID;

public class GetUserListTest {
    //Request
    //https://dummyapi.io/data/v1/user
    //200 OK list of users
    @Test
    public void getUserList() {
        //getRequest("/user", 200);
            /* given().baseUri("https://dummyapi.io/data/v1")
                    .header("app-id", "65438323d1f7d9e2dbaf1c9c")
                    .when().log().all()
                    .get("/user")
                    .then().log().all().statusCode(200);*/ //закоментировал, потому что заменил этот код строчкой сверху, вынес все это в BaseTest
        List<User> users = getRequest("/user", 200)
                .body().jsonPath().getList("data", User.class);
        System.out.println(users.get(0).getFirstName());
        //Check that id of each user is not empty
        for (User oneUser : users) {
            assertFalse(oneUser.getId().isEmpty(), "User id should not be empty");
        }

    }

    @Test
    public void getUserListWithoutAppID() {
        getRequestWithoutAppID("/user", 403);
            /* given().baseUri("https://dummyapi.io/data/v1")
                    .when().log().all()
                    .get("/user")
                    .then().log().all().statusCode(403)
                    .statusLine("HTTP/1.1 403 Forbidden");*/
    }

    //limit = 10
    //List <User> users
    //int limit
    //Check that users quantity == limit
    @Test
    public void getUserListWithSpecLimit() {
        int limitValue = 10;
        Response response = getRequest("/user?limit=" + limitValue, 200);
        int expectedLimit = response.body().jsonPath().getInt("limit");

        List<User> user = response.body().jsonPath().getList("data", User.class);
        assertEquals(expectedLimit, user.size());
    }
//    @ParameterizedTest
//    @ValueSource(ints = {0, -1, 4, 5, 10, 50, 51, 100, 1000})
//    public void getUserListWithSpecLimit(int limitValue) {
//        int expectedLimit = (limitValue < 5 || limitValue > 50) ? 20 : limitValue;
//        Response response = getRequest("/user?limit=" + limitValue, 200);
//
//        List<User> users = response.body().jsonPath().getList("data", User.class);
//        assertEquals(expectedLimit, users.size());
//    }

    //ЗАДАЧА
    //проверить, что то значение лимит которое мы отправляем возвращается, если меньше 5 - возвращает 5,
    // если больше 50 -возвращает 50, если значение от 5 до 50 - возращает само заданное значение!
    static Stream<Integer> provideLimits() {
        return Stream.of(0, -1, 4, 5, 10, 50, 51, 100, 1000);
    }

    @ParameterizedTest
    @MethodSource("provideLimits")
    public void testLimitBoundaries(int limitValue) {
        int expectedLimit = getValidLimit(limitValue);

        Response response = getRequest("/user?limit=" + limitValue, 200);
        int actualLimit = response.body().jsonPath().getInt("limit");
        assertEquals(expectedLimit, actualLimit);
    }

    private int getValidLimit(int limitValue) {
        if (limitValue < 5) {
            return 5;
        } else if (limitValue > 50) {
            return 50;
        } else {
            return limitValue;
        }
    }
    //второй вариант решения задачи сверху:

    @ParameterizedTest
    @MethodSource("validData")
    public void getUserListWithSpecLimitDP(int limitValue, int expectedLimit) {
        Response response = getRequest("/user?limit=" + limitValue, 200);
        int actualLimit = response.body().jsonPath().getInt("limit");
        assertEquals(expectedLimit, actualLimit);
    }

    static Stream<Arguments> validData() {
        return Stream.of(
                Arguments.arguments(0, 5),
                Arguments.arguments(1, 5),
                Arguments.arguments(-1, 5),
                Arguments.arguments(1000, 50)
        );
    }

    public class GetUserById {
        String requestUserId = "60d0fe4f5311236168a109ce";//zavodim peremennuju, cgtobi ne menjatj ejo v end point

        @Test

        public void getValidUserById() {

            User user = getRequest("/user/" + requestUserId, 200)
                    .body().jsonPath().getObject("", User.class);


            assertFalse(user.getId().isEmpty());
            assertFalse(user.getFirstName().isEmpty());
            assertFalse(user.getTitle().isEmpty());
            assertFalse(user.getPicture().isEmpty());
            assertFalse(user.getEmail().isEmpty());
            assertFalse(user.getGender().isEmpty());
            assertFalse(user.getDateOfBirth().isEmpty());
//        assertFalse(user.getStreet().isEmpty());
//        assertFalse(user.getCity().isEmpty());
//        assertFalse(user.getState().isEmpty());
//        assertFalse(user.getCountry().isEmpty());
//        assertFalse(user.getTimezone().isEmpty());

        }


    }
}







