package kitchenpos.order;

import kitchenpos.Money;
import kitchenpos.order.domain.OrderLineItem;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderLineItemTest {

    @Test
    void id가_같으면_동등하다() {
        //given
        OrderLineItem 주문항목 = OrderLineItem.of(1L, 1L, "메뉴1", Money.of(20_000), 1);
        OrderLineItem id만_같은_주문항목 = OrderLineItem.of(1L, 2L, "메뉴1", Money.of(20_000), 1);

        //when
        boolean actual = 주문항목.equals(id만_같은_주문항목);

        //then
        assertThat(actual).isTrue();
    }

    @ParameterizedTest
    @ValueSource(longs = {0L, -1L})
    void 수량은_0_이하일_수_없다(long 수량) {
        //expect
        assertThatThrownBy(()-> OrderLineItem.of(1L, 1L, "메뉴1", Money.of(20_000), 수량))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("수량은 1개 이상이어야 합니다.");
    }

}
