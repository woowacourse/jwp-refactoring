package kitchenpos.acceptance;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import io.restassured.RestAssured;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductAcceptanceTest {
    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    /**
     * Feature: 제품 관리
     * <p>
     * Scenario: 제품을 관리한다.
     * <p>
     * When: 제품을 생성한다.
     * Then: 제품이 생성된다.
     * <p>
     * When: 제품의 목록을 조회한다.
     * Then: 생성된 제품의 목록이 반환된다.
     */
    @TestFactory
    void manageProduct() {
    }
}
