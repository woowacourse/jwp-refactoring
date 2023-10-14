package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class OrderStatusTest {

    @Test
    void 완료_상태를_제외한_상태를_반환한다() {
        // given
        List<OrderStatus> orderStatuses = OrderStatus.notCompletion();

        // expect
        assertThat(orderStatuses).containsExactly(OrderStatus.COOKING, OrderStatus.MEAL);
    }
}
