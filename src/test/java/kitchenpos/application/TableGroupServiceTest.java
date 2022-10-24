package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.transaction.Transactional;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Transactional
class TableGroupServiceTest {

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private TableGroupDao tableGroupDao;

    @Test
    @DisplayName("테이블 단체를 지정한다")
    void create() {
        // given
        final OrderTable orderTable1 = new OrderTable();
        orderTable1.setEmpty(true);
        final OrderTable savedOrderTable1 = orderTableDao.save(orderTable1);

        final OrderTable orderTable2 = new OrderTable();
        orderTable2.setEmpty(true);
        final OrderTable savedOrderTable2 = orderTableDao.save(orderTable2);

        final TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(Arrays.asList(savedOrderTable1, savedOrderTable2));

        // when
        final TableGroup saved = tableGroupService.create(tableGroup);

        // then
        assertAll(
                () -> assertThat(saved.getId()).isNotNull(),
                () -> assertThat(saved.getCreatedDate()).isBeforeOrEqualTo(LocalDateTime.now()),
                () -> assertThat(saved.getOrderTables()).extracting("id")
                        .hasSize(2)
                        .contains(savedOrderTable1.getId(), savedOrderTable2.getId()),
                () -> assertThat(saved.getOrderTables()).extracting("tableGroupId")
                        .containsExactly(saved.getId(), saved.getId()),
                () -> assertThat(saved.getOrderTables()).extracting("empty")
                        .containsExactly(false, false)
        );
    }

    @Test
    @DisplayName("주문 테이블이 비어있을 때 단체를 지정하려고 하면 예외가 발생한다")
    void createWithEmptyOrderTable() {
        // given
        final TableGroup tableGroup = new TableGroup();

        // when, then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 테이블이 1개 이하일 때 단체를 지정하려고 하면 예외가 발생한다")
    void createWithFewOrderTable() {
        // given
        final TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(Collections.singletonList(new OrderTable()));

        // when, then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("존재하지 않은 주문 테이블에 대해서 단체를 지정하려고 하면 예외가 발생한다")
    void createWithNotExistOrderTables() {
        // given
        final TableGroup tableGroup = new TableGroup();
        final List<OrderTable> orderTables = Arrays.asList(new OrderTable(), new OrderTable());
        tableGroup.setOrderTables(orderTables);

        // when, then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문을 등록할 수 없는 주문 테이블에 대해서 단체를 지정하려고 하면 예외가 발생한다")
    void createWithNotEmptyOrderTables() {
        // given
        final OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(false);
        final OrderTable NotEmptyOrderTable = orderTableDao.save(orderTable);
        final OrderTable emptyOrderTable = orderTableDao.save(new OrderTable());

        final TableGroup tableGroup = new TableGroup();
        final List<OrderTable> orderTables = Arrays.asList(NotEmptyOrderTable, emptyOrderTable);
        tableGroup.setOrderTables(orderTables);

        // when, then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("이미 단체로 지정된 주문 테이블에 대해서 단체를 지정하려고 하면 예외가 발생한다")
    void createWithAlreadyGroupedOrderTables() {
        // given
        final TableGroup tableGroup = new TableGroup();
        tableGroup.setCreatedDate(LocalDateTime.now());
        final TableGroup alreadyGroupedTable = tableGroupDao.save(tableGroup);

        final OrderTable orderTable1 = new OrderTable();
        orderTable1.setEmpty(true);
        orderTable1.setTableGroupId(alreadyGroupedTable.getId());
        final OrderTable alreadyGroupedOrderTable1 = orderTableDao.save(orderTable1);

        final OrderTable orderTable2 = new OrderTable();
        orderTable2.setEmpty(true);
        orderTable2.setTableGroupId(alreadyGroupedTable.getId());
        orderTableDao.save(orderTable2);

        final OrderTable orderTable3 = new OrderTable();
        orderTable3.setEmpty(true);
        final OrderTable orderTable = orderTableDao.save(orderTable3);

        final TableGroup newTableGroup = new TableGroup();
        newTableGroup.setOrderTables(Arrays.asList(orderTable, alreadyGroupedOrderTable1));

        // when, then
        assertThatThrownBy(() -> tableGroupService.create(newTableGroup))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("테이블 단체를 해제한다")
    void ungroup() {
        // given
        final TableGroup tableGroup = new TableGroup();
        tableGroup.setCreatedDate(LocalDateTime.now());
        final TableGroup alreadyGroupedTable = tableGroupDao.save(tableGroup);

        final OrderTable orderTable1 = new OrderTable();
        orderTable1.setEmpty(true);
        orderTable1.setTableGroupId(alreadyGroupedTable.getId());
        final OrderTable savedOrderTable1 = orderTableDao.save(orderTable1);

        final OrderTable orderTable2 = new OrderTable();
        orderTable2.setEmpty(true);
        orderTable2.setTableGroupId(alreadyGroupedTable.getId());
        final OrderTable savedOrderTable2 = orderTableDao.save(orderTable2);

        final Order order = new Order();
        order.setOrderStatus(OrderStatus.COMPLETION.name());
        order.setOrderedTime(LocalDateTime.now());
        order.setOrderTableId(savedOrderTable1.getId());
        orderDao.save(order);

        // when
        tableGroupService.ungroup(alreadyGroupedTable.getId());

        // then
        final List<OrderTable> ungroupedOrderTable = orderTableDao.findAllByIdIn(
                Arrays.asList(savedOrderTable1.getId(), savedOrderTable2.getId()));

        assertAll(
                () -> assertThat(ungroupedOrderTable).isNotEmpty(),
                () -> assertThat(ungroupedOrderTable).extracting("tableGroupId")
                        .containsExactly(null, null),
                () -> assertThat(ungroupedOrderTable).extracting("empty")
                        .containsExactly(false, false)
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {"COOKING", "MEAL"})
    @DisplayName("주문의 상태가 COOKING, MEAL인 경우 테이블 단체를 해제하면 예외가 발생한다")
    void ungroupExceptionNotCompletionOrder(final String status) {
        // given
        final TableGroup tableGroup = new TableGroup();
        tableGroup.setCreatedDate(LocalDateTime.now());
        final TableGroup alreadyGroupedTable = tableGroupDao.save(tableGroup);

        final OrderTable orderTable1 = new OrderTable();
        orderTable1.setEmpty(true);
        orderTable1.setTableGroupId(alreadyGroupedTable.getId());
        final OrderTable savedOrderTable1 = orderTableDao.save(orderTable1);

        final OrderTable orderTable2 = new OrderTable();
        orderTable2.setEmpty(true);
        orderTable2.setTableGroupId(alreadyGroupedTable.getId());
        orderTableDao.save(orderTable2);

        final Order order = new Order();
        order.setOrderStatus(status);
        order.setOrderedTime(LocalDateTime.now());
        order.setOrderTableId(savedOrderTable1.getId());
        orderDao.save(order);

        // when, then
        assertThatThrownBy(() -> tableGroupService.ungroup(alreadyGroupedTable.getId()))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }
}
