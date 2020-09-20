package kitchenpos.application;

import static kitchenpos.domain.DomainCreator.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {
    @Autowired
    private TableGroupService tableGroupService;
    @Mock
    private OrderDao orderDao;
    @Mock
    private OrderTableDao orderTableDao;
    @Mock
    private TableGroupDao tableGroupDao;

    private OrderTable orderTable1;
    private OrderTable orderTable2;

    @BeforeEach
    void setUp() {
        tableGroupService = new TableGroupService(orderDao, orderTableDao, tableGroupDao);

        orderTable1 = createOrderTable(true);
        orderTable2 = createOrderTable(true);
        orderTable1.setId(1L);
        orderTable2.setId(2L);
    }

    private static Stream<Arguments> generateData() {
        return Stream.of(
            Arguments.of(new ArrayList<>()),
            Arguments.of(Arrays.asList(createOrderTable(true)))
        );
    }

    @Test
    void create() {
        List<OrderTable> orderTables = Arrays.asList(orderTable1, orderTable2);
        TableGroup tableGroup = createTableGroup(orderTables);

        given(orderTableDao.findAllByIdIn(anyList())).willReturn(orderTables);
        given(tableGroupDao.save(tableGroup)).willReturn(tableGroup);
        given(orderTableDao.save(orderTables.get(0))).willReturn(orderTables.get(0));
        given(orderTableDao.save(orderTables.get(1))).willReturn(orderTables.get(1));

        TableGroup savedTableGroup = tableGroupService.create(tableGroup);
        List<OrderTable> savedOrderTables = savedTableGroup.getOrderTables();

        assertAll(
            () -> assertThat(savedTableGroup.getId()).isEqualTo(tableGroup.getId()),
            () -> assertThat(savedTableGroup.getCreatedDate()).isNotNull(),
            () -> assertThat(savedOrderTables.size()).isEqualTo(orderTables.size())
        );
    }

    @ParameterizedTest
    @MethodSource("generateData")
    @DisplayName("그룹으로 묶을 테이블 수는 2 이상이어야 한다.")
    void createErrorWhenTableNumberIsUnderTwo(List<OrderTable> tables) {
        TableGroup tableGroup = createTableGroup(tables);
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("create Error: 그룹으로 묶을 테이블은 2개 이상이어야 합니다.");
    }

    @Test
    @DisplayName("그룹으로 묶을 테이블의 상태는 empty 여야 한다.")
    void createErrorWhenTableIsNotEmpty() {
        OrderTable emptyTable = createOrderTable(true);
        OrderTable nonEmptyTable = createOrderTable(false);
        List<OrderTable> tables = Arrays.asList(emptyTable, nonEmptyTable);
        TableGroup tableGroup = createTableGroup(tables);

        given(orderTableDao.findAllByIdIn(anyList())).willReturn(tables);

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("create Error: 테이블을 그룹으로 묶을 수 없습니다. (테이블이 비어있지 않거나, 이미 그룹이 되어있는 테이블)");
    }

    @Test
    @DisplayName("그룹으로 묶을 테이블은 다른 테이블 그룹이 아니어야 한다.")
    void createErrorWhenTableGroupIsInOtherTableGroup() {
        OrderTable notGroupedTable = createOrderTable(true);
        OrderTable alreadyGroupedTable = createOrderTable(true);
        alreadyGroupedTable.setTableGroupId(1L);

        List<OrderTable> tables = Arrays.asList(notGroupedTable, alreadyGroupedTable);
        TableGroup tableGroup = createTableGroup(tables);

        given(orderTableDao.findAllByIdIn(anyList())).willReturn(tables);

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("create Error: 테이블을 그룹으로 묶을 수 없습니다. (테이블이 비어있지 않거나, 이미 그룹이 되어있는 테이블)");
    }

    @Test
    @DisplayName("테이블 그룹을 해체할 수 있어야 한다.")
    void ungroup() {
        List<OrderTable> orderTables = Arrays.asList(orderTable1, orderTable2);
        TableGroup tableGroup = createTableGroup(orderTables);
        tableGroup.setId(1L);
        orderTable1.setTableGroupId(tableGroup.getId());
        orderTable2.setTableGroupId(tableGroup.getId());

        given(orderTableDao.findAllByTableGroupId(tableGroup.getId())).willReturn(orderTables);
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList())).willReturn(false);
        given(orderTableDao.save(any())).willReturn(any());

        tableGroupService.ungroup(tableGroup.getId());

        assertAll(
            () -> assertThat(orderTables.get(0).isEmpty()).isEqualTo(false),
            () -> assertThat(orderTables.get(1).isEmpty()).isEqualTo(false),
            () -> assertThat(orderTables.get(0).getTableGroupId()).isNull(),
            () -> assertThat(orderTables.get(1).getTableGroupId()).isNull()
        );
    }

    @Test
    @DisplayName("해체할 테이블 그룹의 모든 테이블의 주문 상태는 결제 완료여야 한다.")
    void ungroupFail() {
        List<OrderTable> orderTables = Arrays.asList(orderTable1, orderTable2);
        TableGroup tableGroup = createTableGroup(orderTables);
        tableGroup.setId(1L);
        orderTable1.setTableGroupId(1L);
        orderTable2.setTableGroupId(1L);

        given(orderTableDao.findAllByTableGroupId(tableGroup.getId())).willReturn(orderTables);
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList())).willReturn(true);

        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroup.getId()))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("ungroup Error: 결제 완료여야 합니다.");
    }
}
