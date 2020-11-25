package kitchenpos.domain.model.tablegroup;

import static java.util.Arrays.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.DynamicTest.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.domain.model.AggregateReference;
import kitchenpos.domain.model.order.OrderRepository;
import kitchenpos.domain.model.order.OrderStatus;
import kitchenpos.domain.model.ordertable.OrderTable;
import kitchenpos.domain.model.ordertable.OrderTableRepository;

@ExtendWith(MockitoExtension.class)
class TableGroupUngroupServiceTest {
    @Mock
    private OrderTableRepository orderTableRepository;
    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private TableGroupUngroupService tableGroupUngroupService;

    @DisplayName("단체 지정 해제")
    @TestFactory
    Stream<DynamicTest> ungroup() {
        return Stream.of(
                dynamicTest("단체 지정을 해제한다.", this::ungroupSuccess),
                dynamicTest("테이블에 모든 주문이 완료 상태가 아닐때 IllegalArgumentException 발생",
                        this::invalidOrderStatus)
        );
    }

    private void ungroupSuccess() {
        OrderTable orderTable1 = new OrderTable(1L, 1L, 0, false);
        OrderTable orderTable2 = new OrderTable(2L, 1L, 0, false);
        TableGroup tableGroup = new TableGroup(1L,
                asList(new AggregateReference<>(orderTable1.getId()),
                        new AggregateReference<>(orderTable2.getId())), LocalDateTime.now());

        given(orderTableRepository.findAllByIdIn(tableGroup.orderTableIds()))
                .willReturn(asList(orderTable1, orderTable2));
        given(orderRepository.existsByOrderTableIdInAndOrderStatusIn(tableGroup.orderTableIds(),
                asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())))
                .willReturn(false);
        given(orderTableRepository.save(orderTable1)).willReturn(orderTable1);
        given(orderTableRepository.save(orderTable2)).willReturn(orderTable2);

        tableGroup.ungroup(tableGroupUngroupService);

        assertAll(
                () -> assertThat(orderTable1.getTableGroupId()).isNull(),
                () -> assertThat(orderTable2.getTableGroupId()).isNull()
        );
    }

    private void invalidOrderStatus() {
        OrderTable orderTable1 = new OrderTable(1L, 1L, 0, false);
        OrderTable orderTable2 = new OrderTable(2L, 1L, 0, false);
        TableGroup tableGroup = new TableGroup(1L,
                asList(new AggregateReference<>(orderTable1.getId()),
                        new AggregateReference<>(orderTable2.getId())), LocalDateTime.now());

        given(orderRepository.existsByOrderTableIdInAndOrderStatusIn(tableGroup.orderTableIds(),
                asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())))
                .willReturn(true);

        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroup.ungroup(tableGroupUngroupService));
    }
}