package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {
    private TableGroupService tableGroupService;

    @Mock
    private TableGroupDao tableGroupDao;

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    private OrderTable orderTable1;
    private OrderTable orderTable2;

    private static Stream<Arguments> generateInvalidTable() {
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(false);
        orderTable.setNumberOfGuests(10);
        return Stream.of(
                Arguments.of(Arrays.asList()),
                Arguments.of(Arrays.asList(orderTable))
        );
    }

    @BeforeEach
    @Test
    void setUp() {
        this.tableGroupService = new TableGroupService(orderDao, orderTableDao, tableGroupDao);
        this.orderTable1 = new OrderTable();
        orderTable1.setEmpty(true);
        orderTable1.setId(1L);
        orderTable1.setNumberOfGuests(10);

        this.orderTable2 = new OrderTable();
        orderTable2.setEmpty(true);
        orderTable2.setId(2L);
        orderTable2.setNumberOfGuests(10);
    }

    @DisplayName("Table Group을 생성하고 DB에 저장한다.")
    @Test
    void createTest() {
        List<OrderTable> orderTables = Arrays.asList(orderTable1, orderTable2);

        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(orderTables);

        when(orderTableDao.findAllByIdIn(Arrays.asList(orderTable1.getId(), orderTable2.getId())))
                .thenReturn(orderTables);
        when(tableGroupDao.save(tableGroup)).thenReturn(tableGroup);

        TableGroup result = tableGroupService.create(tableGroup);

        assertThat(result.getOrderTables()).isEqualTo(orderTables);
        assertThat(result.getCreatedDate()).isNotNull();
    }

    @DisplayName("Table Group 생성 시, Table의 개수가 2개 미만이면 예외가 발생한다.")
    @ParameterizedTest
    @MethodSource("generateInvalidTable")
    void invalidTableSizeExceptionTest(List orderTables) {
        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(orderTables);

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("Table Group 생성 시, Table은 저장된 상태여야 한다.")
    @Test
    void notSavedOrderTableExceptionTest() {
        orderTable1.setId(null);

        when(orderTableDao.findAllByIdIn(Arrays.asList(null, orderTable2.getId()))).thenReturn(Arrays.asList(orderTable2));

        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(Arrays.asList(orderTable1, orderTable2));

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("Table Group 생성 시, Table이 empty가 아니라면 예외가 발생한다.")
    @Test
    void tableEmptyExceptionTest() {
        orderTable1.setEmpty(false);

        List<OrderTable> orderTables = Arrays.asList(orderTable1, orderTable2);
        when(orderTableDao.findAllByIdIn(Arrays.asList(orderTable1.getId(), orderTable2.getId())))
                .thenReturn(orderTables);

        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(orderTables);

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("Table Group 생성 시, 다른 그룹 id에 속한 Table이라면 예외가 발생한다.")
    @Test
    void tableAlreadyRegisteredExceptionTest() {
        orderTable1.setTableGroupId(1L);

        List<OrderTable> orderTables = Arrays.asList(orderTable1, orderTable2);
        List<Long> orderTableIds = Arrays.asList(orderTable1.getId(), orderTable2.getId());
        when(orderTableDao.findAllByIdIn(orderTableIds)).thenReturn(orderTables);

        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(orderTables);

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("TableGroup을 분리한다.")
    @Test
    void ungroupTest() {
        final long TABLE_GROUP_ID = 1L;

        // given
        List<OrderTable> orderTables = Arrays.asList(orderTable1, orderTable2);
        when(orderTableDao.findAllByTableGroupId(TABLE_GROUP_ID)).thenReturn(orderTables);

        // when
        tableGroupService.ungroup(TABLE_GROUP_ID);

        // then
        assertThat(orderTables.get(0).getTableGroupId()).isNull();
        assertThat(orderTables.get(0).isEmpty()).isFalse();
        assertThat(orderTables.get(1).getTableGroupId()).isNull();
        assertThat(orderTables.get(1).isEmpty()).isFalse();
    }

    @DisplayName("TableGroup 분리 시, 주문이 이미 요리/식사 중이면 예외가 발생한다.")
    @Test
    void invalidStateExceptionTest() {
        final long TABLE_GROUP_ID = 1L;

        List<OrderTable> orderTables = Arrays.asList(orderTable1, orderTable2);
        when(orderTableDao.findAllByTableGroupId(TABLE_GROUP_ID)).thenReturn(orderTables);
        when(orderDao.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList())).thenReturn(true);

        assertThatThrownBy(() -> tableGroupService.ungroup(TABLE_GROUP_ID))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
