
package org.example;

import org.example.client.CourierClient;
import org.example.model.Courier;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.Map;
import java.util.UUID;

public class CreateCourierTest {
    private CourierClient courierClient;
    private int courierId;
    private String login;
    private final String password = "password123";

    @Before
    public void setUp() {
        courierClient = new CourierClient();
        login = "courier_" + UUID.randomUUID().toString().substring(0, 8);
    }

    @After
    public void tearDown() {
        try {
            var loginResponse = courierClient.login(Map.of("login", login, "password", password));
            if (loginResponse.extract().statusCode() == 200) {
                courierId = loginResponse.extract().path("id");
                courierClient.delete(courierId);
            }
        } catch (Exception e) {
            System.err.println("Очистка: Не удалось удалить курьера: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("Успешное создание курьера")
    @Description("Проверяем код 201 и тело ok: true")
    public void shouldCreateCourierWithValidData() {
        Courier courier = new Courier(login, password, "Ivan");

        courierClient.create(courier)
                .statusCode(201)
                .body("ok", Matchers.equalTo(true));
    }

    @Test
    @DisplayName("Ошибка при создании дубликата")
    @Description("Проверяем запрет создания курьера с существующим логином")
    public void shouldNotCreateDuplicateCourier() {
        Courier courier = new Courier(login, password, "Alex");


        courierClient.create(courier).statusCode(201);

        courierClient.create(courier)
                .statusCode(409)
                .body("message", Matchers.equalTo("Этот логин уже используется. Попробуйте другой."));
    }

    @Test
    @DisplayName("Ошибка при создании курьера без логина")
    @Description("Проверяем код 400 и валидное сообщение при отсутствии логина")
    public void shouldNotCreateCourierWithoutLogin() {
        Courier courier = new Courier("", password, "Ivan");

        courierClient.create(courier)
                .statusCode(400)
                .body("message", Matchers.equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Ошибка при создании курьера без пароля")
    @Description("Проверяем код 400 и валидное сообщение при отсутствии пароля")
    public void shouldNotCreateCourierWithoutPassword() {
        Courier courier = new Courier(login, "", "Ivan");

        courierClient.create(courier)
                .statusCode(400)
                .body("message", Matchers.equalTo("Недостаточно данных для создания учетной записи"));
    }
}