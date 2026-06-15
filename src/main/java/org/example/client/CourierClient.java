package org.example.client;

import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured; // Добавили импорт
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import org.example.model.Courier;
import java.util.Map;
import static io.restassured.RestAssured.given;

public class CourierClient extends BaseClient {
    private static final String COURIER_PATH = "/api/v1/courier";
    private static final String LOGIN_PATH = "/api/v1/courier/login";

    @Step("Создать курьера: {courier}")
    public ValidatableResponse create(Courier courier) {
        return given()

                .filter(new AllureRestAssured())
                .contentType(ContentType.JSON)
                .body(courier)
                .when()
                .post(COURIER_PATH)
                .then();
    }

    @Step("Логин курьера в системе: {credentials}")
    public ValidatableResponse login(Map<String, String> credentials) {
        return given()
                .filter(new AllureRestAssured()) // Добавили логирование
                .contentType(ContentType.JSON)
                .body(credentials)
                .when()
                .post(LOGIN_PATH)
                .then();
    }

    @Step("Удалить курьера с ID: {courierId}")
    public ValidatableResponse delete(int courierId) {
        return given()
                .filter(new AllureRestAssured()) // Добавили логирование
                .contentType(ContentType.JSON)
                .when()
                .delete(COURIER_PATH + "/" + courierId)
                .then();
    }
}