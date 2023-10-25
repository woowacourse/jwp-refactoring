package kitchenpos.domain.order;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrderLineItemTest {

    @Test
    void id가_같으면_동등하다() {
        //given
        OrderLineItem orderLineItem = new OrderLineItem(1L, 1L, 1);

        //when
        boolean actual = orderLineItem.equals(new OrderLineItem(1L, 2L, 1));

        //then
        assertTrue(actual);
    }

}
