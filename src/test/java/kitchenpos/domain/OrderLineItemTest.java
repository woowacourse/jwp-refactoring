package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.exception.OrderLineItemException.InvalidMenuException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderLineItemTest {


    @Test
    @DisplayName("OrderLineItem을 생성할 때 메뉴 번호가 null이면 예외가 발생한다.")
    void init_fail1() {
        assertThatThrownBy(() -> OrderLineItem.of(Order.of(new OrderTable(10)), null, 10))
                .isInstanceOf(InvalidMenuException.class);
    }

    @Test
    @DisplayName("OrderLineItem을 생성할 때 메뉴 번호가 1보다 작으면 예외가 발생한다.")
    void init_fail2() {
        assertThatThrownBy(() -> OrderLineItem.of(Order.of(new OrderTable(10)), 0L, 10))
                .isInstanceOf(InvalidMenuException.class);
    }
}
