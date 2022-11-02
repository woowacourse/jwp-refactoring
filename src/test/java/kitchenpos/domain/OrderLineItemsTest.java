package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import org.junit.jupiter.api.Test;

class OrderLineItemsTest {

    @Test
    void 주문_항목이_비어있으면_예외가_발생한다() {
        assertThatThrownBy(
                () -> new OrderLineItems(new ArrayList<>())
        ).isInstanceOf(IllegalArgumentException.class);
    }
}
