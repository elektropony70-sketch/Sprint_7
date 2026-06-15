package org.example;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.example.client.OrderClient;
import org.example.model.Order;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import java.time.LocalDate;
import java.util.List;

@RunWith(Parameterized.class)
public class CreateOrderTest {

    private OrderClient orderClient;
    private final List<String> colors;

    public CreateOrderTest(List<String> colors) {
        this.colors = colors;
    }

    @Parameterized.Parameters(name = "Комбинация цветов: {0}")
    public static Object[][] getColorData() {
        return new Object[][]{
                {List.of("BLACK")},
                {List.of("GREY")},
                {List.of("BLACK", "GREY")},
                {List.of()} // Без выбора цвета
        };
    }

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
        orderClient = new OrderClient();
    }

    @Test
    @DisplayName("Создание заказа с разными вариантами цветов")
    @Description("Проверяем, что при любых комбинациях цветов заказ успешно создается и возвращается track id")
    public void shouldCreateOrderWithDifferentColors() {
        String deliveryDate = LocalDate.now().plusDays(1).toString();

        Order order = new Order(
                "Naruto",
                "Uchiha",
                "Konoha, 14",
                "4",
                "+7 999 111 22 33",
                5,
                deliveryDate, // Передаем актуальную дату
                "Жду у ворот",
                colors
        );

        orderClient.createOrder(order)
                .statusCode(201)
                .body("track", Matchers.notNullValue());
    }
}