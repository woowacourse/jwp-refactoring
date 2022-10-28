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
import kitchenpos.ui.dto.OrderResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.HttpStatus;

public class OrderAcceptanceTest extends AcceptanceTest {

    @DisplayName("주문 목록을 조회한다.")
    @Test
    void findProducts() {
        Long tableId1 = createTable(new OrderTable(null, 7, false));
        Long tableId2 = createTable(new OrderTable(null, 2, false));

        Long menuGroupId = MenuGroupAcceptanceTest.createMenuGroup("라라 메뉴");

        Long productId1 = ProductAcceptanceTest.createProduct("후라이드", 9_000);
        Long productId2 = ProductAcceptanceTest.createProduct("돼지국밥", 7_000);
        Long productId3 = ProductAcceptanceTest.createProduct("피자", 12_000);

        MenuProduct menuProduct1 = new MenuProduct(productId1, 1, BigDecimal.valueOf(9_000));
        MenuProduct menuProduct2 = new MenuProduct(productId2, 1, BigDecimal.valueOf(7_000));
        MenuProduct menuProduct3 = new MenuProduct(productId3, 1, BigDecimal.valueOf(12_000));

        Long menuId1 = createMenu(
                Menu.create("해장 세트", BigDecimal.valueOf(15_000), menuGroupId, List.of(menuProduct1, menuProduct2)));
        Long menuId2 = createMenu(
                Menu.create("아재 세트", BigDecimal.valueOf(13_000), menuGroupId, List.of(menuProduct3, menuProduct2)));

        Long orderId1 = createOrder(tableId1, List.of(menuId1));
        Long orderId2 = createOrder(tableId2, List.of(menuId1, menuId2));

        List<OrderResponse> orders = getOrders();

        assertThat(orders).extracting(OrderResponse::getId, OrderResponse::getOrderTableId)
                .containsExactlyInAnyOrder(
                        tuple(orderId1, tableId1),
                        tuple(orderId2, tableId2)
                );
    }

    private Long createOrder(long tableId, List<Long> menuIds) {
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

    private List<OrderResponse> getOrders() {
        return RestAssured.given().log().all()
                .when().log().all()
                .get("/api/orders")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract().body().jsonPath().getList(".", OrderResponse.class);
    }

    @DisplayName("주문 상태를 변경한다.")
    @ParameterizedTest
    @ValueSource(strings = {"MEAL", "COMPLETION"})
    void changeOrderStatus(String orderStatus) {
        Long tableId = createTable(new OrderTable(null, 7, false));

        Long menuGroupId = MenuGroupAcceptanceTest.createMenuGroup("라라 메뉴");

        Long productId1 = ProductAcceptanceTest.createProduct("후라이드", 9000);
        Long productId2 = ProductAcceptanceTest.createProduct("돼지국밥", 7000);

        MenuProduct menuProduct1 = new MenuProduct(productId1, 1, BigDecimal.valueOf(9000));
        MenuProduct menuProduct2 = new MenuProduct(productId2, 1, BigDecimal.valueOf(7000));

        Long menuId = createMenu(
                Menu.create("해장 세트", BigDecimal.valueOf(15_000), menuGroupId, List.of(menuProduct1, menuProduct2)));

        Long orderId = createOrder(tableId, List.of(menuId));

        updateOrderStatus(orderId, orderStatus);

        List<OrderResponse> orders = getOrders();
        OrderResponse order = orders.stream()
                .filter(o -> orderId.equals(o.getId()))
                .findFirst()
                .orElseThrow();

        assertThat(order.getOrderStatus()).isEqualTo(orderStatus);
    }

    private void updateOrderStatus(Long orderId, String orderStatus) {
        RestAssured.given().log().all()
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .body(Map.of("orderStatus", orderStatus))
                .when().log().all()
                .put("/api/orders/" + orderId + "/order-status")
                .then().log().all()
                .statusCode(HttpStatus.OK.value());
    }
}
