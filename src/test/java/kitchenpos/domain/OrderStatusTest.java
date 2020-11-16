package kitchenpos.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static kitchenpos.domain.OrderStatus.COOKING;
import static kitchenpos.domain.OrderStatus.MEAL;
import static org.assertj.core.api.Assertions.assertThat;

class OrderStatusTest {
    @Test
    @DisplayName("OrderTable의 상태를 변경할 때, '조리' 또는 '식사' 일 경우 변경되면 안되는 경우가 있다")
    void unmodifiable() {
        List<String> result = OrderStatus.getUnmodifiableStatus();
        assertThat(result).containsExactlyInAnyOrder(COOKING.name(), MEAL.name());
    }
}