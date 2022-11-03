package kitchenpos.domain.order;

import static kitchenpos.fixture.OrderTableFactory.emptyTable;
import static kitchenpos.fixture.OrderTableFactory.notEmptyTable;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderValidatorTest {

    private final OrderValidator orderValidator = new OrderValidator();

    @DisplayName("OrderLineItem 목록은 빈 목록이 될 수 없다")
    @Test
    void validateOnCreate_orderLineItemsIsEmpty_throwsException() {
        final List<OrderLineItem> invalidOrderLineItems = Collections.emptyList();
        final var order = new Order(null, 1L, OrderStatus.COOKING, LocalDateTime.now(), invalidOrderLineItems);
        final var table = notEmptyTable(2);

        assertThatThrownBy(
                () -> orderValidator.validateOnCreate(order, table)
        ).isInstanceOf(IllegalArgumentException.class);

    }

    @DisplayName("빈 주문 테이블로 생성할 수 없다")
    @Test
    void validateOnCreate_orderTableIsEmptyTrue_throwsException() {
        final var order = new Order(1L, List.of(new OrderLineItem(null, null, 1)));
        final var emptyTable = emptyTable(2);

        assertThatThrownBy(
                () -> orderValidator.validateOnCreate(order, emptyTable)
        ).isInstanceOf(IllegalArgumentException.class);
    }
}
