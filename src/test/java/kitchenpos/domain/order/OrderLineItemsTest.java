package kitchenpos.domain.order;

import kitchenpos.domain.DomainTest;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderLineItemsTest extends DomainTest {
    @Test
    void throw_when_orderLineItems_is_emtpy() {
        // when & then
        assertThatThrownBy(() -> OrderLineItems.of(List.of()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(OrderLineItems.ORDER_LINE_ITEMS_IS_EMPTY_ERROR_MESSAGE);
    }
}