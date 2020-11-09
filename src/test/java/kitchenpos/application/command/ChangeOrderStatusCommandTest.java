package kitchenpos.application.command;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.domain.model.order.OrderStatus;

class ChangeOrderStatusCommandTest {
    @DisplayName("주문 상태 변경 요청 유효성 검사 성공")
    @Test
    void validation() {
        Arrays.stream(OrderStatus.values())
                .map(OrderStatus::name)
                .forEach(orderStatus ->
                        assertDoesNotThrow(() -> new ChangeOrderStatusCommand(orderStatus)));
    }

    @DisplayName("주문 상태 변경 요청 유효성 검사 실패")
    @Test
    void validationFails() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new ChangeOrderStatusCommand(""));
    }
}