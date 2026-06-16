package org.example.client;

import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import org.example.model.Order;
import static io.restassured.RestAssured.given;

public class OrderClient extends BaseClient {
    private static final String ORDER_PATH = "/api/v1/orders";

    @Step("Создать заказ: {order}")
    public ValidatableResponse createOrder(Order order) {
        return given()
                .baseUri(RestAssured.baseURI)
                .filter(new AllureRestAssured())
                .contentType(ContentType.JSON)
                .body(order)
                .when()
                .post(ORDER_PATH)
                .then();
    }

    @Step("Получить список всех заказов")
    public ValidatableResponse getOrderList() {
        return given()
                .baseUri(RestAssured.baseURI)
                .filter(new AllureRestAssured())
                .contentType(ContentType.JSON)
                .when()
                .get(ORDER_PATH)
                .then();
    }
}
