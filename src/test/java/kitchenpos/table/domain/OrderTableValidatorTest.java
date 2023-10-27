package kitchenpos.table.domain;

import kitchenpos.config.ApplicationTestConfig;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItems;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderTableValidatorTest extends ApplicationTestConfig {

    private OrderTableValidator orderTableValidator;

    @BeforeEach
    void setUp() {
        orderTableValidator = new OrderTableValidator(orderRepository);
    }

    @DisplayName("[SUCCESS] 주문 테이블의 빈 상태 변경이 가능한지 검증한다.")
    @Test
    void success_validateChangeEmpty() {
        // given
        final OrderTable savedOrderTable = orderTableRepository.save(OrderTable.withoutTableGroup(10, true));

        // expect
        assertThatCode(() -> orderTableValidator.validateChangeEmpty(savedOrderTable))
                .doesNotThrowAnyException();
    }

    @DisplayName("[EXCEPTION] 단체 지정이 되어있는 주문 테이블의 빈 상태를 변경할 때 주문이 완료된 상태가 아닐 경우 예외가 발생한다.")
    @Test
    void throwException_validateChangeEmpty() {
        // given
        final OrderTable savedOrderTable = orderTableRepository.save(OrderTable.withoutTableGroup(10, true));

        orderRepository.save(
                new Order(
                        savedOrderTable.getId(),
                        new OrderLineItems(Collections.emptyList())
                )
        );

        // expect
        assertThatThrownBy(() -> orderTableValidator.validateChangeEmpty(savedOrderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
