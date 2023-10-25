package kitchenpos.domain;

import static kitchenpos.exception.ExceptionType.ALREADY_COMPLETION_ORDER;
import static kitchenpos.exception.ExceptionType.DUPLICATED_ORDER_LINE_ITEM;
import static kitchenpos.exception.ExceptionType.EMPTY_ORDER_LINE_ITEMS;
import static kitchenpos.exception.ExceptionType.EMPTY_ORDER_TABLE;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.util.List;
import kitchenpos.domain.Order.Builder;
import kitchenpos.fixture.OrderFixture;
import kitchenpos.fixture.OrderFixture.OrderLineItemFixture;
import kitchenpos.fixture.OrderTableFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTest {

    @Test
    @DisplayName("주문을 생성할 수 있다.")
    void create() {
        // given & when & then
        assertDoesNotThrow(OrderFixture.ORDER_1::toEntity);
    }

    @Test
    @DisplayName("빈 테이블에서는 주문을 생성할 수 없다.")
    void create_fail1() {
        // given
        OrderTable emptyTable = OrderTableFixture.EMPTY_TABLE1.toEntity();

        // when
        Builder builder = new Builder()
            .orderTable(emptyTable)
            .orderLineItems(List.of(OrderLineItemFixture.ORDER_LINE_ITEM_1.toEntity()));

        // then
        assertThatThrownBy(builder::build)
            .hasMessageContaining(EMPTY_ORDER_TABLE.getMessage());
    }

    @Test
    @DisplayName("주문 아이템이 0개면 주문을 생성할 수 없다.")
    void create_fail2() {
        // given
        OrderTable occupiedTable = OrderTableFixture.OCCUPIED_TABLE.toEntity();

        // when
        Builder builder = new Builder()
            .orderTable(occupiedTable)
            .orderLineItems(List.of());

        // then
        assertThatThrownBy(builder::build)
            .hasMessageContaining(EMPTY_ORDER_LINE_ITEMS.getMessage());
    }

    @Test
    @DisplayName("주문 아이템이 중복이면 주문을 생성할 수 없다.")
    void create_fail3() {
        // given
        OrderTable occupiedTable = OrderTableFixture.OCCUPIED_TABLE.toEntity();
        OrderLineItem orderLineItem = OrderLineItemFixture.ORDER_LINE_ITEM_1.toEntity();

        // when
        Builder builder = new Builder()
            .orderTable(occupiedTable)
            .orderLineItems(List.of(orderLineItem, orderLineItem));

        // then
        assertThatThrownBy(builder::build)
            .hasMessageContaining(DUPLICATED_ORDER_LINE_ITEM.getMessage());
    }

    @Test
    @DisplayName("주문 상태 변화 성공")
    void change_order_status_success() {
        // given
        OrderTable occupiedTable = OrderTableFixture.OCCUPIED_TABLE.toEntity();
        OrderLineItem orderLineItem = OrderLineItemFixture.ORDER_LINE_ITEM_1.toEntity();

        // when
        Order order = new Builder()
            .orderTable(occupiedTable)
            .orderLineItems(List.of(orderLineItem))
            .orderStatus(OrderStatus.COOKING)
            .build();

        // then
        assertDoesNotThrow(() -> order.changeOrderStatus(OrderStatus.MEAL));
    }

    @Test
    @DisplayName("주문 상태 변화 실패 - 이미 완료된 주문")
    void change_order_status_fail() {
        // given
        OrderTable occupiedTable = OrderTableFixture.OCCUPIED_TABLE.toEntity();
        OrderLineItem orderLineItem = OrderLineItemFixture.ORDER_LINE_ITEM_1.toEntity();

        // when
        Order order = new Builder()
            .orderTable(occupiedTable)
            .orderLineItems(List.of(orderLineItem))
            .orderStatus(OrderStatus.COMPLETION)
            .build();

        // then
        assertThatThrownBy(() -> order.changeOrderStatus(OrderStatus.COMPLETION))
            .hasMessageContaining(ALREADY_COMPLETION_ORDER.getMessage());
    }
}
