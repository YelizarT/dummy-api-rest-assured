package tests;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class BaseTest {
    final static String BASE_URI = "https://dummyapi.io/data/v1";
    final static String APP_ID_VALUE = "65438323d1f7d9e2dbaf1c9c";
    static RequestSpecification specification = new RequestSpecBuilder()
            .setBaseUri(BASE_URI)
            .addHeader("app-id", APP_ID_VALUE)
            .setContentType(ContentType.JSON)
            .build();

    static RequestSpecification specWithoutAppID = new RequestSpecBuilder()
            .setBaseUri(BASE_URI)
            .setContentType(ContentType.JSON)
            .build();

    public static Response getRequest(String endPoint, Integer expectedStatusCode) {
        Response response = given()
                .spec(specification)
                .when()
                .log().all()
                .get(endPoint)
                .then()
                .log().all()
                .statusCode(expectedStatusCode)
                .extract().response();
        return response;
    }

    public static Response getRequestWithoutAppID(String endPoint, Integer expectedStatusCode) {
        Response response = given()
                .spec(specWithoutAppID)
                .when()
                .log().all()
                .get(endPoint)
                .then()
                .log().all()
                .statusCode(expectedStatusCode)
                .extract().response();
        return response;
    }

    public static Response postRequest (String endPoint, Integer expectedStatusCode, Object body){
        Response response = given()
                .spec(specification)
                .body(body)
                .when()
                .log().all()
                .post(endPoint)
                .then()
                .log().all()
                .statusCode(expectedStatusCode)
                .extract().response();
        return response;
    }

    public static Response postRequestWithoutBody(String endPoint, Integer expectedStatusCode){
        Response response = given()
                .spec(specification)
                .when()
                .log().all()
                .post(endPoint)
                .then()
                .log().all()
                .statusCode(expectedStatusCode)
                .extract().response();
        return response;
    }

    public static Response deleteRequest(String endPoint, Integer expectedStatusCode) {
        Response response = given()
                .spec(specification)
                .when().log().all()
                .delete(endPoint)
                .then().log().all()
                .statusCode(expectedStatusCode)
                .extract().response();
        return response;
    }

    public static Response putRequest(String endPoint, Integer expectedStatusCode, Object body){
        Response response = given()
                .spec(specification)
                .body(body)
                .when()
                .log().all()
                .put(endPoint)
                .then()
                .log().all()
                .statusCode(expectedStatusCode)
                .extract().response();
        return response;
    }

}


