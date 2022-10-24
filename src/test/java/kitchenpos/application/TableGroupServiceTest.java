package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

class TableGroupServiceTest extends ServiceTest {

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private TableGroupDao tableGroupDao;

    @Autowired
    private OrderDao orderDao;

    private OrderTable orderTable1;

    @BeforeEach
    void setUp() {
        orderTable1 = orderTableDao.save(new OrderTable(null, 3, true));
    }

    @DisplayName("TableGroup을 생성할 수 있다.")
    @Test
    void create() {
        OrderTable orderTable2 = orderTableDao.save(new OrderTable(null, 5, true));

        TableGroup tableGroup = tableGroupService.create(new TableGroup(List.of(orderTable1, orderTable2)));

        OrderTable groupedOrderTable1 = orderTableDao.findById(orderTable1.getId())
                .orElseThrow();
        OrderTable groupedOrderTable2 = orderTableDao.findById(orderTable2.getId())
                .orElseThrow();
        assertAll(
                () -> assertThat(groupedOrderTable1.getTableGroupId()).isEqualTo(tableGroup.getId()),
                () -> assertThat(groupedOrderTable2.getTableGroupId()).isEqualTo(tableGroup.getId())
        );
    }

    @DisplayName("2개 미만의 OrderTable로 TableGroup을 생성하려고 하면 예외를 발생시킨다.")
    @Test
    void create_Exception_NotEnoughOrderTableNumber() {
        assertThatThrownBy(() -> tableGroupService.create(new TableGroup(List.of(orderTable1))))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Table Group은 2개 이상의 테이블로 생성할 수 있습니다.");
    }

    @DisplayName("존재하지 않는 OrderTable로 TableGroup을 생성하려고 하면 예외를 발생시킨다.")
    @Test
    void create_Exception_NotFoundOrderTable() {
        OrderTable unsavedOrderTable = new OrderTable();

        assertThatThrownBy(() -> tableGroupService.create(new TableGroup(List.of(orderTable1, unsavedOrderTable))))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 테이블로는 Table Group을 생성할 수 없습니다.");
    }

    @DisplayName("empty가 아닌 OrderTable로 TableGroup을 생성하려고 하면 예외를 발생시킨다.")
    @Test
    void create_Exception_NotEmptyOrderTable() {
        OrderTable orderTable2 = orderTableDao.save(new OrderTable(null, 5, false));

        assertThatThrownBy(() -> tableGroupService.create(new TableGroup(List.of(orderTable1, orderTable2))))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("empty 상태가 아닌 테이블로는 Table Group을 생성할 수 없습니다.");
    }

    @DisplayName("이미 TableGroup에 속해 있는 OrderTable로 TableGroup을 생성하려고 하면 예외를 발생시킨다.")
    @Test
    void create_Exception_AlreadyGroupedOrderTable() {
        TableGroup tableGroup = tableGroupDao.save(new TableGroup());
        OrderTable orderTable2 = orderTableDao.save(new OrderTable(tableGroup.getId(), 5, true));

        assertThatThrownBy(() -> tableGroupService.create(new TableGroup(List.of(orderTable1, orderTable2))))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 Table Group에 속해있는 테이블로는 Table Group을 생성할 수 없습니다.");
    }

    @DisplayName("TableGroup을 그룹 해제할 수 있다.")
    @Test
    void ungroup() {
        OrderTable orderTable2 = orderTableDao.save(new OrderTable(null, 5, true));
        TableGroup tableGroup = tableGroupService.create(new TableGroup(List.of(orderTable1, orderTable2)));

        tableGroupService.ungroup(tableGroup.getId());

        OrderTable groupedOrderTable1 = orderTableDao.findById(orderTable1.getId())
                .orElseThrow();
        OrderTable groupedOrderTable2 = orderTableDao.findById(orderTable2.getId())
                .orElseThrow();
        assertAll(
                () -> assertThat(groupedOrderTable1.getTableGroupId()).isNull(),
                () -> assertThat(groupedOrderTable2.getTableGroupId()).isNull()
        );
    }

    @DisplayName("조리중이거나 식사중인 테이블이 존재하는 TableGroup을 그룹 해제할 수 없다.")
    @ParameterizedTest
    @ValueSource(strings = {"COOKING", "MEAL"})
    void ungroup_Exception_NotCompleteOrderTableStatus(String orderStatus) {
        OrderTable orderTable2 = orderTableDao.save(new OrderTable(null, 5, true));
        TableGroup tableGroup = tableGroupService.create(new TableGroup(List.of(orderTable1, orderTable2)));
        orderDao.save(new Order(orderTable2.getId(), orderStatus));

        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroup.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("조리중이거나 식사중인 테이블이 포함된 Table Group은 그룹 해제 할 수 없습니다.");
    }
}
