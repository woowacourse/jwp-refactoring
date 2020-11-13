package kitchenpos.acceptance;

import static io.restassured.RestAssured.*;
import static kitchenpos.ui.MenuGroupRestController.*;
import static kitchenpos.ui.ProductRestController.*;
import static kitchenpos.ui.TableRestController.*;

import java.math.BigDecimal;
import java.util.List;

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
import kitchenpos.domain.OrderTable;
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

    protected <T> T create(final String uri, final T data, final Class<T> cls)
            throws JsonProcessingException {
        final String request = objectMapper.writeValueAsString(data);

        // @formatter:off
        return
                given()
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body(request)
                .when()
                        .post(uri)
                .then()
                        .log().all()
                        .statusCode(HttpStatus.CREATED.value())
                        .extract().as(cls);
        // @formatter:on
    }

    protected <T> List<T> list(final String uri, final Class<T> cls) {
        // @formatter:off
        return
                given()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                        .get(uri)
                .then()
                        .log().all()
                        .statusCode(HttpStatus.OK.value())
                        .extract()
                        .jsonPath().getList(".", cls);
        // @formatter:on
    }

    protected Product createProduct(final String name, final String price)
            throws JsonProcessingException {
        final Product product = new Product();
        product.setName(name);
        product.setPrice(new BigDecimal(price));

        return create(PRODUCT_REST_API_URI, product, Product.class);
    }

    protected MenuGroup createMenuGroup(final String name) throws JsonProcessingException {
        final MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName(name);

        return create(MENU_GROUP_REST_API_URI, menuGroup, MenuGroup.class);
    }

    protected OrderTable createOrderTable(final int numberOfGuests, final boolean empty)
            throws JsonProcessingException {
        final OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(numberOfGuests);
        orderTable.setEmpty(empty);

        return create(TABLE_REST_API_URI, orderTable, OrderTable.class);
    }
}
