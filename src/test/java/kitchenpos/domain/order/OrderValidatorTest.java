package kitchenpos.domain.order;

import static kitchenpos.fixture.OrderTableFactory.emptyTable;
import static kitchenpos.fixture.OrderTableFactory.notEmptyTable;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import kitchenpos.application.FakeSpringContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderValidatorTest extends FakeSpringContext {

    private final OrderValidator orderValidator = new OrderValidator(orderTables);

    @DisplayName("OrderLineItem 목록이 비었다면 예외를 던진다")
    @Test
    void validateOnCreate_orderLineItemsIsEmpty_throwsException() {
        final var table = orderTableDao.save(notEmptyTable(2));

        final List<OrderLineItem> invalidOrderLineItems = Collections.emptyList();
        final var order = new Order(
                null, table.getId(), OrderStatus.COOKING, LocalDateTime.now(), invalidOrderLineItems);

        assertThatThrownBy(
                () -> orderValidator.validateOnCreate(order)
        ).isInstanceOf(IllegalArgumentException.class);

    }

    @DisplayName("조회된 주문 테이블이 비었다면 예외를 던진다")
    @Test
    void validateOnCreate_orderTableIsEmptyTrue_throwsException() {
        final var emptyTable = orderTableDao.save(emptyTable(2));

        final List<OrderLineItem> invalidOrderLineItems = List.of(
                new OrderLineItem(1L, 1L, 1L), new OrderLineItem(1L, 2L, 1L));
        final var order = new Order(
                null, emptyTable.getId(), OrderStatus.COOKING, LocalDateTime.now(), invalidOrderLineItems);

        assertThatThrownBy(
                () -> orderValidator.validateOnCreate(order)
        ).isInstanceOf(IllegalArgumentException.class);
    }
}
