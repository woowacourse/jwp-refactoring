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
import kitchenpos.menu.application.dto.request.MenuProductRequest;
import kitchenpos.menu.application.dto.request.MenuRequest;
import kitchenpos.order.application.dto.request.OrderLineItemRequest;
import kitchenpos.order.application.dto.request.OrderRequest;
import kitchenpos.order.application.dto.response.OrderResponse;
import kitchenpos.table.application.dto.request.OrderTableRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.HttpStatus;

public class OrderAcceptanceTest extends AcceptanceTest {

    @DisplayName("주문 목록을 조회한다.")
    @Test
    void findProducts() {
        Long tableId1 = createTable(new OrderTableRequest(7, false));
        Long tableId2 = createTable(new OrderTableRequest(2, false));

        Long menuGroupId = MenuGroupAcceptanceTest.createMenuGroup("라라 메뉴");

        Long productId1 = ProductAcceptanceTest.createProduct("후라이드", 9_000);
        Long productId2 = ProductAcceptanceTest.createProduct("돼지국밥", 7_000);
        Long productId3 = ProductAcceptanceTest.createProduct("피자", 12_000);

        MenuProductRequest menuProduct1 = new MenuProductRequest(productId1, 1);
        MenuProductRequest menuProduct2 = new MenuProductRequest(productId2, 1);
        MenuProductRequest menuProduct3 = new MenuProductRequest(productId3, 1);

        Long menuId1 = createMenu(
                new MenuRequest("해장 세트", BigDecimal.valueOf(15_000), menuGroupId, List.of(menuProduct1, menuProduct2)));
        Long menuId2 = createMenu(
                new MenuRequest("아재 세트", BigDecimal.valueOf(13_000), menuGroupId, List.of(menuProduct3, menuProduct2)));

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
        List<OrderLineItemRequest> orderLineItems = menuIds.stream()
                .map(id -> new OrderLineItemRequest(id, 3))
                .collect(Collectors.toList());
        OrderRequest orderRequest = new OrderRequest(tableId, orderLineItems);

        String location = RestAssured.given().log().all()
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .body(orderRequest)
                .when().log().all()
                .post("/api/orders")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract().header("Location");

        return Long.parseLong(location.split("/api/orders/")[1]);
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
        Long tableId = createTable(new OrderTableRequest(7, false));

        Long menuGroupId = MenuGroupAcceptanceTest.createMenuGroup("라라 메뉴");

        Long productId1 = ProductAcceptanceTest.createProduct("후라이드", 9000);
        Long productId2 = ProductAcceptanceTest.createProduct("돼지국밥", 7000);

        MenuProductRequest menuProduct1 = new MenuProductRequest(productId1, 1);
        MenuProductRequest menuProduct2 = new MenuProductRequest(productId2, 1);

        Long menuId = createMenu(
                new MenuRequest("해장 세트", BigDecimal.valueOf(15_000), menuGroupId, List.of(menuProduct1, menuProduct2)));

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
