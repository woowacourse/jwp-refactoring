package kitchenpos.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;
import java.util.Collections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTest {

    @DisplayName("주문항목이 비어있으면 예외처")
    @Test
    void validateByOrderLineItemsWithZero() {
        final Order order = new Order(1L, Collections.singletonList(new OrderLineItem(1L, 2L)));

        assertThatThrownBy(() -> order.setOrderLineItems(new ArrayList<>()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("주문항목이 비어있으면 안됩니다.");
    }
}
