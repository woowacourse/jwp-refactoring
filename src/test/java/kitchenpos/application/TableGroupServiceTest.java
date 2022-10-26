package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.util.Arrays;
import java.util.List;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.support.DataSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class TableGroupServiceTest {

    @Autowired
    private TableGroupService tableGroupService;
    @Autowired
    private DataSupport dataSupport;

    @DisplayName("여러 테이블을 단체로 지정할 수 있다.")
    @Test
    void create() {
        // given
        final TableGroup tableGroup = new TableGroup();
        final OrderTable savedOrderTable1 = dataSupport.saveOrderTable(0, true);
        final OrderTable savedOrderTable2 = dataSupport.saveOrderTable(0, true);
        final List<OrderTable> orderTables = Arrays.asList(savedOrderTable1, savedOrderTable2);
        tableGroup.setOrderTables(orderTables);

        // when, then
        assertThatCode(() -> tableGroupService.create(tableGroup))
                .doesNotThrowAnyException();
    }

    @DisplayName("존재하지 않는 테이블을 그룹으로 지정하면 예외가 발생한다.")
    @Test
    void create_throwsException_ifTableNotFound() {
        // given
        final TableGroup tableGroup = new TableGroup();
        final OrderTable savedOrderTable = dataSupport.saveOrderTable(0, true);
        final OrderTable unsavedOrderTable = new OrderTable();
        unsavedOrderTable.setTableGroupId(100L);
        final List<OrderTable> orderTables = Arrays.asList(savedOrderTable, unsavedOrderTable);
        tableGroup.setOrderTables(orderTables);

        // when, then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> tableGroupService.create(tableGroup));
    }

    @DisplayName("2개 미만의 테이블을 그룹으로 지정하면 예외가 발생한다.")
    @Test
    void create_throwsException_ifTableUnder2() {
        // given
        final TableGroup tableGroup = new TableGroup();
        final OrderTable savedOrderTable = dataSupport.saveOrderTable(0, true);
        final List<OrderTable> orderTables = Arrays.asList(savedOrderTable);
        tableGroup.setOrderTables(orderTables);

        // when, then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> tableGroupService.create(tableGroup));
    }

    @DisplayName("2개 미만의 테이블을 그룹으로 지정하면 예외가 발생한다.")
    @Test
    void create_throwsException_ifTableNotEmpty() {
        // given
        final TableGroup tableGroup = new TableGroup();
        final OrderTable savedEmptyTable = dataSupport.saveOrderTable(0, true);
        final OrderTable savedNotEmptyTable = dataSupport.saveOrderTable(0, false);
        final List<OrderTable> orderTables = Arrays.asList(savedEmptyTable, savedNotEmptyTable);
        tableGroup.setOrderTables(orderTables);

        // when, then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> tableGroupService.create(tableGroup));
    }

    @DisplayName("이미 그룹으로 지정된 테이블을 그룹으로 지정하면 예외가 발생한다.")
    @Test
    void create_throwsException_ifTableGrouped() {
        // given
        final TableGroup savedTableGroup = dataSupport.saveTableGroup();
        final TableGroup tableGroup = new TableGroup();
        final OrderTable savedUngroupedTable = dataSupport.saveOrderTable(0, true);
        final OrderTable savedGroupedTable = dataSupport.saveOrderTableWithGroup(savedTableGroup.getId(), 0, true);
        final List<OrderTable> orderTables = Arrays.asList(savedUngroupedTable, savedGroupedTable);
        tableGroup.setOrderTables(orderTables);

        // when, then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> tableGroupService.create(tableGroup));
    }

    @DisplayName("지정된 단체를 해제할 수 있다.")
    @Test
    void ungroup() {
        // given
        final TableGroup savedTableGroup = dataSupport.saveTableGroup();
        dataSupport.saveOrderTableWithGroup(savedTableGroup.getId(), 0, false);
        dataSupport.saveOrderTableWithGroup(savedTableGroup.getId(), 0, false);

        // when, then
        assertThatCode(() -> tableGroupService.ungroup(savedTableGroup.getId()))
                .doesNotThrowAnyException();
    }

    @DisplayName("조리중인 테이블이 있는 단체를 해제하면 예외가 발생한다.")
    @Test
    void ungroup_throwsException_ifCooking() {
        // given
        final TableGroup savedTableGroup = dataSupport.saveTableGroup();
        final OrderTable groupedTable = dataSupport.saveOrderTableWithGroup(savedTableGroup.getId(), 0, false);
        dataSupport.saveOrderTableWithGroup(savedTableGroup.getId(), 0, false);
        dataSupport.saveOrder(groupedTable.getId(), OrderStatus.COOKING.name());

        // when, then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> tableGroupService.ungroup(savedTableGroup.getId()));
    }

    @DisplayName("식사중인 테이블이 있는 단체를 해제하면 예외가 발생한다.")
    @Test
    void ungroup_throwsException_ifMeal() {
        // given
        final TableGroup savedTableGroup = dataSupport.saveTableGroup();
        final OrderTable groupedTable = dataSupport.saveOrderTableWithGroup(savedTableGroup.getId(), 0, false);
        dataSupport.saveOrderTableWithGroup(savedTableGroup.getId(), 0, false);
        dataSupport.saveOrder(groupedTable.getId(), OrderStatus.MEAL.name());

        // when, then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> tableGroupService.ungroup(savedTableGroup.getId()));
    }
}
