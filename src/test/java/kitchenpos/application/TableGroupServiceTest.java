package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

import java.util.Arrays;
import java.util.List;
import kitchenpos.dao.OrderRepository;
import kitchenpos.dao.OrderTableRepository;
import kitchenpos.dao.TableGroupRepository;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderTableRepository orderTableRepository;

    @Mock
    private TableGroupRepository tableGroupRepository;

    @InjectMocks
    private TableGroupService tableGroupService;

    @DisplayName("새로운 단체를 등록할 수 있다.")
    @Test
    void create() {
        // given
        final TableGroup tableGroup = new TableGroup(1L);

        final OrderTable orderTable1 = new OrderTable(10L, null, 2, true);
        final OrderTable orderTable2 = new OrderTable(11L, null, 3, true);
        tableGroup.addOrderTable(orderTable1);
        tableGroup.addOrderTable(orderTable2);

        given(orderTableRepository.findAllByIdIn(List.of(orderTable1.getId(), orderTable2.getId())))
                .willReturn(List.of(orderTable1, orderTable2));

        given(tableGroupRepository.save(any(TableGroup.class)))
                .willReturn(tableGroup);

        // when & then
        assertThat(tableGroupService.create(tableGroup)).isEqualTo(tableGroup);

        then(orderTableRepository).should(times(1)).findAllByIdIn(anyList());
        then(tableGroupRepository).should(times(1)).save(any());
        then(orderTableRepository).should(times(2)).save(any());
    }

    @DisplayName("주문 테이블이 존재하지 않으면 등록할 수 없다.")
    @Test
    void create_FailWhenTableIsNull() {
        // given
        final TableGroup tableGroup = new TableGroup(1L);

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("테이블의 수가 올바르지 않습니다.");
    }

    @DisplayName("주문 테이블의 개수가 2개 미만이면 등록할 수 없다.")
    @Test
    void create_FailWhenTableIsUnderTwo() {
        // given
        final TableGroup tableGroup = new TableGroup(1L);
        final OrderTable orderTable1 = new OrderTable(10L, tableGroup, 2, true);
        tableGroup.addOrderTable(orderTable1);

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("테이블의 수가 올바르지 않습니다.");
    }

    @DisplayName("존재하지 않는 주문 테이블이 포함되어 있으면 등록할 수 없다.")
    @Test
    void create_FailWhenTableSizeUnMatch() {
        // given
        final TableGroup tableGroup = new TableGroup(1L);
        final OrderTable orderTable1 = new OrderTable(10L, tableGroup, 2, false);
        final OrderTable orderTable2 = new OrderTable(11L, tableGroup, 3, true);
        tableGroup.addOrderTable(orderTable1);
        tableGroup.addOrderTable(orderTable2);

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("테이블의 수가 일치하지 않습니다.");
    }

    @DisplayName("테이블이 비어있지 있으면 등록할 수 없다.")
    @Test
    void create_FailWhenTableIsNotEmpty() {
        // given
        final TableGroup tableGroup = new TableGroup(1L);
        final OrderTable orderTable1 = new OrderTable(10L, tableGroup, 2, false);
        final OrderTable orderTable2 = new OrderTable(11L, tableGroup, 3, true);
        tableGroup.addOrderTable(orderTable1);
        tableGroup.addOrderTable(orderTable2);

        given(orderTableRepository.findAllByIdIn(List.of(orderTable1.getId(), orderTable2.getId())))
                .willReturn(tableGroup.getOrderTables());

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("테이블이 비어있지 않거나 이미 할당되어 있습니다.");
    }

    @DisplayName("특정 단체를 삭제할 수 있다.")
    @Test
    void ungroup() {
        // given
        final TableGroup tableGroup = new TableGroup(1L);

        final OrderTable orderTable1 = new OrderTable(10L, tableGroup, 3, true);
        final OrderTable orderTable2 = new OrderTable(11L, tableGroup, 3, true);

        final List<OrderTable> orderTables = List.of(orderTable1, orderTable2);

        given(orderTableRepository.findAllByTableGroupId(tableGroup.getId()))
                .willReturn(orderTables);

        given(orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                List.of(orderTable1.getId(), orderTable2.getId()),
                Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())
        )).willReturn(false);

        given(orderTableRepository.save(orderTable1))
                .willReturn(orderTable1);
        given(orderTableRepository.save(orderTable2))
                .willReturn(orderTable2);

        // when
        tableGroupService.ungroup(1L);

        // then
        then(orderTableRepository).should(times(1)).findAllByTableGroupId(1L);
        then(orderRepository).should(times(1)).existsByOrderTableIdInAndOrderStatusIn(anyList(), any());
        then(orderTableRepository).should(times(2)).save(any());
    }

    @DisplayName("단체가 가진 주문 테이블의 상태가 조리 또는 식사이면 삭제할 수 없다.")
    @Test
    void ungroup_FailWhenTableStatusNotCompletion() {
        // given
        final TableGroup tableGroup = new TableGroup(1L);

        final OrderTable orderTable1 = new OrderTable(10L, tableGroup, 2, true);
        final OrderTable orderTable2 = new OrderTable(11L, tableGroup, 3, true);

        final List<OrderTable> orderTables = List.of(orderTable1, orderTable2);

        given(orderTableRepository.findAllByTableGroupId(1L))
                .willReturn(orderTables);

        given(orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                List.of(orderTable1.getId(), orderTable2.getId()),
                Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())
        )).willReturn(true);
        // when

        // then
        assertThatThrownBy(() -> tableGroupService.ungroup(1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 상태가 완료되지 않았습니다.");
    }
}
