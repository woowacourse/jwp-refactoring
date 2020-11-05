package kitchenpos.application;

import static kitchenpos.domain.DomainCreator.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

@SpringBootTest
@Transactional
@Sql("classpath:delete.sql")
class TableGroupServiceTest {
    @Autowired
    private TableGroupService tableGroupService;
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private OrderTableDao orderTableDao;
    @Autowired
    private TableGroupDao tableGroupDao;

    private OrderTable orderTable1;
    private OrderTable orderTable2;

    @BeforeEach
    void setUp() {
        tableGroupService = new TableGroupService(orderDao, orderTableDao, tableGroupDao);

        orderTable1 = orderTableDao.save(createOrderTable(true));
        orderTable2 = orderTableDao.save(createOrderTable(true));

        orderTableDao.save(orderTable1);
        orderTableDao.save(orderTable2);
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

        TableGroup savedTableGroup = tableGroupService.create(tableGroup);
        List<OrderTable> savedOrderTables = savedTableGroup.getOrderTables();

        assertAll(
            () -> assertThat(savedTableGroup.getId()).isNotNull(),
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
        OrderTable emptyTable = orderTableDao.save(createOrderTable(true));
        OrderTable nonEmptyTable = orderTableDao.save(createOrderTable(false));
        List<OrderTable> tables = Arrays.asList(emptyTable, nonEmptyTable);
        TableGroup tableGroup = createTableGroup(tables);

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("create Error: 테이블을 그룹으로 묶을 수 없습니다. (테이블이 비어있지 않거나, 이미 그룹이 되어있는 테이블)");
    }

    @Test
    @DisplayName("그룹으로 묶을 테이블은 다른 테이블 그룹이 아니어야 한다.")
    void createErrorWhenTableGroupIsInOtherTableGroup() {
        OrderTable notGroupedTable = orderTableDao.save(createOrderTable(true));
        OrderTable alreadyGroupedTable1 = orderTableDao.save(createOrderTable(true));
        OrderTable alreadyGroupedTable2 = orderTableDao.save(createOrderTable(true));

        tableGroupService.create(createTableGroup(Arrays.asList(alreadyGroupedTable1, alreadyGroupedTable2)));

        List<OrderTable> tables = Arrays.asList(notGroupedTable, alreadyGroupedTable1);
        TableGroup tableGroup2 = createTableGroup(tables);

        assertThatThrownBy(() -> tableGroupService.create(tableGroup2))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("create Error: 테이블을 그룹으로 묶을 수 없습니다. (테이블이 비어있지 않거나, 이미 그룹이 되어있는 테이블)");
    }

    @Test
    @DisplayName("테이블 그룹을 해체할 수 있어야 한다.")
    void ungroup() {
        List<OrderTable> orderTables = Arrays.asList(orderTable1, orderTable2);
        TableGroup tableGroup = tableGroupService.create(createTableGroup(orderTables));

        tableGroupService.ungroup(tableGroup.getId());
        List<OrderTable> ungroupedOrderTables = orderTableDao.findAllByTableGroupId(tableGroup.getId());

        assertAll(
            () -> assertThat(ungroupedOrderTables.size()).isZero(),
            () -> assertThat(orderTables.get(0).getTableGroupId()).isNull(),
            () -> assertThat(orderTables.get(1).getTableGroupId()).isNull()
        );
    }

    @Test
    @DisplayName("해체할 테이블 그룹의 모든 테이블의 주문 상태는 결제 완료여야 한다.")
    void ungroupFail() {
        orderDao.save(createOrder(OrderStatus.MEAL, orderTable1.getId()));

        List<OrderTable> orderTables = Arrays.asList(orderTable1, orderTable2);
        TableGroup tableGroup = tableGroupService.create(createTableGroup(orderTables));

        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroup.getId()))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("ungroup Error: 결제 완료여야 합니다.");
    }
}
