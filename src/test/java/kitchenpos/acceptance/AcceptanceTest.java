package kitchenpos.acceptance;

import static io.restassured.RestAssured.*;
import static kitchenpos.ui.MenuGroupRestController.*;
import static kitchenpos.ui.ProductRestController.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Product;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
abstract class AcceptanceTest {
    @LocalServerPort
    private int port;

    @Autowired
    protected ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    protected MenuGroup createMenuGroup(MenuGroup menuGroup) throws JsonProcessingException {
        final String request = objectMapper.writeValueAsString(menuGroup);

        // @formatter:off
        return
                given()
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body(request)
                .when()
                        .post(MENU_GROUP_REST_API_URI)
                .then()
                        .log().all()
                        .statusCode(HttpStatus.CREATED.value())
                        .extract().as(MenuGroup.class)
                ;
        // @formatter:on
    }

    protected Product createProduct(Product product) throws JsonProcessingException {
        final String request = objectMapper.writeValueAsString(product);

        // @formatter:off
        return
                given()
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body(request)
                .when()
                        .post(PRODUCT_REST_API_URI)
                .then()
                        .log().all()
                        .statusCode(HttpStatus.CREATED.value())
                        .extract().as(Product.class)
                ;
        // @formatter:on
    }

    protected Product createSetupProduct(String name, String price) throws JsonProcessingException {
        final Product setUpProduct = new Product();
        setUpProduct.setName(name);
        setUpProduct.setPrice(new BigDecimal(price));

        return createProduct(setUpProduct);
    }

    protected MenuGroup createSetupMenuGroup(String name) throws JsonProcessingException {
        final MenuGroup setUpMenuGroup = new MenuGroup();
        setUpMenuGroup.setName(name);

        return createMenuGroup(setUpMenuGroup);
    }
}
