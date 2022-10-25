package acceptance;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import io.restassured.RestAssured;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import kitchenpos.Application;
import kitchenpos.common.DataClearExtension;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

@SpringBootTest(
        webEnvironment = WebEnvironment.RANDOM_PORT,
        classes = {Application.class, DataClearExtension.class}
)
@ExtendWith(DataClearExtension.class)
public class AcceptanceTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    protected long 상품을_생성한다(final String name, final int price) {
        Product product = new Product();
        product.setName(name);
        product.setPrice(BigDecimal.valueOf(price));

        return RestAssured.given().log().all()
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .body(product)
                .when().log().all()
                .post("/api/products")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract().jsonPath().getLong("id");
    }

    protected List<Product> 상품을_조회한다() {
        return RestAssured.given().log().all()
                .when().log().all()
                .get("/api/products")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract().body().jsonPath().getList(".", Product.class);
    }

    protected long 메뉴_그룹을_생성한다(String name) {
        return RestAssured.given().log().all()
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .body(Map.of("name", name))
                .when().log().all()
                .post("/api/menu-groups")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract().body().jsonPath().getLong("id");
    }


    protected List<MenuGroup> 메뉴_그룹을_조회한다() {
        return RestAssured.given().log().all()
                .when().log().all()
                .get("/api/menu-groups")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract().body().jsonPath().getList(".", MenuGroup.class);
    }

    protected long 메뉴를_생성한다(String name, int price, long menuGroup, List<Long> products, int quantity) {
        List<MenuProduct> menuProducts = products.stream()
                .map(product -> {
                    MenuProduct menuProduct = new MenuProduct();
                    menuProduct.setProductId(product);
                    menuProduct.setQuantity(quantity);
                    return menuProduct;
                })
                .collect(Collectors.toList());

        Menu menu = new Menu();
        menu.setName(name);
        menu.setPrice(BigDecimal.valueOf(price));
        menu.setMenuGroupId(menuGroup);
        menu.setMenuProducts(menuProducts);

        return RestAssured.given().log().all()
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .body(menu)
                .when().log().all()
                .post("/api/menus")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract().body().jsonPath().getLong("id");
    }

    protected List<Menu> 메뉴를_조회한다() {
        return RestAssured.given().log().all()
                .when().log().all()
                .get("/api/menus")
                .then().log().all()
                .extract().body().jsonPath().getList(".", Menu.class);
    }

    protected long 테이블을_생성한다(int numberOfGuests, boolean empty) {
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(numberOfGuests);
        orderTable.setEmpty(empty);

        return RestAssured.given().log().all()
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .body(orderTable)
                .when().log().all()
                .post("/api/tables")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract().jsonPath().getLong("id");
    }

    protected List<OrderTable> 테이블_목록을_조회한다() {
        return RestAssured.given().log().all()
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .when().log().all()
                .get("/api/tables")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract().jsonPath().getList(".", OrderTable.class);
    }

    protected OrderTable 테이블_상태를_변경한다(long orderTableId, boolean empty) {
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(empty);

        return RestAssured.given().log().all()
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .body(orderTable)
                .when().log().all()
                .put("/api/tables/{order_table_id}/empty", orderTableId)
                .then().log().all()
                .extract().as(OrderTable.class);
    }

    protected TableGroup 테이블_그룹을_생성한다(List<OrderTable> orderTables) {
        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(orderTables);

        return RestAssured.given().log().all()
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .body(tableGroup)
                .when().log().all()
                .post("/api/table-groups")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract().as(TableGroup.class);
    }

    protected void 테이블_그룹을_해제한다(Long id) {
        RestAssured.given().log().all()
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .when().log().all()
                .delete("/api/table-groups/{id}", id)
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    protected OrderTable 테이블_방문자_수를_변경한다(long orderTableId, int numberOfGuests) {
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(numberOfGuests);

        return RestAssured.given().log().all()
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .body(orderTable)
                .when().log().all()
                .put("/api/tables/{order_table_id}/number-of-guests", orderTableId)
                .then().log().all()
                .extract().as(OrderTable.class);
    }

    protected long 주문을_생성한다(long table, List<OrderLineItem> orderLineItems) {
        Order order = new Order();
        order.setOrderTableId(table);
        order.setOrderLineItems(orderLineItems);

        return RestAssured.given().log().all()
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .body(order)
                .when().log().all()
                .post("/api/orders")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract().jsonPath().getLong("id");
    }

    protected Order 주문_상태를_변경한다(long 주문, String status) {
        Order order = new Order();
        order.setOrderStatus(status);

        return RestAssured.given().log().all()
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .body(order)
                .when().log().all()
                .put("/api/orders/{order_id}/order-status", 주문)
                .then().log().all()
                .extract().as(Order.class);
    }

    protected List<Order> 주문_목목을_조회한다() {
        return RestAssured.given().log().all()
                .when().log().all()
                .get("/api/orders")
                .then().log().all()
                .extract().jsonPath().getList(".", Order.class);
    }
}
