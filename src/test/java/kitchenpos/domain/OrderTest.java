package kitchenpos.domain;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.domain.enums.OrderStatus;

class OrderTest {

    @DisplayName("주문항목이 비어있으면 예외처")
    @Test
    void validateByOrderLineItemsWithZero() {
        final OrderTable orderTable = new OrderTable(4, true);
        final MenuGroup menuGroup = new MenuGroup("치킨");
        final Menu menu = new Menu("후라이드+후라이", BigDecimal.valueOf(16000), menuGroup, new ArrayList<>());
        final Order order = new Order(orderTable, OrderStatus.COOKING,
                Collections.singletonList(new OrderLineItem(null, menu, 2L)));

        assertThatThrownBy(() -> order.setOrderLineItems(new ArrayList<>()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("주문항목이 비어있으면 안됩니다.");
    }
}
