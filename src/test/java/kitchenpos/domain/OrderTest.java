package kitchenpos.domain;

import static kitchenpos.domain.OrderStatus.COMPLETION;
import static kitchenpos.domain.OrderStatus.COOKING;
import static kitchenpos.fixture.DomainFixture.createOrderTable;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.exception.NotConvertableStatusException;
import org.junit.jupiter.api.Test;

class OrderTest {

    @Test
    void 완료_상태면_상태를_바꿀때_예외를_반환한다() {
        Order order = new Order(createOrderTable());
        order.changeOrderStatus(COMPLETION);

        assertThatThrownBy(() -> order.changeOrderStatus(COOKING))
                .isInstanceOf(NotConvertableStatusException.class);
    }
}
