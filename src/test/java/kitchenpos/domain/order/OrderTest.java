package kitchenpos.domain.order;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Collections;
import kitchenpos.exception.OrderEmptyException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTest {

    @DisplayName("OrderLineItem이 비어있을 경우 - 실패")
    @Test
    void changeOrderLineItemsFail() {
        //given
        Order order = new Order();
        //when
        //then
        assertThatThrownBy(() -> order.changeOrderLineItems(Collections.emptyList()))
                .isInstanceOf(OrderEmptyException.class);
    }
}