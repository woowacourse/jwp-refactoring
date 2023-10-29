package kitchenpos.application;

import kitchenpos.OrderFixtures;
import kitchenpos.OrderTableFixtures;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.repository.OrderRepository;
import kitchenpos.domain.repository.OrderTableRepository;
import kitchenpos.dto.OrderTableUpdateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class OrderTableEventListenerTest {


    @Autowired
    private OrderTableService orderTableService;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderTableRepository orderTableRepository;

    @Test
    @DisplayName("주문 테이블의 빈 테이블 여부가 변경될 때 주문 상태가 COOKING, MEAL이면 예외가 발생한다")
    void validateOrderStatus() {
        //given
        final OrderTable orderTable = orderTableRepository.save(OrderTableFixtures.createWithNotEmpty());
        orderRepository.save(OrderFixtures.create(orderTable.getId(), 1L));

        final OrderTableUpdateRequest request = new OrderTableUpdateRequest(4, true);

        //when, then
        assertThatThrownBy(() -> orderTableService.changeEmpty(orderTable.getId(), request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 테이블이 조리 중이거나 식사 중입니다.");
    }
}
