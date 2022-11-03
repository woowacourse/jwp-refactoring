package kitchenpos.domain;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderTest {

    @DisplayName("orderLineItems의 사이즈와 menu의 개수가 다르면 예외를 던진다.")
    @Test
    void createOrderByNotEqualSize() {
        assertThatThrownBy(() -> Order.from(1L, List.of(new OrderLineItem(1L, 1L, "",
                BigDecimal.ONE)), 2))
                .isInstanceOf(IllegalArgumentException.class);
    }
}