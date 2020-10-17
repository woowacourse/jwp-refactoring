package kitchenpos.application;

import kitchenpos.application.common.TestObjectFactory;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
@Sql("/delete_all.sql")
class TableGroupServiceTest {
    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private OrderDao orderDao;

    @DisplayName("테이블 그룹 생성 메서드 테스트")
    @Test
    void create() {
        OrderTable orderTable1 = orderTableDao.save(TestObjectFactory.creatOrderTable());
        OrderTable orderTable2 = orderTableDao.save(TestObjectFactory.creatOrderTable());
        List<OrderTable> orderTables = Arrays.asList(orderTable1, orderTable2);

        TableGroup tableGroup = TestObjectFactory.createTableGroupDto(orderTables);
        TableGroup savedTableGroup = tableGroupService.create(tableGroup);

        List<OrderTable> orderTablesByTableGroupId = orderTableDao.findAllByTableGroupId(savedTableGroup.getId());
        assertAll(
                () -> assertThat(savedTableGroup.getId()).isNotNull(),
                () -> assertThat(orderTablesByTableGroupId).hasSize(2)
        );
    }

    @DisplayName("테이블 그룹 생성 - 그룹을 맺으려는 테이블의 수가 2보다 작은 경우 예외 처리")
    @Test
    void createWithOrderTablesLessTwo() {
        OrderTable orderTable1 = orderTableDao.save(TestObjectFactory.creatOrderTable());
        List<OrderTable> orderTables = Arrays.asList(orderTable1);

        TableGroup tableGroup = TestObjectFactory.createTableGroupDto(orderTables);

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 그룹 생성 - 존재하지 않는 테이블의 아이디를 입력받은 경우 예외처리")
    @Test
    void createWithNotFoundOrderTable() {
        OrderTable orderTable1 = orderTableDao.save(TestObjectFactory.creatOrderTable());
        OrderTable orderTable2 = TestObjectFactory.creatOrderTable();
        List<OrderTable> orderTables = Arrays.asList(orderTable1, orderTable2);

        TableGroup tableGroup = TestObjectFactory.createTableGroupDto(orderTables);

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 그룹 생성 - 이미 그룹핑된 테이블인 경우 예외처리")
    @Test
    void createWithGroupedTable() {
        OrderTable orderTable1 = orderTableDao.save(TestObjectFactory.creatOrderTable());
        OrderTable orderTable2 = orderTableDao.save(TestObjectFactory.creatOrderTable());
        List<OrderTable> orderTables = Arrays.asList(orderTable1, orderTable2);
        TableGroup tableGroup = TestObjectFactory.createTableGroupDto(orderTables);
        tableGroupService.create(tableGroup);

        OrderTable orderTable3 = orderTableDao.save(TestObjectFactory.creatOrderTable());
        List<OrderTable> orderTables2 = Arrays.asList(orderTable2, orderTable3);
        TableGroup tableGroupContainsGroupedTable = TestObjectFactory.createTableGroupDto(orderTables2);

        assertThatThrownBy(() -> tableGroupService.create(tableGroupContainsGroupedTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 그룹을 해지하는 메서드 테스트")
    @Test
    void ungroup() {
        OrderTable orderTable1 = orderTableDao.save(TestObjectFactory.creatOrderTable());
        OrderTable orderTable2 = orderTableDao.save(TestObjectFactory.creatOrderTable());
        List<OrderTable> orderTables = Arrays.asList(orderTable1, orderTable2);

        TableGroup tableGroup = TestObjectFactory.createTableGroupDto(orderTables);
        TableGroup savedTableGroup = tableGroupService.create(tableGroup);

        tableGroupService.ungroup(savedTableGroup.getId());

        List<OrderTable> orderTablesByTableGroupId = orderTableDao.findAllByTableGroupId(savedTableGroup.getId());
        assertAll(
                () -> assertThat(orderTablesByTableGroupId).hasSize(0)
        );
    }

    @DisplayName("테이블 그룹을 해지 - OrderStatus가 COOKING 혹은 MEAL 인 경우 예외처리")
    @ParameterizedTest
    @CsvSource({"COOKING", "MEAL"})
    void ungroupWhenCookingOrMeal(OrderStatus orderStatus) {
        OrderTable orderTable1 = orderTableDao.save(TestObjectFactory.creatOrderTable());
        OrderTable orderTable2 = orderTableDao.save(TestObjectFactory.creatOrderTable());
        List<OrderTable> orderTables = Arrays.asList(orderTable1, orderTable2);

        TableGroup tableGroup = TestObjectFactory.createTableGroupDto(orderTables);
        TableGroup savedTableGroup = tableGroupService.create(tableGroup);

        Order order = TestObjectFactory.createOrder(orderTable1, orderStatus, new ArrayList<>());
        orderDao.save(order);

        assertThatThrownBy(() -> tableGroupService.ungroup(savedTableGroup.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
