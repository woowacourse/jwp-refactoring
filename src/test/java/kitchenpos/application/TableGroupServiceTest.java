package kitchenpos.application;

import static kitchenpos.fixture.OrderTableFixture.두명인_테이블;
import static kitchenpos.fixture.OrderTableFixture.주문가능_테이블;
import static kitchenpos.fixture.OrderTableFixture.한명인_테이블;
import static kitchenpos.fixture.TableGroupFixture.주문상태_완료된_두번째테이블그룹;
import static kitchenpos.fixture.TableGroupFixture.주문상태_안료되지_않은_첫번째테이블그룹;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.fixture.OrderFixture;
import kitchenpos.fixture.OrderTableFixture;
import kitchenpos.fixture.TableGroupFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TableGroupServiceTest {

    private OrderDao orderDao;
    private OrderTableDao orderTableDao;
    private TableGroupDao tableGroupDao;

    private TableGroupService tableGroupService;

    @BeforeEach
    void setUp() {
        orderDao = OrderFixture.setUp().getOrderDao();
        orderTableDao = OrderTableFixture.setUp().getOrderTableDao();
        tableGroupDao = TableGroupFixture.setUp().getTableGroupDao();
        tableGroupService = new TableGroupService(orderDao, orderTableDao, tableGroupDao);
    }

    @Test
    @DisplayName("테이블 그룹을 생성한다.")
    void createTableGroup() {
        final OrderTable orderTable1 = orderTableDao.save(OrderTableFixture.createOrderTableByEmpty(true));
        final OrderTable orderTable2 = orderTableDao.save(OrderTableFixture.createOrderTableByEmpty(true));

        final TableGroup tableGroup = TableGroupFixture.createTableGroup(List.of(orderTable1, orderTable2));
        final TableGroup actual = tableGroupService.create(tableGroup);

        for (OrderTable orderTable : actual.getOrderTables()) {
            assertThat(actual.getId()).isEqualTo(orderTable.getTableGroupId());
        }
    }

    @Test
    @DisplayName("주문 그룹이 비어있으면 예외 발생")
    void whenOrderTableIsEmpty() {
        final TableGroup tableGroup = TableGroupFixture.createTableGroup(new ArrayList<>());

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 그룹이 2개 미만이면 예외 발생")
    void whenOrderTableIsUnderTwo() {
        final OrderTable orderTable = orderTableDao.findById(한명인_테이블)
                .orElseThrow();
        final TableGroup tableGroup = TableGroupFixture.createTableGroup(Collections.singletonList(orderTable));

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 테이블이 빈 테이블이 아닐 경우 예외 발생")
    void whenOrderTableIsNotEmptyTable() {
        final OrderTable orderTable1 = orderTableDao.save(OrderTableFixture.createOrderTableByEmpty(false));
        final OrderTable orderTable2 = orderTableDao.save(OrderTableFixture.createOrderTableByEmpty(true));

        final TableGroup tableGroup = TableGroupFixture.createTableGroup(List.of(orderTable1, orderTable2));

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 테이블의 상태가 주문 테이블로 변경된다.")
    void createTableGroupIsChangeOrderTableStatus() {
        final List<OrderTable> orderTables = orderTableDao.findAllByIdIn(List.of(한명인_테이블, 두명인_테이블));
        for (final OrderTable orderTable : orderTables) {
            orderTable.setTableGroupId(null);
            orderTable.setEmpty(true);
        }
        final TableGroup tableGroup = TableGroupFixture.createTableGroup(orderTables);

        final TableGroup actual = tableGroupService.create(tableGroup);
        final List<OrderTable> actualOrderTables = actual.getOrderTables();

        assertAll(
                () -> assertThat(actualOrderTables.get(0).isEmpty()).isFalse(),
                () -> assertThat(actualOrderTables.get(1).isEmpty()).isFalse()
        );
    }

    @Test
    @DisplayName("각 주문 테이블이 테이블 그룹에 포함되지 않을 경우 예외 발생")
    void whenOrderTableIsNotIncludeInTableGroup() {
        final OrderTable orderTable1 = orderTableDao.findById(두명인_테이블)
                .orElseThrow();
        final OrderTable orderTable2 = OrderTableFixture.createEmptyTable();
        final TableGroup tableGroup = TableGroupFixture.createTableGroup(List.of(orderTable1, orderTable2));

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("각 주문 테이블이 기존 테이블 그룹에 포함되어 있는 경우 예외 발생")
    void whenOrderTableIsIncludeInExistTableGroup() {
        final TableGroup tableGroup = tableGroupDao.findById(주문상태_안료되지_않은_첫번째테이블그룹)
                .orElseThrow();

        final TableGroup includeTableGroup = TableGroupFixture.createTableGroup(tableGroup.getOrderTables());


        assertThatThrownBy(() -> tableGroupService.create(includeTableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("테이블 그룹을 해제한다.")
    void ungroup() {
        final TableGroup tableGroup = tableGroupDao.findById(주문상태_완료된_두번째테이블그룹)
                .orElseThrow();
        final List<Long> orderTableIds = orderTableDao.findAllByTableGroupId(tableGroup.getId())
                .stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        tableGroupService.ungroup(tableGroup.getId());
        final List<OrderTable> actualOrderTables = orderTableDao.findAllByIdIn(orderTableIds);

        assertAll(
                () -> assertThat(actualOrderTables.get(0).getTableGroupId()).isNull(),
                () -> assertThat(actualOrderTables.get(0).isEmpty()).isFalse(),
                () -> assertThat(actualOrderTables.get(1).getTableGroupId()).isNull(),
                () -> assertThat(actualOrderTables.get(1).isEmpty()).isFalse()
        );
    }

    @Test
    @DisplayName("각 주문 테이블의 주문 상태가 완료상태가 아니면 예외 발생")
    void whenIsNotCompletion() {
        final TableGroup tableGroup = tableGroupDao.findById(주문상태_안료되지_않은_첫번째테이블그룹)
                .orElseThrow();

        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroup.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
