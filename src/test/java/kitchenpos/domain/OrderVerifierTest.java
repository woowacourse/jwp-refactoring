package kitchenpos.domain;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Collections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class OrderVerifierTest {
    @DisplayName("결제 완료되지 않은 테이블이 존재하면 예외 발생")
    @ParameterizedTest
    @CsvSource({"COOKING", "MEAL"})
    void validateOrderStatus_CompletionStatus_ThrownException(OrderStatus orderStatus) {
        Order order = new Order(1L, 1L, orderStatus, LocalDateTime.now());

        assertThatThrownBy(() -> OrderVerifier.validateOrderStatus(Collections.singletonList(order)))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("테이블의 주문이 아직 결제되지 않았습니다.");
    }
}