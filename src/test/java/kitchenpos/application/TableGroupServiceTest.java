package kitchenpos.application;

import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Orders;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.repository.OrderRepository;
import kitchenpos.domain.repository.OrderTableRepository;
import kitchenpos.domain.repository.TableGroupRepository;
import kitchenpos.ui.dto.TableGroupRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

@DisplayName("테이블그룹 서비스 테스트")
class TableGroupServiceTest extends ServiceTest {

    @InjectMocks
    private TableGroupService tableGroupService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderTableRepository orderTableRepository;

    @Mock
    private TableGroupRepository tableGroupRepository;

    private List<Long> orderTableIds;

    @BeforeEach
    void setUp() {
        orderTableIds = Arrays.asList(1L, 2L);
    }

    @DisplayName("새 테이블그룹을 생성한다. - 실패, 테이블그룹에 포함된 주문 테이블이 비어있는 경우")
    @Test
    void createFailedWhenOrderTablesEmpty() {
        // given
        TableGroupRequest tableGroupRequest = CREATE_TABLE_GROUP_REQUEST(emptyList());

        // when - then
        assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
                .isInstanceOf(IllegalArgumentException.class);
        then(orderTableRepository).should(never())
                .findAllByIdIn(anyList());
        then(tableGroupRepository).should(never())
                .save(tableGroupRequest.toTableGroup());
        then(orderTableRepository).should(never())
                .save(any(OrderTable.class));
    }

    @DisplayName("새 테이블그룹을 생성한다. - 실패, 테이블그룹에 포함된 주문 테이블이 2개 미만인 경우")
    @Test
    void createFailedWhenOrderTableLessThan2() {
        // given
        TableGroupRequest tableGroupRequest = CREATE_TABLE_GROUP_REQUEST(singletonList(1L));

        // when - then
        assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
                .isInstanceOf(IllegalArgumentException.class);
        then(orderTableRepository).should(never())
                .findAllByIdIn(anyList());
        then(tableGroupRepository).should(never())
                .save(tableGroupRequest.toTableGroup());
        then(orderTableRepository).should(never())
                .save(any(OrderTable.class));
    }

    @DisplayName("새 테이블그룹을 생성한다. - 실패, 입력으로 들어온 주문 테이블과 저장된 주문 테이블의 수가 다른 경우")
    @Test
    void createFailedWhenSizeNotEqual() {
        // given
        TableGroupRequest tableGroupRequest = CREATE_TABLE_GROUP_REQUEST(orderTableIds);

        final List<Long> orderTableIds = tableGroupRequest.orderTableIds();

        List<OrderTable> savedOrderTables = singletonList(new OrderTable(1L));
        given(orderTableRepository.findAllByIdIn(orderTableIds)).willReturn(savedOrderTables);

        // when - then
        assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
                .isInstanceOf(IllegalArgumentException.class);
        then(orderTableRepository).should(times(1))
                .findAllByIdIn(orderTableIds);
        then(tableGroupRepository).should(never())
                .save(tableGroupRequest.toTableGroup());
        then(orderTableRepository).should(never())
                .save(any(OrderTable.class));
    }

    @DisplayName("새 테이블그룹을 생성한다. - 실패, 비어있는 주문 테이블을 포함하지 않은 경우")
    @Test
    void createFailedWhenSavedOrderTableEmpty() {
        // given
        // 비어있지 않은 orderTable을 포함한다.
        OrderTable orderTable1 = new OrderTable(1L, null, 0, false);
        OrderTable orderTable2 = new OrderTable(2L, null, 0, false);

        TableGroupRequest tableGroupRequest = CREATE_TABLE_GROUP_REQUEST(orderTableIds);

        final List<Long> orderTableIds = tableGroupRequest.orderTableIds();

        List<OrderTable> savedOrderTables = Arrays.asList(orderTable1, orderTable2);
        given(orderTableRepository.findAllByIdIn(orderTableIds)).willReturn(savedOrderTables);

        // when - then
        assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
                .isInstanceOf(IllegalArgumentException.class);
        then(orderTableRepository).should(times(1))
                .findAllByIdIn(orderTableIds);
        then(tableGroupRepository).should(never())
                .save(tableGroupRequest.toTableGroup());
        then(orderTableRepository).should(never())
                .save(any(OrderTable.class));
    }

    @DisplayName("새 테이블그룹을 생성한다. - 실패, 주문 테이블에 포함된 테이블그룹 Id가 null이 아닌 경우")
    @Test
    void createFailedWhenTableGroupIdNull() {
        // given
        OrderTable orderTable1
                = new OrderTable(1L, new TableGroup(1L, LocalDateTime.MIN), 0, false);
        OrderTable orderTable2
                = new OrderTable(2L, new TableGroup(1L, LocalDateTime.MIN), 0, false);
        List<OrderTable> savedOrderTables = Arrays.asList(orderTable1, orderTable2);

        TableGroupRequest tableGroupRequest = CREATE_TABLE_GROUP_REQUEST(orderTableIds);
        final List<Long> orderTableIds = tableGroupRequest.orderTableIds();

        given(orderTableRepository.findAllByIdIn(orderTableIds)).willReturn(savedOrderTables);

        // when - then
        assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
                .isInstanceOf(IllegalArgumentException.class);
        then(orderTableRepository).should(times(1))
                .findAllByIdIn(orderTableIds);
        then(tableGroupRepository).should(never())
                .save(tableGroupRequest.toTableGroup());
        then(orderTableRepository).should(never())
                .save(any(OrderTable.class));
    }

    @DisplayName("테이블 그룹을 해제한다. - 실패, 계산완료되지 않은 테이블을 포함한 경우")
    @Test
    void ungroupFailedWhenOrderStatusNotSatisfied() {
        // given
        long tableGroupId = 1L;
        OrderTable orderTable1 = new OrderTable(1L, new TableGroup(1L, LocalDateTime.MIN), 10, false);

        // meal 상태의 주문을 포함한다.
        Orders orders = new Orders(orderTable1, OrderStatus.MEAL.name());
        OrderTable orderTable2 = new OrderTable(2L, new TableGroup(2L, LocalDateTime.MIN), 10, false);
        List<OrderTable> orderTables = Arrays.asList(orderTable1, orderTable2);

        given(orderTableRepository.findAllByTableGroupId(tableGroupId)).willReturn(orderTables);

        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

//        given(orderRepository.existsByOrderTableIdInAndOrderStatusIn(orderTableIds,
//                Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))
//        ).willThrow(IllegalArgumentException.class);

        // when - then
//        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroupId))
//                .isInstanceOf(IllegalArgumentException.class);
//        then(orderTableRepository).should(times(1))
//                .findAllByTableGroupId(tableGroupId);
//        then(orderRepository).should(times(1))
//                .existsByOrderTableIdInAndOrderStatusIn(orderTableIds,
//                        Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()));
//        then(orderTableRepository).should(never())
//                .save(any(OrderTable.class));
    }
}
