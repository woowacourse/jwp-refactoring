package kitchenpos.domain;

import kitchenpos.exception.orderLineItemException.InvalidOrderLineItemException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class OrderLineItemsTest {

    @Test
    void create() {
        //given
        final OrderLineItem orderLineItem = new OrderLineItem(new Menu("메뉴", null, null, null), 1);

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
