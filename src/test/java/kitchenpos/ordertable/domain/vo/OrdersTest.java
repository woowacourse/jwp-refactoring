package kitchenpos.ordertable.domain.vo;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrdersTest {

    @Test
    @DisplayName("주문 상태가 요리 혹은 식사중인 주문이 있는 지 확인한다.")
    void hasCookingOrMealOrders() {
        // given
        final Orders orders = new Orders();

        // when
        final boolean hasCookingOrMealOrders = orders.hasCookingOrMealOrders();

        // then
        assertThat(hasCookingOrMealOrders).isFalse();
    }
}
