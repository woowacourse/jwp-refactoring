package kitchenpos.application;

import static kitchenpos.util.ObjectUtil.*;
import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.TableGroupRepository;

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

    @DisplayName("여러 빈 테이블들을 정상적으로 단체지정한다.")
    @Test
    void createTest() {
        final OrderTable firstOrderTable = createOrderTable(1L, null, 0, true);
        final OrderTable secondOrderTable = createOrderTable(1L, null, 0, true);
        final List<OrderTable> orderTables = Arrays.asList(firstOrderTable, secondOrderTable);
        final Queue<OrderTable> queue = new LinkedList<>(orderTables);
        final TableGroup expectedTableGroup = createTableGroup(3L, LocalDateTime.now(), orderTables);

        given(orderTableRepository.findAllByIdIn(anyList())).willReturn(expectedTableGroup.getOrderTables());
        given(tableGroupRepository.save(any(TableGroup.class))).willReturn(expectedTableGroup);
        given(orderTableRepository.save(any(OrderTable.class))).willReturn(queue.poll());

        assertThat(tableGroupService.create(createTableGroup(null, null, orderTables)))
            .usingRecursiveComparison()
            .ignoringFields("createdDateTime")
            .isEqualTo(expectedTableGroup);
    }

    @DisplayName("단체 지정할 테이블들이 null이거나 2 미만이면 예외를 반환한다.")
    @MethodSource("provideOrderTables")
    @ParameterizedTest
    void createTest2(final List<OrderTable> orderTables) {
        final TableGroup invalidTableGroup = createTableGroup(null, LocalDateTime.now(), orderTables);

        assertThatThrownBy(() -> tableGroupService.create(invalidTableGroup))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("그룹내의 주문 테이블과 등록된 주문 테이블에서 조회한 것이 다르면 예외를 반환한다.")
    @Test
    void createTest3() {
        final OrderTable firstTable = createOrderTable(1L, 3L, 0, true);
        final OrderTable secondTable = createOrderTable(2L, 3L, 0, true);
        final List<OrderTable> orderTables = Arrays.asList(firstTable, secondTable);

        given(orderTableRepository.findAllByIdIn(anyList())).willReturn(Collections.singletonList(firstTable));

        assertThatThrownBy(() -> tableGroupService.create(createTableGroup(null, null, orderTables)))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정할 테이블 중 빈 테이블이 아닌 테이블이 있으면 예외를 반환한다.")
    @Test
    void createTest4() {
        final OrderTable firstTable = createOrderTable(1L, 3L, 0, true);
        final OrderTable persistFirstTable = createOrderTable(1L, 3L, 2, false);
        final OrderTable secondTable = createOrderTable(2L, 3L, 0, true);
        final List<OrderTable> orderTables = Arrays.asList(firstTable, secondTable);

        given(orderTableRepository.findAllByIdIn(anyList())).willReturn(Arrays.asList(persistFirstTable, secondTable));

        assertThatThrownBy(() -> tableGroupService.create(createTableGroup(null, null, orderTables)))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정할 테이블 중 이미 단체지정된 테이블이 있으면 예외를 반환한다.")
    @Test
    void createTest5() {
        final OrderTable firstTable = createOrderTable(1L, 3L, 0, true);
        final OrderTable persistFirstTable = createOrderTable(1L, 4L, 2, false);
        final OrderTable secondTable = createOrderTable(2L, 3L, 0, true);
        final List<OrderTable> orderTables = Arrays.asList(firstTable, secondTable);

        given(orderTableRepository.findAllByIdIn(anyList())).willReturn(Arrays.asList(persistFirstTable, secondTable));

        assertThatThrownBy(() -> tableGroupService.create(createTableGroup(null, null, orderTables)))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("특정 테이블들을 단체 지정 해제한다.")
    @Test
    void ungroupTest() {
        final OrderTable orderTable = createOrderTable(1L, 3L, 0, true);
        final OrderTable ungroupOrderTable = createOrderTable(1L, null, 0, true);
        final List<OrderTable> orderTables = Collections.singletonList(orderTable);
        final TableGroup expectedTableGroup = createTableGroup(3L, LocalDateTime.now(), orderTables);

        given(orderTableRepository.findAllByTableGroupId(anyLong())).willReturn(expectedTableGroup.getOrderTables());
        given(orderRepository.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList())).willReturn(false);
        given(orderTableRepository.save(any(OrderTable.class))).willReturn(ungroupOrderTable);

        assertDoesNotThrow(() -> tableGroupService.ungroup(1L));
    }

    @DisplayName("계산 완료가 안된 테이블이 존재하면 예외를 반환한다.")
    @Test
    void ungroupTest2() {
        final OrderTable orderTable = createOrderTable(1L, 3L, 0, true);
        final List<OrderTable> orderTables = Collections.singletonList(orderTable);
        final TableGroup expectedTableGroup = createTableGroup(3L, LocalDateTime.now(), orderTables);

        given(orderTableRepository.findAllByTableGroupId(anyLong())).willReturn(expectedTableGroup.getOrderTables());
        given(orderRepository.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList())).willReturn(true);

        assertThatThrownBy(() -> tableGroupService.ungroup(1L))
            .isInstanceOf(IllegalArgumentException.class);
    }

    private static Stream<List<OrderTable>> provideOrderTables() {
        OrderTable orderTable = createOrderTable(1L, null, 0, true);

        return Stream.of(
            null,
            Collections.singletonList(orderTable)
        );
    }
}
