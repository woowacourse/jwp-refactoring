package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;

import kitchenpos.order.domain.OrderStatus;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class OrderStatusTest {

    @ParameterizedTest
    @CsvSource(value = {"COMPLETION, true", "COOKING, false", "MEAL, false"})
    void order_status가_완료됐는지_확인한다(OrderStatus orderStatus, boolean completed) {
        assertThat(orderStatus.isCompleted()).isEqualTo(completed);
    }
}
