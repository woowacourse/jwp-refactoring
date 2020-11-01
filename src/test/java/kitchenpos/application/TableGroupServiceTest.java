package kitchenpos.application;

import static kitchenpos.OrderTableFixture.*;
import static kitchenpos.TableGroupFixture.*;
import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.TableGroupRepository;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {
    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderTableRepository orderTableRepository;

    @Mock
    private TableGroupRepository tableGroupRepository;
    private TableGroupService tableGroupService;

    @BeforeEach
    void setUp() {
        tableGroupService = new TableGroupService(orderRepository, orderTableRepository, tableGroupRepository);
    }

    @DisplayName("여러 빈 테이블들을 정상적으로 단체지정한다.")
    @Test
    void createTest() {
        final TableGroup expectedTableGroup = createTableGroupWithId(1L);
        given(orderTableRepository.findAllByIdIn(anyList())).willReturn(expectedTableGroup.getOrderTables());
        given(tableGroupRepository.save(any(TableGroup.class))).willReturn(expectedTableGroup);
        given(orderTableRepository.save(any(OrderTable.class))).willReturn(createOrderTableWithId(1L));

        assertThat(tableGroupService.create(createTableGroupWithId(1L)))
            .isEqualToComparingFieldByField(expectedTableGroup);
    }

    @DisplayName("단체 지정할 테이블 갯수가 2개 미만이면 예외를 반환한다.")
    @Test
    void createTest2() {
        final TableGroup tableGroupWithOneOrderTables = createTableGroupWithId(1L);
        tableGroupWithOneOrderTables.setOrderTables(Collections.singletonList(createOrderTableWithId(1L)));

        assertThatThrownBy(() -> tableGroupService.create(tableGroupWithOneOrderTables))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정할 테이블들이 없으면 예외를 반환한다.")
    @Test
    void createTest3() {
        final TableGroup tableGroupWithoutOrderTables = createTableGroupWithId(1L);
        tableGroupWithoutOrderTables.setOrderTables(null);

        assertThatThrownBy(() -> tableGroupService.create(tableGroupWithoutOrderTables))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("없는 테이블을 포함해서 단체지정을 시도하면 예외를 반환한다.")
    @Test
    void createTest4() {
        given(orderTableRepository.findAllByIdIn(anyList())).willReturn(Collections.singletonList(createOrderTableWithId(1L)));

        assertThatThrownBy(() -> tableGroupService.create(createTableGroupWithoutId()))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정할 테이블 중 빈 테이블이 아닌 테이블이 있으면 예외를 반환한다.")
    @Test
    void createTest5() {
        given(orderTableRepository.findAllByIdIn(anyList())).willReturn(createOrderTables());

        assertThatThrownBy(() -> tableGroupService.create(createTableGroupWithoutId()))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정할 테이블 중 이미 단체지정된 테이블이 있으면 예외를 반환한다.")
    @Test
    void createTest6() {
        final List<OrderTable> emptyOrderTables = createEmptyOrderTables();
        final OrderTable firstOrderTable = emptyOrderTables.get(0);
        firstOrderTable.setTableGroupId(1L);
        given(orderTableRepository.findAllByIdIn(anyList())).willReturn(emptyOrderTables);

        assertThatThrownBy(() -> tableGroupService.create(createTableGroupWithoutId()))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("특정 테이블들을 단체 지정 해제한다.")
    @Test
    void ungroupTest() {
        final TableGroup expectedTableGroup = createTableGroupWithId(1L);
        given(orderTableRepository.findAllByTableGroupId(anyLong())).willReturn(expectedTableGroup.getOrderTables());
        given(orderRepository.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList())).willReturn(false);
        given(orderTableRepository.save(any(OrderTable.class))).willReturn(createOrderTableWithId(1L));

        assertDoesNotThrow(() -> tableGroupService.ungroup(1L));
    }

    @DisplayName("계산 완료가 안된 테이블이 존재하면 예외를 반환한다.")
    @Test
    void ungroupTest2() {
        final TableGroup expectedTableGroup = createTableGroupWithId(1L);
        given(orderTableRepository.findAllByTableGroupId(anyLong())).willReturn(expectedTableGroup.getOrderTables());
        given(orderRepository.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList())).willReturn(true);

        assertThatThrownBy(() -> tableGroupService.ungroup(1L))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
