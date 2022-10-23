package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.dao.OrderDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class TableGroupServiceTest {

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private TableService tableService;

    @SpyBean
    private OrderDao orderDao;

    @DisplayName("새로운 단체 지정(table group)을 생성할 수 있다.")
    @Test
    void create() {
        // given
        final List<OrderTable> orderTables = tableService.list();
        final TableGroup tableGroup = new TableGroup(LocalDateTime.now(), orderTables);

        // when
        final TableGroup createdTableGroup = tableGroupService.create(tableGroup);

        // then
        assertThat(createdTableGroup).isNotNull();
        assertThat(createdTableGroup.getId()).isNotNull();
    }

    @DisplayName("새로운 테이블 그룹의 주문 테이블이 비어있거나 그룹화하려는 주문 테이블이 2개 보다 작을 수는 없다.")
    @Test
    void canNotCreateTableGroupLessThenTwoTable() {
        // given
        final List<OrderTable> orderTables = tableService.list();
        final OrderTable orderTable = orderTables.get(0);
        final TableGroup tableGroup = new TableGroup(LocalDateTime.now(), List.of(orderTable));

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
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
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블이 비어있고 이미 단체 지정되지 않은 경우에만 새롭게 지정할 수 있다.")
    @Test
    void canNotCreateTableGroupWhenAlreadyGrouping() {
        // given
        final List<OrderTable> orderTables = tableService.list();
        tableGroupService.create(new TableGroup(LocalDateTime.now(), orderTables));

        final OrderTable orderTable1 = orderTables.get(0);
        final OrderTable orderTable2 = orderTables.get(1);
        final TableGroup tableGroup = new TableGroup(LocalDateTime.now(), List.of(orderTable1, orderTable2));

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정(table group)을 해제할 수 있다.")
    @Test
    void ungroup() {
        // given
        final List<OrderTable> orderTables = tableService.list();
        final TableGroup tableGroup = tableGroupService.create(new TableGroup(LocalDateTime.now(), orderTables));

        // when
        tableGroupService.ungroup(tableGroup.getId());

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
        when(orderDao.existsByOrderTableIdInAndOrderStatusIn(any(), any()))
                .thenReturn(true);

        final OrderTable orderTable = new OrderTable(0, true);
        final OrderTable anotherOrderTable = new OrderTable(0, true);

        final OrderTable createdOrderTable = tableService.create(orderTable);
        final OrderTable createdAnotherOrderTable = tableService.create(anotherOrderTable);

        final TableGroup tableGroup = new TableGroup(LocalDateTime.now(), List.of(createdOrderTable, createdAnotherOrderTable));
        final TableGroup createdTableGroup = tableGroupService.create(tableGroup);

        // when & then
        assertThatThrownBy(() -> tableGroupService.ungroup(createdTableGroup.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
