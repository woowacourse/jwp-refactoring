package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.generator.TableGenerator;
import kitchenpos.generator.TableGroupGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class TableGroupServiceTest extends ServiceTest {

    @Mock
    private OrderDao orderDao;
    @Mock
    private OrderTableDao orderTableDao;
    @Mock
    private TableGroupDao tableGroupDao;
    @InjectMocks
    private TableGroupService tableGroupService;

    @DisplayName("단체 지정 저장")
    @Test
    void create() {
        List<OrderTable> orderTables = Arrays.asList(
            TableGenerator.newInstance(1L, null, 0, true),
            TableGenerator.newInstance(2L, null, 0, true)
        );
        when(orderTableDao.findAllByIdIn(convertIdsFromOrderTables(orderTables))).thenReturn(orderTables);
        when(tableGroupDao.save(any(TableGroup.class))).thenAnswer(invocation -> {
            TableGroup tableGroup = invocation.getArgument(0);
            return TableGroupGenerator.newInstance(1L, tableGroup.getCreatedDate());
        });

        TableGroup actual = tableGroupService.create(TableGroupGenerator.newInstance(convertIdsFromOrderTables(orderTables)));

        verify(tableGroupDao, times(1)).save(any(TableGroup.class));
        verify(orderTableDao, times(orderTables.size())).save(
            argThat(orderTable ->
                !orderTable.isEmpty() && Objects.nonNull(orderTable.getTableGroupId())
            )
        );
        assertThat(actual.getId()).isNotNull();
        assertThat(actual.getOrderTables())
            .hasSameSizeAs(orderTables)
            .usingRecursiveFieldByFieldElementComparator()
            .usingElementComparatorIgnoringFields("id", "tableGroupId", "empty")
            .hasSameElementsAs(orderTables);
        for (OrderTable orderTable : actual.getOrderTables()) {
            assertThat(orderTable.getTableGroupId()).isEqualTo(actual.getId());
            assertThat(orderTable.isEmpty()).isFalse();
        }
    }

    @DisplayName("1개 이하의 주문 테이블로 단체 지정을 등록할 경우 예외 처리")
    @Test
    void createWhenOrderTableSizeIsLessThan2() {
        List<OrderTable> orderTables = Collections.singletonList(TableGenerator.newInstance(1L, null, 0, true));

        assertThatThrownBy(() -> tableGroupService.create(TableGroupGenerator.newInstance(convertIdsFromOrderTables(orderTables))))
            .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("등록되지 않은 테이블로 단체 지정을 등록할 경우 예외 처리")
    @Test
    void createWhenSomeOrderTablesCannotBeFound() {
        List<OrderTable> orderTables = Arrays.asList(
            TableGenerator.newInstance(1L, null, 0, true),
            TableGenerator.newInstance(2L, null, 0, true)
        );
        when(orderTableDao.findAllByIdIn(convertIdsFromOrderTables(orderTables))).thenReturn(Collections.singletonList(orderTables.get(0)));

        assertThatThrownBy(() -> tableGroupService.create(TableGroupGenerator.newInstance(convertIdsFromOrderTables(orderTables))))
            .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("비어있지 않은 테이블로 단체 지정을 등록할 경우 예외 처리")
    @Test
    void createWhenSomeOrderTablesAreNotEmpty() {
        List<OrderTable> orderTables = Arrays.asList(
            TableGenerator.newInstance(1L, null, 0, true),
            TableGenerator.newInstance(2L, null, 0, false)
        );
        when(orderTableDao.findAllByIdIn(convertIdsFromOrderTables(orderTables))).thenReturn(orderTables);

        assertThatThrownBy(() -> tableGroupService.create(TableGroupGenerator.newInstance(convertIdsFromOrderTables(orderTables))))
            .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("이미 단체 지정이 된 테이블을 단체 지정으로 등록할 경우 예외 처리")
    @Test
    void createWhenSomeOrderTablesAreAlreadyDesignatedAsGroup() {
        List<OrderTable> orderTables = Arrays.asList(
            TableGenerator.newInstance(1L, null, 0, true),
            TableGenerator.newInstance(2L, 1L, 0, true)
        );
        when(orderTableDao.findAllByIdIn(convertIdsFromOrderTables(orderTables))).thenReturn(orderTables);

        assertThatThrownBy(() -> tableGroupService.create(TableGroupGenerator.newInstance(convertIdsFromOrderTables(orderTables))))
            .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정 제거")
    @Test
    void ungroup() {
        long tableGroupId = 1L;
        List<OrderTable> orderTables = Arrays.asList(
            TableGenerator.newInstance(1L, tableGroupId, 0, false),
            TableGenerator.newInstance(2L, tableGroupId, 0, false)
        );
        when(orderTableDao.findAllByTableGroupId(tableGroupId)).thenReturn(orderTables);
        when(
            orderDao.existsByOrderTableIdInAndOrderStatusIn(
                convertIdsFromOrderTables(orderTables),
                Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())
            )
        ).thenReturn(false);

        tableGroupService.ungroup(tableGroupId);

        verify(orderTableDao, times(orderTables.size())).save(
            argThat(orderTable ->
                !orderTable.isEmpty() && Objects.isNull(orderTable.getTableGroupId())
            )
        );
    }

    @DisplayName("조리나 식사 상태인 테이블이 있는 단체 지정을 제거할 경우 예외 처리")
    @Test
    void deleteWithNotFoundTableGroup() {
        long tableGroupId = 1L;
        List<OrderTable> orderTables = Arrays.asList(
            TableGenerator.newInstance(1L, tableGroupId, 0, false),
            TableGenerator.newInstance(2L, tableGroupId, 0, false)
        );
        when(orderTableDao.findAllByTableGroupId(tableGroupId)).thenReturn(orderTables);
        when(
            orderDao.existsByOrderTableIdInAndOrderStatusIn(
                convertIdsFromOrderTables(orderTables),
                Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())
            )
        ).thenReturn(true);

        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroupId));
    }

    private List<Long> convertIdsFromOrderTables(final List<OrderTable> orderTables) {
        return orderTables.stream()
            .map(OrderTable::getId)
            .collect(Collectors.toList());
    }
}
