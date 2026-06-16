package org.example;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.example.client.CourierClient;
import org.example.model.Courier;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.Map;
import java.util.UUID;

public class LoginCourierTest {
    private CourierClient courierClient;
    private int courierId;
    private String login;
    private final String password = "securePassword123";

    @Before
    public void setUp() {
        courierClient = new CourierClient();
        login = "courier_" + UUID.randomUUID().toString().substring(0, 8);
        Courier courier = new Courier(login, password, "AuthUser");
        courierClient.create(courier).statusCode(201);
    }

    @After
    public void tearDown() {
        if (courierId != 0) {
            try {
                courierClient.delete(courierId);
            } catch (Exception e) {
                System.err.println("Не удалось удалить курьера по ID: " + e.getMessage());
            }
        } else {
            try {
                var response = courierClient.login(Map.of("login", login, "password", password));
                if (response.extract().statusCode() == 200) {
                    int id = response.extract().path("id");
                    courierClient.delete(id);
                }
            } catch (Exception ignored) {}
        }
    }

    @Test
    @DisplayName("Успешный логин курьера")
    public void shouldLoginSuccessfully() {
        courierId = courierClient.login(Map.of("login", login, "password", password))
                .statusCode(200)
                .body("id", Matchers.notNullValue())
                .extract().path("id");
    }

    @Test
    @DisplayName("Ошибка логина при указании неверного пароля")
    @Description("Проверяем код 404 и сообщение об ошибке при неверном пароле")
    public void shouldNotLoginWithWrongPassword() {
        courierClient.login(Map.of("login", login, "password", "wrong_password_here"))
                .statusCode(404)
                .body("message", Matchers.equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Ошибка логина для несуществующего пользователя")
    @Description("Проверяем код 404, если курьера с таким логином нет в базе")
    public void shouldNotLoginWithNonExistentCourier() {
        String fakeLogin = "fake_courier_" + UUID.randomUUID().toString().substring(0, 4);
        courierClient.login(Map.of("login", fakeLogin, "password", password))
                .statusCode(404)
                .body("message", Matchers.equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Ошибка логина без указания логина")
    @Description("Проверяем код 400 и сообщение при передаче пустого логина")
    public void shouldNotLoginWithoutLoginField() {
        courierClient.login(Map.of("login", "", "password", password))
                .statusCode(400)
                .body("message", Matchers.equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Ошибка логина без указания пароля")
    @Description("Проверяем код 400 и сообщение при передаче пустого пароля")
    public void shouldNotLoginWithoutPasswordField() {
        courierClient.login(Map.of("login", login, "password", ""))
                .statusCode(400)
                .body("message", Matchers.equalTo("Недостаточно данных для входа"));
    }
}