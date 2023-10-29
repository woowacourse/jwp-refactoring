package kitchenpos.order.domain;

import kitchenpos.config.ApplicationTestConfig;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableEmptyValidatorImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderTableEmptyValidatorTest extends ApplicationTestConfig {

    private OrderTableEmptyValidator orderTableEmptyValidator;

    @BeforeEach
    void setUp() {
        orderTableEmptyValidator = new OrderTableEmptyValidatorImpl(orderTableRepository);
    }

    @DisplayName("[SUCCESS] 주문 테이블의 빈 상태가 맞는지 검증한다.")
    @Test
    void success_validateEmpty() {
        // given
        final OrderTable savedOrderTable = orderTableRepository.save(
                OrderTable.withoutTableGroup(10, true)
        );

        // expect
        assertThatCode(() -> orderTableEmptyValidator.validateEmpty(savedOrderTable.getId()))
                .doesNotThrowAnyException();
    }

    @DisplayName("[EXCEPTION] 주문 테이블의 빈 상태가 아닌 경우 예외가 발생한다.")
    @Test
    void throwException_validateEmpty_when_notEmpty() {
        // given
        final OrderTable savedOrderTable = orderTableRepository.save(
                OrderTable.withoutTableGroup(10, false)
        );

        // expect
        assertThatThrownBy(() -> orderTableEmptyValidator.validateEmpty(savedOrderTable.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
