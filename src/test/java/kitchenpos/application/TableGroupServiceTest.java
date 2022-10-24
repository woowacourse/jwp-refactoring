package kitchenpos.application;

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
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
@SpringBootTest
class TableGroupServiceTest {

    private static final int ORDER_TABLE_COUNT_LIMIT = 2;

    private final TableGroupService tableGroupService;
    private final OrderDao orderDao;
    private final OrderTableDao orderTableDao;
    private final TableGroupDao tableGroupDao;

    @Autowired
    public TableGroupServiceTest(final TableGroupService tableGroupService, final OrderDao orderDao,
                                 final OrderTableDao orderTableDao, final TableGroupDao tableGroupDao) {
        this.tableGroupService = tableGroupService;
        this.orderDao = orderDao;
        this.orderTableDao = orderTableDao;
        this.tableGroupDao = tableGroupDao;
    }

    @DisplayName("단체 지정을 한다")
    @Test
    void create() {
        final var expected = new TableGroup(LocalDateTime.now(), saveOrderTableAsTimes(ORDER_TABLE_COUNT_LIMIT));

        final var actual = tableGroupService.create(expected);

        final var tableGroupId = actual.getId();
        final var orderTables = orderTableDao.findAllByTableGroupId(tableGroupId);
        actual.setOrderTables(orderTables);

        assertThat(actual.getId()).isPositive();
        assertThat(actual.getCreatedDate()).isEqualToIgnoringNanos(expected.getCreatedDate());
        assertOrderTablesAssigned(tableGroupId, actual.getOrderTables(), expected.getOrderTables());
    }

    private void assertOrderTablesAssigned(final long tableGroupId, final List<OrderTable> actualOrderTables,
                                           final List<OrderTable> expectedOrderTables) {
        final var expectedOrderTableSize = expectedOrderTables.size();
        assertThat(actualOrderTables).hasSize(expectedOrderTableSize);

        for (int i = 0; i < expectedOrderTableSize; i++) {
            final var actual = actualOrderTables.get(i);
            final var expected = expectedOrderTables.get(i);

            assertThat(actual.getTableGroupId()).isEqualTo(tableGroupId);
            assertThat(actual.getNumberOfGuests()).isEqualTo(expected.getNumberOfGuests());
            assertThat(actual.isEmpty()).isFalse();
        }
    }

    private List<OrderTable> saveOrderTables(final OrderTable... orderTables) {
        return Stream.of(orderTables)
                .map(orderTableDao::save)
                .collect(Collectors.toUnmodifiableList());
    }

    @DisplayName("2개 이상의 주문 테이블을 지정해야만 단체 지정을 할 수 있다")
    @ParameterizedTest
    @ValueSource(ints = {0, 1})
    void createWithEmptyOrSingleOrderTable(final int orderTableCount) {
        final var tableGroup = new TableGroup(LocalDateTime.now(), saveOrderTableAsTimes(orderTableCount));

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("2개 이상의 주문 테이블을 지정해야 합니다.");
    }

    private List<OrderTable> saveOrderTableAsTimes(final int times) {
        return Collections.nCopies(times, new OrderTable(null, 1, true))
                .stream()
                .map(orderTableDao::save)
                .collect(Collectors.toUnmodifiableList());
    }

    @DisplayName("존재하는 주문 테이블이어야만 단체 지정을 할 수 있다")
    @Test
    void createWithUnsavedOrderTables() {
        final var tableGroup = new TableGroup(LocalDateTime.now(), List.of(
                new OrderTable(null, 1, true),
                new OrderTable(null, 1, true)));

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 주문 테이블이 있습니다.");
    }

    @DisplayName("비어있는 주문 테이블이어야만 단체 지정을 할 수 있다")
    @Test
    void createWithNonEmptyOrderTable() {
        final var tableGroup = new TableGroup(LocalDateTime.now(), saveOrderTables(
                new OrderTable(null, 1, false),
                new OrderTable(null, 1, true)));

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("비어있지 않은 주문 테이블이 존재합니다.");
    }

    @DisplayName("단체 지정되지 않은 주문 테이블이어야만 단체 지정을 할 수 있다")
    @Test
    void createWithAlreadyGroupAssignedOrderTable() {
        final var tableGroup = new TableGroup(LocalDateTime.now(), saveOrderTables(
                new OrderTable(1L, 1, false),
                new OrderTable(null, 1, true)));

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("비어있지 않은 주문 테이블이 존재합니다.");
    }

    @DisplayName("단체 지정을 해제한다")
    @Test
    void ungroup() {
        final var expected = saveOrderTableAsTimes(ORDER_TABLE_COUNT_LIMIT);
        final var savedTableGroup = tableGroupDao.save(new TableGroup(LocalDateTime.now(), expected));

        final var savedOrderTableIds = expected.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toUnmodifiableList());

        savedOrderTableIds.forEach(orderTableId -> orderDao.save(
                new Order(orderTableId, OrderStatus.COMPLETION.name(), LocalDateTime.now(), Collections.emptyList()))
        );

        tableGroupService.ungroup(savedTableGroup.getId());

        final var actual = orderTableDao.findAllByIdIn(savedOrderTableIds);
        assertOrderTablesUnassigned(actual, expected);
    }

    private void assertOrderTablesUnassigned(final List<OrderTable> actualOrderTables,
                                             final List<OrderTable> expectedOrderTables) {
        final var expectedOrderTableSize = expectedOrderTables.size();
        assertThat(actualOrderTables).hasSize(expectedOrderTableSize);

        for (int i = 0; i < expectedOrderTableSize; i++) {
            final var actual = actualOrderTables.get(i);
            final var expected = expectedOrderTables.get(i);

            assertThat(actual.getTableGroupId()).isNull();
            assertThat(actual.getNumberOfGuests()).isEqualTo(expected.getNumberOfGuests());
            assertThat(actual.isEmpty()).isTrue();
        }
    }

    @DisplayName("지정된 주문 테이블의 모든 계산이 완료되어야만 단체 지정을 해제할 수 있다")
    @ParameterizedTest
    @ValueSource(strings = {"COOKING", "MEAL"})
    void ungroupWithUnreadyOrderTable(final String orderStatus) {
        final var savedOrderTables = saveOrderTableAsTimes(ORDER_TABLE_COUNT_LIMIT);
        final var savedTableGroup = tableGroupService.create(new TableGroup(LocalDateTime.now(), savedOrderTables));

        final var firstOrderTable = savedOrderTables.get(0);
        orderDao.save(new Order(firstOrderTable.getId(), orderStatus, LocalDateTime.now(), Collections.emptyList()));

        final var tableGroupId = savedTableGroup.getId();
        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroupId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("계산이 완료되지 않은 테이블이 존재합니다.");
    }
}