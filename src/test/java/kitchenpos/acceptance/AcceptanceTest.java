package kitchenpos.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import kitchenpos.acceptance.OrderAcceptanceTest.OrderLineItemForTest;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.Product;
import kitchenpos.domain.Table;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.MenuResponse;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class AcceptanceTest {
    @LocalServerPort
    protected int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    static RequestSpecification given() {
        return RestAssured.given().log().all();
    }

    Product createProduct(String productName, int productPrice) {
        Map<String, String> body = new HashMap<>();

        body.put("name", productName);
        body.put("price", Integer.toString(productPrice));

        Product response = sendCreateProductRequest(body);

        assertThat(response.getId()).isNotNull();
        assertThat(response.getName()).isEqualTo(productName);
        assertThat(response.getPrice().intValue()).isEqualTo(productPrice);

        return response;
    }

    private Product sendCreateProductRequest(Map<String, String> body) {
        return
            given()
                .body(body)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
            .when()
                .post("/api/products")
            .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract().as(Product.class);
    }

    MenuGroup createMenuGroup(String name) {
        Map<String, String> body = new HashMap<>();
        body.put("name", name);

        return sendMenuGroupRequest(body);
    }

    private MenuGroup sendMenuGroupRequest(Map<String, String> body) {
        return
            given()
                .body(body)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
                .post("/api/menu-groups")
            .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract().as(MenuGroup.class);
    }

    Table createTable(int numberOfGuests, boolean empty) {
        Map<String, Object> body = new HashMap<>();

        body.put("numberOfGuests", numberOfGuests);
        body.put("empty", empty);

        Table response = sendCreateTableRequest(body);

        assertThat(response.getId()).isNotNull();
        assertThat(response.getTableGroupId()).isEqualTo(null);
        assertThat(response.getNumberOfGuests()).isEqualTo(numberOfGuests);

        return response;
    }

    Table sendCreateTableRequest(Map<String, Object> body) {
        return
            given()
                .body(body)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
            .when()
                .post("/api/tables")
            .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract().as(Table.class);
    }

    MenuResponse createMenu(String menuName, List<Product> products, Long menuPrice, Long menuGroupId) {
        Map<String, Object> body = new HashMap<>();

        body.put("name", menuName);
        body.put("price", menuPrice);
        body.put("menuGroupId", menuGroupId);

        List<Map> menuProducts = makeMenuProducts(products);
        body.put("menuProducts", menuProducts);

        return sendCreateMenuRequest(body);
    }

    List<Map> makeMenuProducts(List<Product> products) {
        List<Map> menuProducts = new ArrayList<>();

        for (Product product : products) {
            Map<String, String> menuProduct = new HashMap<>();

            menuProduct.put("productId", Long.toString(product.getId()));
            menuProduct.put("quantity", "1");

            menuProducts.add(menuProduct);
        }
        return Collections.unmodifiableList(menuProducts);
    }

    private MenuResponse sendCreateMenuRequest(Map<String, Object> body) {
        return
            given()
                .body(body)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
            .when()
                .post("/api/menus")
            .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract().as(MenuResponse.class);
    }

    Table changeEmptyToFalse(Table table) {
        Map<String, Object> body = new HashMap<>();
        body.put("empty", false);

        return
            given()
                .body(body)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
            .when()
                .put("/api/tables/" + table.getId() + "/empty")
            .then()
                .statusCode(HttpStatus.OK.value())
                .extract().as(Table.class);
    }

    Table changeNumberOfGuests(Table table, int numberOfGuests) {
        Map<String, Object> body = new HashMap<>();
        body.put("numberOfGuests", numberOfGuests);

            return given()
                .body(body)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
            .when()
                .put("/api/tables/" + table.getId() + "/number-of-guests")
            .then()
                .statusCode(HttpStatus.OK.value())
                .extract().as(Table.class);
    }

    TableGroup groupTables(List<Table> tables) {
        Map<String, Object> body = new HashMap<>();

        List<Map<String, Object>> tablesForGroupingRequest = new ArrayList<>();

        for (Table table : tables) {
            Map<String, Object> tableForGroupingRequest = new HashMap<>();
            tableForGroupingRequest.put("id", table.getId());

            tablesForGroupingRequest.add(tableForGroupingRequest);
        }
        body.put("tables", tablesForGroupingRequest);

        return
            given()
                .body(body)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
            .when()
                .post("/api/table-groups")
            .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract().as(TableGroup.class);
    }

    Order requestOrder(Table table, List<OrderLineItemForTest> orderLineItems) {
        Map<String, Object> body = new HashMap<>();

        body.put("orderTableId", table.getId());

        List<Map> orderLineItemsForRequest = new ArrayList<>();

        orderLineItems.forEach(orderLineItem -> {
            Map<String, Object> orderLineItemForRequest = new HashMap<>();

            orderLineItemForRequest.put("menuId", orderLineItem.getMenuId());
            orderLineItemForRequest.put("quantity", orderLineItem.getQuantity());

            orderLineItemsForRequest.add(Collections.unmodifiableMap(orderLineItemForRequest));
        });

        body.put("orderLineItems", orderLineItemsForRequest);

        return
            given()
                .body(body)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
            .when()
                .post("/api/orders")
            .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract().as(Order.class);
    }

    Order changeOrderStatusTo(OrderStatus orderStatus, Order order) {
        Map<String, Object> body = new HashMap<>();

        body.put("orderStatus", orderStatus);

        return
            given()
                .body(body)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
            .when()
                .put("/api/orders/" + order.getId() + "/order-status")
            .then()
                .statusCode(HttpStatus.OK.value())
                .extract().as(Order.class);
    }
}
