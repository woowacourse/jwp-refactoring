package kitchenpos.acceptance;

import static kitchenpos.acceptance.MenuAcceptanceTest.createMenu;
import static kitchenpos.acceptance.TableAcceptanceTest.createTable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import io.restassured.RestAssured;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.HttpStatus;

public class OrderAcceptanceTest extends AcceptanceTest {

    @DisplayName("주문 목록을 조회한다.")
    @Test
    void findProducts() {
        long tableId1 = createTable(new OrderTable(null, 7, false));
        long tableId2 = createTable(new OrderTable(null, 2, false));

        long menuGroupId = MenuGroupAcceptanceTest.createMenuGroup("라라 메뉴");

        long productId1 = ProductAcceptanceTest.createProduct("후라이드", 9_000);
        long productId2 = ProductAcceptanceTest.createProduct("돼지국밥", 7_000);
        long productId3 = ProductAcceptanceTest.createProduct("피자", 12_000);

        MenuProduct menuProduct1 = new MenuProduct(productId1, 1);
        MenuProduct menuProduct2 = new MenuProduct(productId2, 1);
        MenuProduct menuProduct3 = new MenuProduct(productId3, 1);

        long menuId1 = createMenu(
                new Menu("해장 세트", BigDecimal.valueOf(15_000), menuGroupId, List.of(menuProduct1, menuProduct2)));
        long menuId2 = createMenu(
                new Menu("아재 세트", BigDecimal.valueOf(13_000), menuGroupId, List.of(menuProduct3, menuProduct2)));

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
                .map(id -> new OrderLineItem(null, id, 3))
                .collect(Collectors.toList());
        Order order = new Order(tableId, null, null, orderLineItems);

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
        long tableId = createTable(new OrderTable(null, 7, false));

        long menuGroupId = MenuGroupAcceptanceTest.createMenuGroup("라라 메뉴");

        long productId1 = ProductAcceptanceTest.createProduct("후라이드", 9000);
        long productId2 = ProductAcceptanceTest.createProduct("돼지국밥", 7000);

        MenuProduct menuProduct1 = new MenuProduct(productId1, 1);
        MenuProduct menuProduct2 = new MenuProduct(productId2, 1);

        long menuId = createMenu(
                new Menu("해장 세트", BigDecimal.valueOf(15_000), menuGroupId, List.of(menuProduct1, menuProduct2)));

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
        return RestAssured.given().log().all()
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .body(Map.of("orderStatus", orderStatus))
                .when().log().all()
                .put("/api/orders/" + orderId + "/order-status")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract().jsonPath().getLong("id");
    }
}
