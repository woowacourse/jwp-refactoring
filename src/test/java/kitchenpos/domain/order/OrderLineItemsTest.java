package kitchenpos.domain.order;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import kitchenpos.domain.order.OrderLineItems;
import org.junit.jupiter.api.Test;

class OrderLineItemsTest {

    @Test
    void 주문_항목이_비어있으면_예외가_발생한다() {
        assertThatThrownBy(
                () -> new OrderLineItems(new ArrayList<>())
        ).isInstanceOf(IllegalArgumentException.class);
    }
}
