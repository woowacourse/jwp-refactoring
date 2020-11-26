package kitchenpos.order.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class OrderStatusTest {

    @DisplayName("COMPLETION 인지 확인한다.")
    @ParameterizedTest
    @CsvSource(value = {"COMPLETION,true", "MEAL,false", "COOKING,false"})
    void isCompletion(OrderStatus orderStatus, boolean expect) {
        assertThat(orderStatus.isCompletion()).isEqualTo(expect);
    }

    @DisplayName("상태에 따라 그룹을 해제 할 수 있는지 확인한다.")
    @ParameterizedTest
    @CsvSource(value = {"COMPLETION,true", "MEAL,false", "COOKING,false"})
    void canUngroup(OrderStatus orderStatus, boolean expect) {
        assertThat(orderStatus.canUngroup()).isEqualTo(expect);
    }
}