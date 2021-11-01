package kitchenpos.ui;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import kitchenpos.domain.OrderStatus;
import kitchenpos.dto.request.MenuCreateRequest;
import kitchenpos.dto.request.MenuGroupRequest;
import kitchenpos.dto.request.MenuProductCreateRequest;
import kitchenpos.dto.request.OrderCreateRequest;
import kitchenpos.dto.request.OrderLineItemCreateRequest;
import kitchenpos.dto.request.OrderStatusChangeRequest;
import kitchenpos.dto.request.ProductRequest;
import kitchenpos.dto.request.TableCreateRequest;
import kitchenpos.dto.response.MenuGroupResponse;
import kitchenpos.dto.response.MenuResponse;
import kitchenpos.dto.response.OrderResponse;
import kitchenpos.dto.response.ProductResponse;
import kitchenpos.dto.response.TableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("Order 인수 테스트")
class OrderAcceptanceTest extends AcceptanceTest {

    @DisplayName("Order 생성")
    @Test
    void create() {
        OrderResponse response = createOrder();

        assertAll(
            () -> assertThat(response.getId()).isNotNull(),
            () -> assertThat(response.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name()),
            () -> assertThat(response.getOrderTableId()).isNotNull()
        );
    }

    @DisplayName("Order 생성 실패 - 주문 항목이 비어있다.")
    @Test
    void create_fail_order_line_item_less_than_one() {
        TableCreateRequest tableCreateRequest = new TableCreateRequest(1, false);
        TableResponse tableResponse = makeResponse("/api/tables", TestMethod.POST,
            tableCreateRequest)
            .as(TableResponse.class);

        OrderCreateRequest request = new OrderCreateRequest(tableResponse.getId(),
            new ArrayList<>());

        int actual = makeResponse("/api/orders", TestMethod.POST, request).statusCode();

        assertThat(actual).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("Order 생성 실패 - 존재하지 않는 메뉴가 있다.")
    @Test
    void create_fail_menu_non_exist() {
        TableCreateRequest tableCreateRequest = new TableCreateRequest(1, false);
        TableResponse tableResponse = makeResponse("/api/tables", TestMethod.POST,
            tableCreateRequest)
            .as(TableResponse.class);
        OrderLineItemCreateRequest orderLineItemCreateRequest = new OrderLineItemCreateRequest(
            999L, 2L);

        OrderCreateRequest request = new OrderCreateRequest(tableResponse.getId(),
            Collections.singletonList(orderLineItemCreateRequest));

        int actual = makeResponse("/api/orders", TestMethod.POST, request).statusCode();

        assertThat(actual).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("Order 생성 실패 - 존재하지 않는 테이블에 주문을 넣고 있다.")
    @Test
    void create_fail_table_non_exist() {
        OrderCreateRequest request = new OrderCreateRequest(999L,
            new ArrayList<>());

        int actual = makeResponse("/api/orders", TestMethod.POST, request).statusCode();

        assertThat(actual).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("Order 생성 실패 - 주문을 넣는 테이블이 빈 테이블이다.")
    @Test
    void create_fail_empty_table() {
        MenuGroupRequest menuGroupRequest = new MenuGroupRequest("menu-group");
        MenuGroupResponse menuGroupResponse = makeResponse("/api/menu-groups/", TestMethod.POST,
            menuGroupRequest)
            .as(MenuGroupResponse.class);
        ProductRequest productRequest = new ProductRequest("product", 1000L);
        ProductResponse productResponse = makeResponse("/api/products", TestMethod.POST,
            productRequest).as(
            ProductResponse.class);
        MenuProductCreateRequest menuProductCreateRequest = new MenuProductCreateRequest(
            productResponse.getId(), 10L);
        MenuCreateRequest menuCreateRequest = new MenuCreateRequest("menu",
            5000L,
            menuGroupResponse.getId(), Collections.singletonList(menuProductCreateRequest));
        MenuResponse menuResponse = makeResponse("/api/menus", TestMethod.POST, menuCreateRequest)
            .as(MenuResponse.class);
        TableCreateRequest tableCreateRequest = new TableCreateRequest(0, true);
        TableResponse tableResponse = makeResponse("/api/tables", TestMethod.POST,
            tableCreateRequest)
            .as(TableResponse.class);
        OrderLineItemCreateRequest orderLineItemCreateRequest = new OrderLineItemCreateRequest(
            menuResponse.getId(), 2L);

        OrderCreateRequest request = new OrderCreateRequest(tableResponse.getId(),
            Collections.singletonList(orderLineItemCreateRequest));

        int actual = makeResponse("/api/orders", TestMethod.POST, request).statusCode();

        assertThat(actual).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("Order 리스트를 불러온다.")
    @Test
    void list() {
        createOrder();
        createOrder();

        List<OrderResponse> orders = makeResponse("/api/orders", TestMethod.GET).jsonPath()
            .getList(".", OrderResponse.class);

        assertThat(orders.size()).isEqualTo(2);
    }

    @DisplayName("Order의 상태 변경")
    @Test
    void changeOrderStatus() {
        OrderResponse orderResponse = createOrder();

        OrderStatus meal = OrderStatus.MEAL;
        OrderStatusChangeRequest request = new OrderStatusChangeRequest(meal);

        OrderResponse response = makeResponse(
            "/api/orders/" + orderResponse.getId() + "/order-status",
            TestMethod.PUT, request).as(OrderResponse.class);

        assertThat(response.getOrderStatus()).isEqualTo(meal.name());
    }

    @DisplayName("Order 상태 변경 실패 - 주문이 존재하지 않는다.")
    @Test
    void change_order_status_fail_order_non_exist() {
        OrderStatus meal = OrderStatus.MEAL;
        OrderStatusChangeRequest request = new OrderStatusChangeRequest(meal);

        int actual = makeResponse("/api/orders/" + 999 + "/order-status",
            TestMethod.PUT, request).statusCode();

        assertThat(actual).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("Order 상태 변경 실패 - 주문 상태가 완료되어 있다.")
    @Test
    void change_order_status_fail_completed_status() {
        OrderResponse orderResponse = createOrder();
        OrderStatus completion = OrderStatus.COMPLETION;
        OrderStatusChangeRequest orderStatusChangeRequest = new OrderStatusChangeRequest(
            completion);
        makeResponse("/api/orders/" + orderResponse.getId() + "/order-status",
            TestMethod.PUT, orderStatusChangeRequest);

        OrderStatusChangeRequest request = new OrderStatusChangeRequest(OrderStatus.MEAL);

        int actual = makeResponse("/api/orders/" + orderResponse.getId() + "/order-status",
            TestMethod.PUT, request).statusCode();

        assertThat(actual).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private OrderResponse createOrder() {
        MenuGroupRequest menuGroupRequest = new MenuGroupRequest("menu-group");
        MenuGroupResponse menuGroupResponse = makeResponse("/api/menu-groups/", TestMethod.POST,
            menuGroupRequest)
            .as(MenuGroupResponse.class);
        ProductRequest productRequest = new ProductRequest("product", 1000L);
        ProductResponse productResponse = makeResponse("/api/products", TestMethod.POST,
            productRequest).as(
            ProductResponse.class);
        MenuProductCreateRequest menuProductCreateRequest = new MenuProductCreateRequest(
            productResponse.getId(), 10L);
        MenuCreateRequest menuCreateRequest = new MenuCreateRequest("menu",
            5000L,
            menuGroupResponse.getId(), Collections.singletonList(menuProductCreateRequest));
        MenuResponse menuResponse = makeResponse("/api/menus", TestMethod.POST, menuCreateRequest)
            .as(MenuResponse.class);
        TableCreateRequest tableCreateRequest = new TableCreateRequest(1, false);
        TableResponse tableResponse = makeResponse("/api/tables", TestMethod.POST,
            tableCreateRequest)
            .as(TableResponse.class);
        OrderLineItemCreateRequest orderLineItemCreateRequest = new OrderLineItemCreateRequest(
            menuResponse.getId(), 2L);

        OrderCreateRequest request = new OrderCreateRequest(tableResponse.getId(),
            Collections.singletonList(orderLineItemCreateRequest));

        return makeResponse("/api/orders", TestMethod.POST, request).as(OrderResponse.class);
    }
}
