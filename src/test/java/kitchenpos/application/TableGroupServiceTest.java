package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.RepositoryTest;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@RepositoryTest
class TableGroupServiceTest {

    private TableGroupService sut;
    private TableService tableService;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private TableGroupDao tableGroupDao;

    @BeforeEach
    void setUp() {
        sut = new TableGroupService(orderDao, orderTableDao, tableGroupDao);
        tableService = new TableService(orderDao, orderTableDao);
    }

    @DisplayName("새로운 단체 지정(table group)을 생성할 수 있다.")
    @Test
    void create() {
        // given
        final List<OrderTable> orderTables = tableService.list();
        final TableGroup tableGroup = new TableGroup(LocalDateTime.now(), orderTables);

        // when
        final TableGroup createdTableGroup = sut.create(tableGroup);

        // then
        assertThat(createdTableGroup).isNotNull();
        assertThat(createdTableGroup.getId()).isNotNull();
        final TableGroup foundTableGroup = tableGroupDao.findById(createdTableGroup.getId()).get();
        assertThat(foundTableGroup)
                .usingRecursiveComparison()
                .ignoringFields("id", "orderTables")
                .isEqualTo(createdTableGroup);
    }

    @DisplayName("새로운 테이블 그룹의 주문 테이블이 비어있거나 그룹화하려는 주문 테이블이 2개 보다 작을 수는 없다.")
    @Test
    void canNotCreateTableGroupLessThenTwoTable() {
        // given
        final List<OrderTable> orderTables = tableService.list();
        final OrderTable orderTable = orderTables.get(0);
        final TableGroup tableGroup = new TableGroup(LocalDateTime.now(), List.of(orderTable));

        // when & then
        assertThatThrownBy(() -> sut.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정하려는 개별 주문 테이블이 실제 존재하는 주문 테이블이어야 한다.")
    @Test
    void canCreateTableGroupWhenExistOrderTable() {
        // given
        final OrderTable orderTable1 = new OrderTable(1, true);
        final OrderTable orderTable2 = new OrderTable(1, true);
        final TableGroup tableGroup = new TableGroup(LocalDateTime.now(), List.of(orderTable1, orderTable2));

        // when & then
        assertThatThrownBy(() -> sut.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블이 비어있고 이미 단체 지정되지 않은 경우에만 새롭게 지정할 수 있다.")
    @Test
    void canNotCreateTableGroupWhenAlreadyGrouping() {
        // given
        final List<OrderTable> orderTables = tableService.list();
        sut.create(new TableGroup(LocalDateTime.now(), orderTables));

        final OrderTable orderTable1 = orderTables.get(0);
        final OrderTable orderTable2 = orderTables.get(1);
        final TableGroup tableGroup = new TableGroup(LocalDateTime.now(), List.of(orderTable1, orderTable2));

        // when & then
        assertThatThrownBy(() -> sut.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }
    
    @DisplayName("테이블이 이미 그룹에 지정되어 그룹 id 를 가지고 있으면 그룹핑 할 수 없다.")
    @Test
    void canNotCreateTableGroupWhenTableInGroup() {
        // given
        final List<OrderTable> orderTables = tableService.list();

        final TableGroup createdTableGroup = sut.create(new TableGroup(LocalDateTime.now(), orderTables));
        final OrderTable orderTable = new OrderTable(createdTableGroup.getId(), 0, true);
        final OrderTable savedOrderTable = orderTableDao.save(orderTable);
        orderTables.add(savedOrderTable);

        final TableGroup tableGroup = new TableGroup(LocalDateTime.now(), orderTables);

        // when & then
        assertThatThrownBy(() -> sut.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정(table group)을 해제할 수 있다.")
    @Test
    void ungroup() {
        // given
        final List<OrderTable> orderTables = tableService.list();
        final TableGroup tableGroup = sut.create(new TableGroup(LocalDateTime.now(), orderTables));

        // when
        sut.ungroup(tableGroup.getId());

        // then
        final List<OrderTable> results = tableService.list();
        assertThat(results)
                .hasSize(8)
                .extracting("tableGroupId")
                .containsExactly(null, null, null, null, null, null, null, null);
    }

    @DisplayName("이미 조리 중이거나 식사중인 테이블이 있으면 해제할 수 없다.")
    @Test
    void canNotUngroupWhenCookOrMeal() {
        // given
        final OrderTable orderTable = new OrderTable(0, true);
        final OrderTable anotherOrderTable = new OrderTable(0, true);

        final OrderTable createdOrderTable = tableService.create(orderTable);
        final OrderTable createdAnotherOrderTable = tableService.create(anotherOrderTable);

        final TableGroup tableGroup = new TableGroup(LocalDateTime.now(), List.of(createdOrderTable, createdAnotherOrderTable));
        final TableGroup createdTableGroup = sut.create(tableGroup);

        saveCookingOrder(createdOrderTable);

        // when & then
        assertThatThrownBy(() -> sut.ungroup(createdTableGroup.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private void saveCookingOrder(final OrderTable createdOrderTable) {
        final OrderLineItem orderLineItem = new OrderLineItem(1L, 1L, 1L, 1L);
        final Order order = new Order(createdOrderTable.getId(), "COOKING", LocalDateTime.now(), List.of(orderLineItem));
        orderDao.save(order);
    }
}
