package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = {"classpath:truncate.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class TableGroupServiceTest {

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private OrderDao orderDao;

    private OrderTable firstTable;
    private OrderTable secondTable;

    @BeforeEach
    void init() {
        final OrderTable firstOrderTable = new OrderTable();
        firstOrderTable.setEmpty(true);
        firstOrderTable.setNumberOfGuests(0);
        firstTable = orderTableDao.save(firstOrderTable);

        final OrderTable secondOrderTable = new OrderTable();
        secondOrderTable.setEmpty(true);
        secondOrderTable.setNumberOfGuests(0);
        secondTable = orderTableDao.save(secondOrderTable);
    }

    @Test
    void 테이블_그룹을_생성한다() {
        // given
        final TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(List.of(firstTable, secondTable));

        // when
        final TableGroup savedTableGroup = tableGroupService.create(tableGroup);

        // then
        assertThat(savedTableGroup.getOrderTables()).hasSize(2);
    }

    @Test
    void 테이블_그룹_생성_시_테이블_리스트가_비어있으면_예외가_발생한다() {
        // given
        final TableGroup tableGroup = new TableGroup();

        // when
        tableGroup.setOrderTables(Collections.emptyList());

        // then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블_그룹_생성_시_테이블_리스트가_1개만_있으면_예외가_발생한다() {
        // given
        final TableGroup tableGroup = new TableGroup();

        // then
        tableGroup.setOrderTables(List.of(firstTable));

        // then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블_그룹_생성_시_이미_다른_그룹에_속한경우_예외가_발생한다() {
        // given
        final TableGroup firstTableGroup = new TableGroup();
        firstTableGroup.setOrderTables(List.of(firstTable, secondTable));

        final TableGroup savedFirstTableGroup = tableGroupService.create(firstTableGroup);

        firstTable.setTableGroupId(savedFirstTableGroup.getId());
        secondTable.setTableGroupId(savedFirstTableGroup.getId());

        // when
        final TableGroup newTableGroup = new TableGroup();
        newTableGroup.setOrderTables(List.of(firstTable, secondTable));

        // then
        assertThatThrownBy(() -> tableGroupService.create(newTableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블_그룹을_해제한다() {
        // given
        final TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(List.of(firstTable, secondTable));
        final TableGroup savedTableGroup = tableGroupService.create(tableGroup);

        // when
        tableGroupService.ungroup(savedTableGroup.getId());

        // then
        final OrderTable ungroupedOrderTable = orderTableDao.findAll().stream()
                .filter(orderTable -> orderTable.getId().equals(firstTable.getId()))
                .findFirst()
                .get();

        assertThat(ungroupedOrderTable.getTableGroupId()).isNull();
    }

    @Test
    void 조리_또는_식사_중인_테이블_그룹을_해제할_때_예외가_발생한다() {
        // given
        final TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(List.of(firstTable, secondTable));
        final TableGroup createdGroup = tableGroupService.create(tableGroup);

        // when
        final Order order = new Order();
        order.setOrderStatus(OrderStatus.COOKING.name());
        order.setOrderTableId(firstTable.getId());
        order.setOrderedTime(LocalDateTime.now());
        orderDao.save(order);

        // then
        assertThatThrownBy(() -> tableGroupService.ungroup(createdGroup.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
