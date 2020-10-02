package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static kitchenpos.DomainFactory.createOrderTable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class TableGroupServiceTest extends ServiceTest {
    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private TableGroupDao tableGroupDao;

    private TableGroupService tableGroupService;

    static Stream<TableGroup> invalidTableGroupWithOrderTable() {
        TableGroup tableGroupWithEmptyOrderTable = new TableGroup();
        tableGroupWithEmptyOrderTable.setOrderTables(Collections.emptyList());

        TableGroup tableGroupWithOrderTableSizeUnderTwo = new TableGroup();
        tableGroupWithOrderTableSizeUnderTwo.setOrderTables(Collections.singletonList(new OrderTable()));

        return Stream.of(
                tableGroupWithEmptyOrderTable, tableGroupWithOrderTableSizeUnderTwo
        );
    }

    @BeforeEach
    void setUp() {
        tableGroupService = new TableGroupService(orderDao, orderTableDao, tableGroupDao);
        orderTableIds = new ArrayList<>();
        orderIds = new ArrayList<>();
        tableGroupIds = new ArrayList<>();
    }

    @DisplayName("새로운 단체 지정")
    @Test
    void createTest() {
        OrderTable firstOrderTable = saveOrderTable(orderTableDao, 1, true);
        OrderTable secondOrderTable = saveOrderTable(orderTableDao, 2, true);

        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(Arrays.asList(firstOrderTable, secondOrderTable));

        TableGroup savedTableGroup = tableGroupService.create(tableGroup);
        tableGroupIds.add(savedTableGroup.getId());

        assertAll(
                () -> assertThat(savedTableGroup.getId()).isNotNull(),
                () -> assertThat(savedTableGroup.getOrderTables()).hasSize(2),
                () -> assertThat(savedTableGroup.getCreatedDate()).isNotNull()
        );
    }

    @DisplayName("새로운 단체 지정 시 주문 테이블의 개수가 잘못 되었을 때 예외 반환")
    @ParameterizedTest
    @MethodSource("invalidTableGroupWithOrderTable")
    void createTableGroupWithInvalidOrderTableTest(TableGroup tableGroup) {
        assertThatThrownBy(() -> {
            tableGroupService.create(tableGroup);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("새로운 단체 지정 시 존재하지 않는 주문 테이블 입력 시 예외 반환")
    @Test
    void createTableGroupWithInvalidOrderTableTest() {
        OrderTable firstInvalidOrderTable = createOrderTable(0, false);
        OrderTable secondInvalidOrderTable = createOrderTable(0, false);

        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(Arrays.asList(firstInvalidOrderTable, secondInvalidOrderTable));

        assertThatThrownBy(() -> {
            tableGroupService.create(tableGroup);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정 해제")
    @Test
    void ungroupTest() {
        TableGroup savedTableGroup = saveTableGroup(tableGroupDao);
        saveOrderTable(orderTableDao, 1, true, savedTableGroup.getId());
        saveOrderTable(orderTableDao, 2, true, savedTableGroup.getId());

        tableGroupService.ungroup(savedTableGroup.getId());
        List<OrderTable> ungroupedOrderTables = orderTableDao.findAllByIdIn(orderTableIds);

        assertAll(
                () -> assertThat(ungroupedOrderTables.get(0).getTableGroupId()).isNull(),
                () -> assertThat(ungroupedOrderTables.get(0).getNumberOfGuests()).isEqualTo(1),
                () -> assertThat(ungroupedOrderTables.get(0).isEmpty()).isFalse(),
                () -> assertThat(ungroupedOrderTables.get(1).getTableGroupId()).isNull(),
                () -> assertThat(ungroupedOrderTables.get(1).getNumberOfGuests()).isEqualTo(2),
                () -> assertThat(ungroupedOrderTables.get(1).isEmpty()).isFalse()
        );
    }

    @DisplayName("단체 테이블 중 결제 완료 되지 않은 주문이 남아있을 때 단체 지정 해제 시 예외 반환")
    @Test
    void ungroupWithInvalidOrderTableTest() {
        TableGroup savedTableGroup = saveTableGroup(tableGroupDao);
        OrderTable unPairedOrderTable = saveOrderTable(orderTableDao, 1, true, savedTableGroup.getId());
        saveOrderTable(orderTableDao, 2, true, savedTableGroup.getId());
        saveOrder(orderDao, unPairedOrderTable.getId(), OrderStatus.MEAL.name(), LocalDateTime.now());

        assertThatThrownBy(() -> {
            tableGroupService.ungroup(savedTableGroup.getId());
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @AfterEach
    void tearDown() {
        deleteOrder();
        deleteOrderTable();
        deleteTableGroup();
    }
}