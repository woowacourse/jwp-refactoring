package kitchenpos.application;

import static kitchenpos.fixture.OrderTableFixture.generateOrderTable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.ArrayList;
import java.util.List;
import kitchenpos.dao.FakeOrderDao;
import kitchenpos.dao.FakeOrderTableDao;
import kitchenpos.dao.FakeTableGroupDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TableGroupServiceTest {

    private TableGroupService tableGroupService;

    private OrderDao orderDao;
    private OrderTableDao orderTableDao;
    private TableGroupDao tableGroupDao;

    @BeforeEach
    void beforeEach() {
        this.orderDao = new FakeOrderDao();
        this.orderTableDao = new FakeOrderTableDao();
        this.tableGroupDao = new FakeTableGroupDao();
        this.tableGroupService = new TableGroupService(orderDao, orderTableDao, tableGroupDao);
    }

    @Test
    @DisplayName("테이블 그룹을 생성한다.")
    void create() {
        // given
        TableGroup tableGroup = new TableGroup();
        List<OrderTable> orderTables = new ArrayList<>();

        OrderTable orderTable1 = generateOrderTable(null, 0, true);
        OrderTable orderTable2 = generateOrderTable(null, 0, true);

        orderTables.add(orderTableDao.save(orderTable1));
        orderTables.add(orderTableDao.save(orderTable2));

        tableGroup.setOrderTables(orderTables);
        // when
        TableGroup nweTableGroup = tableGroupService.create(tableGroup);

        // then
        assertAll(
                () -> assertThat(nweTableGroup.getId()).isNotNull(),
                () -> assertThat(nweTableGroup.getCreatedDate()).isNotNull(),
                () -> assertThat(nweTableGroup.getOrderTables().size()).isEqualTo(2)
        );
    }

    @Test
    @DisplayName("테이블 그룹을 생성 시 주문테이블이 비어있다면 예외를 반환한다.")
    void create_WhenEmptyOrderTable() {
        // given
        TableGroup tableGroup = new TableGroup();
        List<OrderTable> orderTables = new ArrayList<>();

        tableGroup.setOrderTables(orderTables);
        // when & then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("테이블 그룹을 생성 시 주문테이블이 1개 이하라면 예외를 반환한다.")
    void create_WhenUnderOneOrderTable() {
        // given
        TableGroup tableGroup = new TableGroup();
        List<OrderTable> orderTables = new ArrayList<>();

        OrderTable orderTable1 = generateOrderTable(null, 0, true);

        orderTables.add(orderTableDao.save(orderTable1));

        tableGroup.setOrderTables(orderTables);
        // when & then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("테이블 그룹을 생성 시 입력된 주문 테이블 목록과 저장된 주문 테이블 목록이 다르다면 예외를 반환한다.")
    void create_WhenNotExistOrderTable() {
        // given
        TableGroup tableGroup = new TableGroup();
        List<OrderTable> orderTables = new ArrayList<>();

        OrderTable orderTable1 = generateOrderTable(null, 0, true);
        OrderTable orderTable2 = generateOrderTable(null, 0, true);

        orderTables.add(orderTableDao.save(orderTable1));
        orderTables.add(orderTable2);

        tableGroup.setOrderTables(orderTables);
        // when & then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 테이블 목록과 테이블 그룹을 분리한다.")
    void ungroup() {
        // when
        TableGroup tableGroup = new TableGroup();
        List<OrderTable> orderTables = new ArrayList<>();

        OrderTable orderTable1 = generateOrderTable(null, 0, true);
        OrderTable orderTable2 = generateOrderTable(null, 0, true);

        orderTables.add(orderTableDao.save(orderTable1));
        orderTables.add(orderTableDao.save(orderTable2));

        tableGroup.setOrderTables(orderTables);

        TableGroup newTableGroup = tableGroupService.create(tableGroup);

        // when
        tableGroupService.ungroup(newTableGroup.getId());
        // then
        List<OrderTable> orderTablesInTableGroup = orderTableDao.findAllByTableGroupId(newTableGroup.getId());
        assertThat(orderTablesInTableGroup.size()).isEqualTo(0);
    }
}
