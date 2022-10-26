package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import javax.transaction.Transactional;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.fixtures.OrderFixtures;
import kitchenpos.fixtures.OrderTableFixtures;
import kitchenpos.fixtures.TableGroupFixtures;
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
        final OrderTable orderTable1 = OrderTableFixtures.createEmptyTable(null);
        final OrderTable savedOrderTable1 = orderTableDao.save(orderTable1);

        final OrderTable orderTable2 = OrderTableFixtures.createEmptyTable(null);
        final OrderTable savedOrderTable2 = orderTableDao.save(orderTable2);

        final TableGroup tableGroup = TableGroupFixtures.createWithOrderTables(savedOrderTable1, savedOrderTable2);

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
        final TableGroup tableGroup = TableGroupFixtures.create();

        // when, then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 테이블이 1개 이하일 때 단체를 지정하려고 하면 예외가 발생한다")
    void createWithFewOrderTable() {
        // given
        final OrderTable orderTable = OrderTableFixtures.createWithGuests(null, 2);
        final TableGroup tableGroup = TableGroupFixtures.createWithOrderTables(orderTable);

        // when, then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("존재하지 않은 주문 테이블에 대해서 단체를 지정하려고 하면 예외가 발생한다")
    void createWithNotExistOrderTables() {
        // given
        final OrderTable orderTable1 = OrderTableFixtures.createWithGuests(null, 2);
        final OrderTable orderTable2 = OrderTableFixtures.createWithGuests(null, 2);
        final TableGroup tableGroup = TableGroupFixtures.createWithOrderTables(orderTable1, orderTable2);

        // when, then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문을 등록할 수 없는 주문 테이블에 대해서 단체를 지정하려고 하면 예외가 발생한다")
    void createWithNotEmptyOrderTables() {
        // given
        final OrderTable orderTable1 = OrderTableFixtures.createWithGuests(null, 2);
        final OrderTable NotEmptyOrderTable = orderTableDao.save(orderTable1);
        final OrderTable orderTable2 = OrderTableFixtures.createEmptyTable(null);
        final OrderTable emptyOrderTable = orderTableDao.save(orderTable2);

        final TableGroup tableGroup = TableGroupFixtures.createWithOrderTables(NotEmptyOrderTable, emptyOrderTable);

        // when, then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("이미 단체로 지정된 주문 테이블에 대해서 단체를 지정하려고 하면 예외가 발생한다")
    void createWithAlreadyGroupedOrderTables() {
        // given
        final TableGroup tableGroup = TableGroupFixtures.create();
        final TableGroup alreadyGroupedTable = tableGroupDao.save(tableGroup);

        final OrderTable orderTable1 = OrderTableFixtures.createWithGuests(alreadyGroupedTable.getId(), 2);
        final OrderTable alreadyGroupedOrderTable1 = orderTableDao.save(orderTable1);

        final OrderTable orderTable2 = OrderTableFixtures.createWithGuests(alreadyGroupedTable.getId(), 2);
        orderTableDao.save(orderTable2);

        final OrderTable orderTable3 = OrderTableFixtures.createWithGuests(null, 2);
        final OrderTable orderTable = orderTableDao.save(orderTable3);

        final TableGroup newTableGroup = TableGroupFixtures.createWithOrderTables(orderTable,
                alreadyGroupedOrderTable1);

        // when, then
        assertThatThrownBy(() -> tableGroupService.create(newTableGroup))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("테이블 단체를 해제한다")
    void ungroup() {
        // given
        final TableGroup tableGroup = TableGroupFixtures.create();
        final TableGroup alreadyGroupedTable = tableGroupDao.save(tableGroup);

        final OrderTable orderTable1 = OrderTableFixtures.createWithGuests(alreadyGroupedTable.getId(), 2);
        final OrderTable savedOrderTable1 = orderTableDao.save(orderTable1);

        final OrderTable orderTable2 = OrderTableFixtures.createWithGuests(alreadyGroupedTable.getId(), 2);
        final OrderTable savedOrderTable2 = orderTableDao.save(orderTable2);

        final Order order = OrderFixtures.COMPLETION_ORDER.createWithOrderTableId(savedOrderTable1.getId());
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
    @ValueSource(strings = {"COOKING_ORDER", "MEAL_ORDER"})
    @DisplayName("주문의 상태가 COOKING, MEAL인 경우 테이블 단체를 해제하면 예외가 발생한다")
    void ungroupExceptionNotCompletionOrder(final OrderFixtures orderFixtures) {
        // given
        final TableGroup tableGroup = TableGroupFixtures.create();
        final TableGroup alreadyGroupedTable = tableGroupDao.save(tableGroup);

        final OrderTable orderTable1 = OrderTableFixtures.createWithGuests(alreadyGroupedTable.getId(), 2);
        final OrderTable savedOrderTable1 = orderTableDao.save(orderTable1);

        final OrderTable orderTable2 = OrderTableFixtures.createWithGuests(alreadyGroupedTable.getId(), 2);
        orderTableDao.save(orderTable2);

        final Order order = orderFixtures.createWithOrderTableId(savedOrderTable1.getId());
        orderDao.save(order);

        // when, then
        assertThatThrownBy(() -> tableGroupService.ungroup(alreadyGroupedTable.getId()))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }
}
