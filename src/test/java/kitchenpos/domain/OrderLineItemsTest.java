package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderLineItemsTest {

    @DisplayName("주문 항목이 비어 있으면 예외가 발생한다.")
    @Test
    void create_failEmptyOrderLineItems() {
        // given
        // when
        // then
        assertThatThrownBy(() -> new OrderLineItems(List.of(), new Order()))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
