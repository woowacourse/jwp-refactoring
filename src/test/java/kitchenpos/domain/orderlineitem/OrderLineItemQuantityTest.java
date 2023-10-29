package kitchenpos.domain.orderlineitem;

import kitchenpos.order.domain.OrderLineItemQuantity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderLineItemQuantityTest {
    @ParameterizedTest
    @ValueSource(longs = {0, 1})
    void 주문_항목_수량은_0_이상이다(long quantity) {
        final OrderLineItemQuantity orderLineItemQuantity = new OrderLineItemQuantity(quantity);
        assertThat(orderLineItemQuantity.getQuantity()).isEqualTo(quantity);
    }

    @Test
    void 주문_항목_수량이_음수이면_예외가_발생한다() {
        assertThatThrownBy(() -> new OrderLineItemQuantity(-1))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
