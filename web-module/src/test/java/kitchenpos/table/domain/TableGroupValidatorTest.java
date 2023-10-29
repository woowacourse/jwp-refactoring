package kitchenpos.table.domain;

import kitchenpos.config.ApplicationTestConfig;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.order.domain.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TableGroupValidatorTest extends ApplicationTestConfig {

    private TableGroupValidator tableGroupValidator;

    @BeforeEach
    void setUp() {
        tableGroupValidator = new TableGroupValidator(orderRepository, orderTableRepository);
    }

    @DisplayName("[SUCCESS] 단체 지정 해제 검증을 성공한다.")
    @Test
    void success_validateUngroup() {
        // given
        final OrderTable savedOrderTable = orderTableRepository.save(OrderTable.withoutTableGroup(10, true));
        final Order savedOrder = orderRepository.save(
                new Order(
                        savedOrderTable.getId(),
                        new OrderLineItems(List.of())
                )
        );
        savedOrder.changeOrderStatus(OrderStatus.COMPLETION);

        final TableGroup savedTableGroup = tableGroupRepository.save(
                new TableGroup(new OrderTables(List.of(
                        savedOrderTable
                )))
        );

        // expect
        assertThatCode(() -> tableGroupValidator.validateUngroup(savedTableGroup.getId()))
                .doesNotThrowAnyException();
    }

    @DisplayName("[EXCEPTION] 단체 지정 해제시 주문 목록이 모두 완료 상태가 아닌 경우 예외가 발생한다.")
    @Test
    void throwException_validateUngroup() {
        // given
        final OrderTable savedOrderTable = orderTableRepository.save(OrderTable.withoutTableGroup(10, true));
        final Order savedOrder = orderRepository.save(
                new Order(
                        savedOrderTable.getId(),
                        new OrderLineItems(List.of())
                )
        );

        final TableGroup savedTableGroup = tableGroupRepository.save(
                new TableGroup(new OrderTables(List.of(
                        savedOrderTable
                )))
        );

        // expect
        assertThatThrownBy(() -> tableGroupValidator.validateUngroup(savedTableGroup.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
