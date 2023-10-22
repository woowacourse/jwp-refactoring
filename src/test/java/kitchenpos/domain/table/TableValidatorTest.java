package kitchenpos.domain.table;

import kitchenpos.domain.order.OrderRepository;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.support.ServiceTest;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static kitchenpos.fixture.OrderFixture.order;
import static kitchenpos.fixture.OrderLineItemFixture.orderLineItem;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@ServiceTest
class TableValidatorTest {

    @Autowired
    private OrderTableRepository orderTableRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private TableValidator tableValidator;

    @Test
    void 주문_상태가_완료가_아니면_예외가_발생한다() {
        // given
        OrderTable orderTable = orderTableRepository.save(new OrderTable(1L, 10, false));
        orderRepository.save(order(orderTable.getId(), OrderStatus.COOKING, List.of(orderLineItem(1L, 10))));

        // expect
        assertThatThrownBy(() -> tableValidator.validate(orderTable))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 상태가 완료가 아닙니다");
    }

}
