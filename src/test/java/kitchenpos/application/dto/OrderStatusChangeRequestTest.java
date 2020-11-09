package kitchenpos.application.dto;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class OrderStatusChangeRequestTest {
    @DisplayName("주문 상태 변경 요청 유효성 검사 성공")
    @ParameterizedTest
    @ValueSource(strings = {"COOKING", "MEAL", "COMPLETION"})
    void validation(String input) {
        assertDoesNotThrow(() -> new OrderStatusChangeRequest(input));
    }

    @DisplayName("주문 상태 변경 요청 유효성 검사 실패")
    @Test
    void validationFails() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new OrderStatusChangeRequest(""));
    }
}