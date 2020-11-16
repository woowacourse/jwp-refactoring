package kitchenpos.application;

import static kitchenpos.fixture.RequestFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.DynamicTest.*;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import kitchenpos.application.command.ChangeOrderTableEmptyCommand;
import kitchenpos.application.response.OrderResponse;
import kitchenpos.domain.model.order.CreateOrderVerifier;
import kitchenpos.domain.model.order.OrderStatus;
import kitchenpos.domain.model.ordertable.ChangeOrderTableEmptyVerifier;

@Import({OrderService.class, OrderTableService.class, CreateOrderVerifier.class,
        ChangeOrderTableEmptyVerifier.class})
class OrderServiceTest extends ApplicationServiceTest {
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderTableService orderTableService;

    @DisplayName("주문 생성")
    @Test
    void create() {
        orderTableService.changeEmpty(ORDER_CREATE_REQUEST.getOrderTableId(),
                new ChangeOrderTableEmptyCommand(false));
        OrderResponse response = orderService.create(ORDER_CREATE_REQUEST);

        assertAll(
                () -> assertThat(response.getId()).isNotNull(),
                () -> assertThat(response.getOrderTableId()).isNotNull(),
                () -> assertThat(response.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name()),
                () -> assertThat(response.getOrderedTime()).isNotNull(),
                () -> response.getOrderLineItems().forEach(orderLineItem ->
                        assertThat(orderLineItem.getOrderId()).isEqualTo(response.getId()))
        );
    }

    @DisplayName("주문 전체 조회")
    @Test
    void list() {
        orderTableService.changeEmpty(ORDER_CREATE_REQUEST.getOrderTableId(),
                new ChangeOrderTableEmptyCommand(false));
        OrderResponse created = orderService.create(ORDER_CREATE_REQUEST);
        List<OrderResponse> responses = orderService.list();
        OrderResponse last = responses.get(responses.size() - 1);

        assertAll(
                () -> assertThat(created.getId()).isEqualTo(last.getId()),
                () -> assertThat(created.getOrderedTime()).isEqualTo(last.getOrderedTime()),
                () -> assertThat(created.getOrderStatus()).isEqualTo(last.getOrderStatus()),
                () -> assertThat(created.getOrderLineItems().get(0).getOrderId()).isEqualTo(
                        last.getOrderLineItems().get(0).getOrderId())
        );
    }

    @DisplayName("주문 상태 변경")
    @TestFactory
    Stream<DynamicTest> changeOrderStatus() {
        return Stream.of(
                dynamicTest("주문 상태 변경 성공.", this::changeOrderStatusSuccess),
                dynamicTest("주문이 존재해야 한다.", this::noOrder)
        );
    }

    private void changeOrderStatusSuccess() {
        orderTableService.changeEmpty(ORDER_CREATE_REQUEST.getOrderTableId(),
                new ChangeOrderTableEmptyCommand(false));
        OrderResponse created = orderService.create(ORDER_CREATE_REQUEST);
        OrderResponse changed = orderService.changeOrderStatus(created.getId(),
                ORDER_STATUS_CHANGE_REQUEST2);
        List<OrderResponse> responses = orderService.list();
        OrderResponse last = responses.get(responses.size() - 1);

        assertAll(
                () -> assertThat(changed.getId()).isEqualTo(last.getId()),
                () -> assertThat(changed.getOrderedTime()).isEqualTo(last.getOrderedTime()),
                () -> assertThat(changed.getOrderStatus()).isEqualTo(
                        ORDER_STATUS_CHANGE_REQUEST2.getOrderStatus()),
                () -> assertThat(changed.getOrderLineItems().get(0).getOrderId()).isEqualTo(
                        last.getOrderLineItems().get(0).getOrderId())
        );
    }

    private void noOrder() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderService.changeOrderStatus(0L, ORDER_STATUS_CHANGE_REQUEST2));
    }
}
