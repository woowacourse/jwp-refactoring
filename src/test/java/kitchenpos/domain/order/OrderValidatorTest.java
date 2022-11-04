package kitchenpos.domain.order;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.domain.ordertable.OrderTable;
import kitchenpos.repository.OrderTableRepository;
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

    @DisplayName("주문 생성시 검증")
    @Nested
    class ValidateOnOrderCreated {

        @DisplayName("주문 테이블이 비어있다면, IAE를 던진다.")
        @Test
        void Should_ThrowIAE_When_OrderTableIsEmpty() {
            // given
            OrderTable savedOrderTable = orderTableRepository.save(new OrderTable(0, true));

            // when & then
            assertThatThrownBy(() -> orderValidator.validateOnOrderCreated(savedOrderTable.getId()))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
