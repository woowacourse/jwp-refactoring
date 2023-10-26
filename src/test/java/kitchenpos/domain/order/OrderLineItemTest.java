package kitchenpos.domain.order;

import kitchenpos.domain.Money;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class OrderLineItemTest {

    @Test
    void id가_같으면_동등하다() {
        //given
        OrderLineItem 주문항목 = new OrderLineItem(1L, 1L, "메뉴1", Money.of(20_000), 1);
        OrderLineItem id만_같은_주문항목 = new OrderLineItem(1L, 2L, "메뉴1", Money.of(20_000), 1);

        //when
        boolean actual = 주문항목.equals(id만_같은_주문항목);

        //then
        assertThat(actual).isTrue();
    }

}
