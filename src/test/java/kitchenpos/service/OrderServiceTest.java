package kitchenpos.service;

import kitchenpos.application.OrderService;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.dto.OrderCreateRequest;
import kitchenpos.dto.OrderLineItemRequest;
import kitchenpos.dto.OrderStatusUpdateRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
@Sql({"classpath:/truncate.sql", "classpath:/set_up.sql"})
public class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Test
    void create() {
        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(4L,
                List.of(new OrderLineItemRequest(2L, 3L),
                        new OrderLineItemRequest(1L, 2L)));

        Order order = orderService.create(orderCreateRequest);

        assertAll(
                () -> assertThat(order.getOrderStatus()).isEqualTo("COOKING"),
                () -> assertThat(order.getOrderTableId()).isEqualTo(4L),
                () -> assertThat(order.getOrderLineItems()).isEqualTo(
                        List.of(new OrderLineItem(order.getId(), 1L, 2L),
                                new OrderLineItem(order.getId(), 2L, 3L)))
        );
    }

    @Test
    void create_orderLineItemEmpty() {
        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(4L, List.of());

        assertThatThrownBy(() -> orderService.create(orderCreateRequest)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void create_menuIdNotExist() {
        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(4L,
                List.of(new OrderLineItemRequest(2L, 3L),
                        new OrderLineItemRequest(100L, 2L)));

        assertThatThrownBy(() -> orderService.create(orderCreateRequest)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void create_OrderTableEmpty() {
        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(1L,
                List.of(new OrderLineItemRequest(2L, 3L),
                        new OrderLineItemRequest(1L, 2L)));

        assertThatThrownBy(() -> orderService.create(orderCreateRequest)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void list() {
        List<Order> orders = orderService.list();

        assertThat(orders.size()).isEqualTo(6);
    }

    @Test
    void changeOrderStatus() {
        OrderStatusUpdateRequest orderStatusUpdateRequest = new OrderStatusUpdateRequest("MEAL");

        Order order = orderService.changeOrderStatus(1L, orderStatusUpdateRequest);

        assertAll(
                () -> assertThat(order.getOrderStatus()).isEqualTo("MEAL"),
                () -> assertThat(order.getOrderTableId()).isEqualTo(2L),
                () -> assertThat(order.getOrderLineItems()).isEqualTo(
                        List.of(new OrderLineItem(order.getId(), 1L, 1L),
                                new OrderLineItem(order.getId(), 2L, 1L)))

        );
    }

    @Test
    void changeOrderStatus_orderNotExist() {
        OrderStatusUpdateRequest orderStatusUpdateRequest = new OrderStatusUpdateRequest("MEAL");

        assertThatThrownBy(() -> orderService.changeOrderStatus(100L, orderStatusUpdateRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void changeOrderStatus_stateComplete() {
        OrderStatusUpdateRequest orderStatusUpdateRequest = new OrderStatusUpdateRequest("MEAL");

        assertThatThrownBy(() -> orderService.changeOrderStatus(4L, orderStatusUpdateRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
