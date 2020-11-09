package kitchenpos.application;

import static kitchenpos.fixture.RequestFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.application.dto.OrderResponse;
import kitchenpos.application.dto.OrderTableChangeEmptyRequest;
import kitchenpos.domain.OrderStatus;

@SpringBootTest
@Transactional
class OrderServiceTest {
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderTableService orderTableService;

    @DisplayName("주문 생성")
    @Test
    void create() {
        orderTableService.changeEmpty(ORDER_CREATE_REQUEST.getOrderTableId(), new OrderTableChangeEmptyRequest(false));
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
        OrderResponse created = orderService.create(ORDER_CREATE_REQUEST);
        List<OrderResponse> responses = orderService.list();
        OrderResponse last = responses.get(responses.size() - 1);

        assertAll(
                () -> assertThat(created.getId()).isEqualTo(last.getId()),
                () -> assertThat(created.getOrderedTime()).isEqualTo(last.getOrderedTime()),
                () -> assertThat(created.getOrderStatus()).isEqualTo(last.getOrderStatus()),
                () -> assertThat(created.getOrderLineItems().get(0).getOrderId()).isEqualTo(last.getOrderLineItems().get(0).getOrderId())
        );
    }
}
