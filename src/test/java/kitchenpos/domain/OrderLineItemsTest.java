package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderLineItemsTest {

    @Test
    @DisplayName("주문 항목이 비어있는 경우 예외가 발생한다.")
    void emptyValues() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new OrderLineItems(Collections.emptyList()))
                .withMessage("주문 항목이 비어있습니다.");
    }

    @Test
    @DisplayName("주문 항목이 비어있는 경우 예외가 발생한다.")
    void size() {
        List<OrderLineItem> orderLineItems = List.of(new OrderLineItem(1L, 2), new OrderLineItem(2L, 2));
        OrderLineItems result = new OrderLineItems(orderLineItems);

        assertThat(result.size()).isEqualTo(2);
    }
}
