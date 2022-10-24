package acceptance;

import static acceptance.MenuAcceptanceTest.createMenu;
import static acceptance.MenuAcceptanceTest.givenMenuProduct;
import static acceptance.MenuGroupAcceptanceTest.createMenuGroup;
import static acceptance.ProductAcceptanceTest.createProduct;
import static acceptance.TableAcceptanceTest.createTable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import io.restassured.RestAssured;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.Application;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

@SpringBootTest(
        webEnvironment = WebEnvironment.RANDOM_PORT,
        classes = Application.class
)
public class OrderAcceptanceTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @DisplayName("주문 목록을 조회한다.")
    @Test
    void findProducts() {
        long tableId1 = createTable(7, false);
        long tableId2 = createTable(2, false);

        long menuGroupId = createMenuGroup("라라 메뉴");

        long productId1 = createProduct("후라이드", 9000);
        long productId2 = createProduct("돼지국밥", 7000);
        long productId3 = createProduct("피자", 12000);

        MenuProduct menuProduct1 = givenMenuProduct(productId1, 1);
        MenuProduct menuProduct2 = givenMenuProduct(productId2, 1);
        MenuProduct menuProduct3 = givenMenuProduct(productId3, 1);

        long menuId1 = createMenu("해장 세트", 15000, menuGroupId, List.of(menuProduct1, menuProduct2));
        long menuId2 = createMenu("아재 세트", 13000, menuGroupId, List.of(menuProduct3, menuProduct2));

        long orderId1 = createOrder(tableId1, List.of(menuId1));
        long orderId2 = createOrder(tableId2, List.of(menuId1, menuId2));

        List<Order> orders = getOrders();

        assertThat(orders).extracting(Order::getId, Order::getOrderTableId)
                .containsExactlyInAnyOrder(
                        tuple(orderId1, tableId1),
                        tuple(orderId2, tableId2)
                );
    }

    private long createOrder(long tableId, List<Long> menuIds) {
        List<OrderLineItem> orderLineItems = menuIds.stream()
                .map(id -> {
                    OrderLineItem orderLineItem = new OrderLineItem();
                    orderLineItem.setMenuId(id);
                    orderLineItem.setQuantity(3);
                    return orderLineItem;
                })
                .collect(Collectors.toList());
        Order order = new Order();
        order.setOrderTableId(tableId);
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

    private List<Order> getOrders() {
        return RestAssured.given().log().all()
                .when().log().all()
                .get("/api/orders")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract().body().jsonPath().getList(".", Order.class);
    }

    @DisplayName("주문 상태를 변경한다.")
    @ParameterizedTest
    @ValueSource(strings = {"MEAL", "COMPLETION"})
    void changeOrderStatus(String orderStatus) {
        long tableId = createTable(7, false);

        long menuGroupId = createMenuGroup("라라 메뉴");

        long productId1 = createProduct("후라이드", 9000);
        long productId2 = createProduct("돼지국밥", 7000);

        MenuProduct menuProduct1 = givenMenuProduct(productId1, 1);
        MenuProduct menuProduct2 = givenMenuProduct(productId2, 1);

        long menuId = createMenu("해장 세트", 15000, menuGroupId, List.of(menuProduct1, menuProduct2));

        long orderId = createOrder(tableId, List.of(menuId));

        Long updatedOrderId = updateOrderStatus(orderId, orderStatus);

        List<Order> orders = getOrders();
        Order order = orders.stream()
                .filter(o -> updatedOrderId.equals(o.getId()))
                .findFirst()
                .orElseThrow();

        assertThat(order.getOrderStatus()).isEqualTo(orderStatus);
    }

    private Long updateOrderStatus(Long orderId, String orderStatus) {
        Order order = new Order();
        order.setOrderStatus(orderStatus);

        return RestAssured.given().log().all()
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .body(order)
                .when().log().all()
                .put("/api/orders/" + orderId + "/order-status")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract().jsonPath().getLong("id");
    }
}
