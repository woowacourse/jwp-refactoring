package kitchenpos.application;

import static kitchenpos.application.DomainFixture.getEmptyTable;
import static kitchenpos.application.DomainFixture.getNotEmptyTable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class TableGroupServiceTest {

    @MockBean
    private OrderDao orderDao;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private TableGroupDao tableGroupDao;

    @Autowired
    private final TableGroupService tableGroupService = new TableGroupService(orderDao, orderTableDao, tableGroupDao);


    private OrderTable table1;
    private OrderTable table2;

    @BeforeEach
    void setUp() {
        table1 = orderTableDao.save(getEmptyTable());
        table2 = orderTableDao.save(getEmptyTable());
    }

    @DisplayName("단체 지정을 등록한다.")
    @Test
    void create() {
        final TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(List.of(table1, table2));
        final TableGroup savedTableGroup = tableGroupService.create(tableGroup);

        final OrderTable table1 = orderTableDao.findById(this.table1.getId()).get();
        final OrderTable table2 = orderTableDao.findById(this.table2.getId()).get();

        assertAll(
                () -> assertThat(savedTableGroup.getId()).isNotNull(),
                () -> assertThat(savedTableGroup.getCreatedDate()).isBefore(LocalDateTime.now()),
                () -> assertThat(savedTableGroup.getOrderTables()).hasSize(2),
                () -> assertThat(table1.getTableGroupId()).isEqualTo(savedTableGroup.getId()),
                () -> assertThat(table2.getTableGroupId()).isEqualTo(savedTableGroup.getId())
        );
    }

    @DisplayName("단체 지정을 등록한다. - 주문 테이블이 존재하지 않으면 예외를 반환한다.")
    @Test
    void create_exception_noTables() {
        final TableGroup tableGroup = new TableGroup();

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정을 등록한다. - 주문 테이블이 2개보다 적으면 예외를 반환한다.")
    @Test
    void create_exception_tableLessThanTwo() {
        final TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(List.of(table1));

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정을 등록한다. - 비어있지 않은 테이블이 포함되어 있다면 예외를 반환한다.")
    @Test
    void create_exception_containsNotEmptyTable() {
        final TableGroup tableGroup = new TableGroup();
        final OrderTable table3 = orderTableDao.save(getNotEmptyTable(0));
        tableGroup.setOrderTables(List.of(table1, table2, table3));

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정을 등록한다. - 이미 다른 단체에 지정된 테이블이 포함되어 있다면 예외를 반환한다.")
    @Test
    void create_exception_containsAlreadyGroupedTable() {
        final TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(List.of(table1, table2));
        tableGroupService.create(tableGroup);

        final OrderTable table3 = orderTableDao.save(getNotEmptyTable(0));
        final TableGroup newTableGroup = new TableGroup();
        newTableGroup.setOrderTables(List.of(table1, table3));

        assertThatThrownBy(() -> tableGroupService.create(newTableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정을 해체한다.")
    @Test
    void list() {
        final TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(List.of(table1, table2));
        final TableGroup savedTableGroup = tableGroupService.create(tableGroup);

        tableGroupService.ungroup(savedTableGroup.getId());

        final OrderTable table1 = orderTableDao.findById(this.table1.getId()).get();
        final OrderTable table2 = orderTableDao.findById(this.table2.getId()).get();

        assertAll(
                () -> assertThat(table1.getTableGroupId()).isNull(),
                () -> assertThat(table2.getTableGroupId()).isNull()
        );
    }

    @DisplayName("단체 지정을 해체한다. - 주문 상태가 COOKING이거나 MEAL인 테이블이 존재한다면 예외를 반환한다.")
    @Test
    void list_exception_orderStatusIsCookingOrMeal() {
        final TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(List.of(table1, table2));
        final TableGroup savedTableGroup = tableGroupService.create(tableGroup);

        when(orderDao.existsByOrderTableIdInAndOrderStatusIn(List.of(table1.getId(), table2.getId()), List.of(
                OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).thenReturn(true);

        assertThatThrownBy(() -> tableGroupService.ungroup(savedTableGroup.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
