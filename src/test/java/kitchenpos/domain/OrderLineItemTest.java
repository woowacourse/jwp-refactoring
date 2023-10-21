package kitchenpos.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class OrderLineItemTest {

    @DisplayName("주문 항목 생성 시, 수량이 0개 미만이면 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(longs = {-1L, -100L})
    void orderLineItem_FailWithInvalidQuantity(Long invalidQuantity) {
        // when & then
        assertThatThrownBy(() -> OrderLineItem.create(1L, invalidQuantity))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 항목의 수량은 0개 이상이어야 합니다.");
    }

    @DisplayName("주문 항목을 생성할 수 있다.")
    @Test
    void orderLineItem() {
        // then
        assertDoesNotThrow(() -> OrderLineItem.create(1L, 1L));
    }
}
