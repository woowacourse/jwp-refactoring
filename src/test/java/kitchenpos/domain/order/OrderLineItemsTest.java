package kitchenpos.domain.order;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.Collections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("OrderLineItems 도메인 테스트")
class OrderLineItemsTest {

    @DisplayName("주문 항목이 비어있으면 안된다")
    @Test
    void orderLineItemIsEmpty() {
        assertThatThrownBy(() -> new OrderLineItems(Collections.emptyList()))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("주문 항목이 존재하지 않습니다.");
    }
}
