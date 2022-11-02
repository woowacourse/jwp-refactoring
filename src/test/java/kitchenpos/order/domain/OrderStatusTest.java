package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class OrderStatusTest {

    @ParameterizedTest
    @CsvSource(value = {"COOKING:false", "COMPLETION:true"}, delimiter = ':')
    @DisplayName("완료상태 여부에 따라 t/f를 반환한다.")
    void isCompletion(String status, boolean expected) {
        assertThat(OrderStatus.isCompletion(status)).isEqualTo(expected);
    }

}
