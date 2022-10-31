package kitchenpos.domain.order;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderLineItemsTest {

    @DisplayName("주문 항목은 최소 하나 있어야 합니다")
    @Test
    void constructWithEmptyOrderLineItems() {
        final List<OrderLineItem> orderLineItems = Collections.emptyList();

        assertThatThrownBy(() -> new OrderLineItems(orderLineItems))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 항목이 비어 있습니다.");
    }

    @DisplayName("주문 항목의 메뉴는 중복될 수 없습니다")
    @Test
    void constructWithDuplicatedMenu() {
        final var orderLineItems = List.of(
                new OrderLineItem(1L, 1),
                new OrderLineItem(1L, 1)
        );

        assertThatThrownBy(() -> new OrderLineItems(orderLineItems))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("중복된 메뉴의 주문 항목이 존재합니다.");
    }
}
