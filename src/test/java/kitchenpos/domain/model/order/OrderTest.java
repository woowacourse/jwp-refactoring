package kitchenpos.domain.model.order;

import static java.util.Collections.*;
import static kitchenpos.fixture.RequestFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.DynamicTest.*;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

class OrderTest {
    @DisplayName("주문 생성")
    @Test
    void create() {
        Order order = new Order(1L, 1L, null, null,
                singletonList(new OrderLineItem(1L, 1L, 1L, 1L)));

        Order created = order.create();
        assertAll(
                () -> assertThat(created.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name()),
                () -> assertThat(created.getOrderedTime()).isNotNull()
        );
    }

    @DisplayName("주문 상태 변경")
    @TestFactory
    Stream<DynamicTest> changeOrderStatus() {
        return Stream.of(
                dynamicTest("주문 상태를 변경한다.", this::changeOrderStatusSuccess),
                dynamicTest("주문의 상태가 완료일 수 없다.", this::invalidOrder)
        );
    }

    private void changeOrderStatusSuccess() {
        Order order = new Order(1L, 1L, "MEAL", LocalDateTime.now(),
                singletonList(new OrderLineItem(1L, 1L, 1L, 1L)));

        order.changeOrderStatus(ORDER_STATUS_CHANGE_REQUEST2.getOrderStatus());

        assertThat(order.getOrderStatus()).isEqualTo(ORDER_STATUS_CHANGE_REQUEST2.getOrderStatus());
    }

    private void invalidOrder() {
        Order order = new Order(1L, 1L, "COMPLETION", LocalDateTime.now(),
                singletonList(new OrderLineItem(1L, 1L, 1L, 1L)));

        assertThatIllegalArgumentException().isThrownBy(
                () -> order.changeOrderStatus(ORDER_STATUS_CHANGE_REQUEST1.getOrderStatus()));
    }
}
