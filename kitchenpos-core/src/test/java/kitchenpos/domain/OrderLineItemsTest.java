package kitchenpos.domain;

import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.order.exception.InvalidOrderLineItemException;
import kitchenpos.product.domain.Price;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class OrderLineItemsTest {

    @Test
    void create() {
        //given
        final OrderLineItem orderLineItem = new OrderLineItem("메뉴", new Price(new BigDecimal(1000)), 1);

        //when&then
        assertDoesNotThrow(() -> new OrderLineItems(List.of(orderLineItem)));
    }

    @Test
    void validOrderLineItems() {
        //when&then
        assertThatThrownBy(() -> new OrderLineItems(List.of()))
                .isInstanceOf(InvalidOrderLineItemException.class)
                .hasMessage("주문의 OrderLineItem은 비어있을 수 없습니다.");
    }
}
