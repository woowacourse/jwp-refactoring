package kitchenpos.order.domain;

import static kitchenpos.common.fixtures.OrderTableFixtures.ORDER_TABLE1_NUMBER_OF_GUESTS;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.order.exception.OrderException;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;
import kitchenpos.ordertable.exception.OrderTableException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class OrderValidatorTest {

    @Autowired
    private OrderValidator orderValidator;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Nested
    @DisplayName("Order 생성 검증 시")
    class ValidateCreateOrder {

        @Test
        @DisplayName("OrderTableId에 해당하는 OrderTable이 존재하지 않아서 예외가 발생한다.")
        void throws_notExistOrderTable() {
            // given
            final Long notExistOrderTableId = -1L;

            // when & then
            assertThatThrownBy(() -> orderValidator.validateCreate(notExistOrderTableId))
                    .isInstanceOf(OrderTableException.NotFoundOrderTableException.class)
                    .hasMessage("[ERROR] 해당하는 OrderTable이 존재하지 않습니다.");
        }

        @Test
        @DisplayName("OrderTableId에 해당하는 OrderTable이 비어 있으면 예외가 발생한다.")
        void throws_emptyOrderTable() {
            // given
            final OrderTable orderTable = new OrderTable(ORDER_TABLE1_NUMBER_OF_GUESTS, true);
            final OrderTable savedOrderTable = orderTableRepository.save(orderTable);

            // when & then
            assertThatThrownBy(() -> orderValidator.validateCreate(savedOrderTable.getId()))
                    .isInstanceOf(OrderException.CannotOrderStateByOrderTableEmptyException.class)
                    .hasMessage("[ERROR] 주문 테이블이 비어있는 상태일 때 주문할 수 없습니다.");
        }
    }

}
