package kitchenpos.ui;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("Order 인수 테스트")
class OrderAcceptanceTest extends AcceptanceTest {

    @DisplayName("Order 생성")
    @Test
    void create() {
        Order order = order();
        Order created = makeResponse("/api/orders", TestMethod.POST, order).as(Order.class);

        assertAll(
            () -> assertThat(created.getId()).isNotNull(),
            () -> assertThat(created.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name()),
            () -> assertThat(created.getOrderLineItems()).isNotEmpty()
        );
    }

    @DisplayName("Order 생성 실패 - 주문 항목이 1개 미만이다.")
    @Test
    void create_fail_order_line_item_less_than_one() {
        Order order = order();
        order.setOrderLineItems(new ArrayList<>());
        ExtractableResponse<Response> response = makeResponse("/api/orders", TestMethod.POST,
            order);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("Order 생성 실패 - 존재하지 않는 메뉴가 있다.")
    @Test
    void create_fail_menu_non_exist() {
        Order order = order();
        order.setOrderLineItems(Collections.singletonList(new OrderLineItem(999L, 10)));
        ExtractableResponse<Response> response = makeResponse("/api/orders", TestMethod.POST,
            order);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("Order 생성 실패 - 존재하지 않는 테이블에 주문을 넣고 있다.")
    @Test
    void create_fail_table_non_exist() {
        Order order = order();
        order.setOrderTableId(999L);
        ExtractableResponse<Response> response = makeResponse("/api/orders", TestMethod.POST,
            order);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("Order 생성 실패 - 주문을 넣는 테이블이 빈 테이블이다.")
    @Test
    void create_fail_empty_table() {
        Order order = order();
        OrderTable orderTable = OrderTable.EMPTY_TABLE;
        OrderTable createdOrderTable = makeResponse("/api/tables", TestMethod.POST, orderTable)
            .as(OrderTable.class);
        order.setOrderTableId(createdOrderTable.getId());
        ExtractableResponse<Response> response = makeResponse("/api/orders", TestMethod.POST,
            order);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("Order 리스트를 불러온다.")
    @Test
    void list() {
        Order order = order();
        makeResponse("/api/orders", TestMethod.POST, order);

        List<Order> orders = makeResponse("/api/orders", TestMethod.GET).jsonPath()
            .getList(".", Order.class);

        assertThat(orders.size()).isEqualTo(1);
    }

    @DisplayName("Order의 상태 변경")
    @Test
    void changeOrderStatus() {
        Order order = makeResponse("/api/orders", TestMethod.POST,
            order()).as(Order.class);
        order.changeStatus(OrderStatus.COMPLETION);
        Order changed = makeResponse("/api/orders/" + order.getId() + "/order-status",
            TestMethod.PUT, order).as(Order.class);

        assertThat(changed.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION.name());
    }

    @DisplayName("Order 상태 변경 실패 - 주문이 존재하지 않는다.")
    @Test
    void change_order_status_fail_order_non_exist() {
        Order order = makeResponse("/api/orders", TestMethod.POST,
            order()).as(Order.class);
        order.setId(999L);

        ExtractableResponse<Response> response = makeResponse("/api/orders/" + order.getId() + "/order-status",
            TestMethod.PUT, order);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("Order 상태 변경 실패 - 주문 상태가 완료되어 있다.")
    @Test
    void change_order_status_fail_completed_status() {
        Order order = makeResponse("/api/orders", TestMethod.POST,
            order()).as(Order.class);
        order.changeStatus(OrderStatus.COMPLETION);
        makeResponse("/api/orders/" + order.getId() + "/order-status",
            TestMethod.PUT, order);

        order.changeStatus(OrderStatus.COOKING);
        ExtractableResponse<Response> response = makeResponse(
            "/api/orders/" + order.getId() + "/order-status",
            TestMethod.PUT, order);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
