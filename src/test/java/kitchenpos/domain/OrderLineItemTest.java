package kitchenpos.domain;

import kitchenpos.domain.order.OrderLineItem;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class OrderLineItemTest {

    @DisplayName("메뉴 id가 null인 주문 내역은 생성할 수 없다")
    @Test
    void create_menuIdNull() {
        assertThatThrownBy(() -> new OrderLineItem(null, null, 1L))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("수량이 양의 정수가 아닌 주문 내역은 생성할 수 없다")
    @ParameterizedTest
    @ValueSource(longs = {0L, -1L})
    void create_menuIdNull(long invalidQuantity) {
        assertThatThrownBy(() -> new OrderLineItem(null, 1L, invalidQuantity))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
