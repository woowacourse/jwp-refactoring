package kitchenpos.domain.table;

import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItems;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.order.repository.OrderRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.spy;

@ExtendWith(MockitoExtension.class)
class OrderTableValidatorTest {

    @InjectMocks
    private OrderTableValidator orderTableValidator;

    @Mock
    private OrderRepository orderRepository;

    @ParameterizedTest
    @EnumSource(value = OrderStatus.class, names = {"MEAL", "COOKING"})
    void 주문상태가_요리와_식사중이면_그룹을_해제할_수_없다(final OrderStatus orderStatus) {
        // given
        final OrderTable spyOrderTable1 = spy(new OrderTable(3, true));
        final OrderTable spyOrderTable2 = spy(new OrderTable(4, true));

        final OrderTables orderTables = OrderTables.from(List.of(spyOrderTable1, spyOrderTable2));
        given(spyOrderTable1.getId()).willReturn(1L);
        given(spyOrderTable2.getId()).willReturn(2L);

        final Long orderTableId1 = 1L;
        final Order order1 = new Order(orderTableId1, orderStatus, now(), new OrderLineItems());
        final Long orderTableId2 = 2L;
        final Order order2 = new Order(orderTableId2, orderStatus, now(), new OrderLineItems());
        given(orderRepository.findAllByOrderTableIds(anyList())).willReturn(List.of(order1, order2));

        // when, then
        assertThatThrownBy(() -> orderTableValidator.validate(orderTables))
                .isInstanceOf(IllegalArgumentException.class);

    }
}
