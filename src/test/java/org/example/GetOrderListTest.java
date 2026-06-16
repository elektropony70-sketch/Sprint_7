package org.example;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.example.client.OrderClient;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

public class GetOrderListTest {
    private OrderClient orderClient;

    @Before
    public void setUp() {
        orderClient = new OrderClient();
    }

    @Test
    @DisplayName("Получение списка всех заказов")
    @Description("Проверяем, что возвращается код 200, а тело ответа содержит непустой массив заказов")
    public void shouldReturnNotEmptyOrdersList() {
        orderClient.getOrderList()
                .statusCode(200)

                .body("orders", Matchers.notNullValue())

                .body("orders.size()", Matchers.greaterThan(0));
    }
}