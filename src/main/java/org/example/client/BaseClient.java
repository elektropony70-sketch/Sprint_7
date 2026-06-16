package org.example.client;

import io.restassured.RestAssured;

public class BaseClient {
    static {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }
}
