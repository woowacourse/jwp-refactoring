package kitchenpos.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestConstructor.AutowireMode;

import kitchenpos.TestUtils;
import kitchenpos.application.TableGroupService;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

@SpringBootTest
@TestConstructor(autowireMode = AutowireMode.ALL)
public class TableGroupServiceTest {

    private final TableGroupService tableGroupService;
    private final OrderTableDao orderTableDao;

    List<OrderTable> orderTables;

    public TableGroupServiceTest(TableGroupService tableGroupService, OrderTableDao orderTableDao) {
        this.tableGroupService = tableGroupService;
        this.orderTableDao = orderTableDao;
    }

    @BeforeEach
    void setUp() {
        orderTables = TestUtils.of(
                orderTableDao.save(new OrderTable(100, true)),
                orderTableDao.save(new OrderTable(100, true))
        );
    }


    @DisplayName("주문 테이블이 존재하지 않는 경우 예외가 발생한다.")
    @Test
    public void createWithEmptyOrderTable() {
        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(TestUtils.of());

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블의 개수가 1개인 경우 예외가 발생한다.")
    @Test
    public void createWithOneElementOrderTable() {
        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(TestUtils.of(new OrderTable(50, true)));

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블이 사전에 저장되어 있지 않은 경우 예외가 발생한다.")
    public void createWithNoSavedOrderTable() {
        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(TestUtils.of(
                new OrderTable(50, true),
                new OrderTable(50, true)
        ));

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블이 비어있지 않다면 예외가 발생한다.")
    @Test
    public void createWithNotEmptyOrderTable() {
        TableGroup tableGroup = new TableGroup();
        List<OrderTable> tempOrderTables = new ArrayList<>(orderTables);
        tempOrderTables.add(orderTableDao.save(new OrderTable(100, false)));
        tableGroup.setOrderTables(tempOrderTables);

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블중 이미 다른 테이블 그룹에 속해있는 주문 테이블이 있다면 예외가 발생한다.")
    @Test
    public void createWithAlreadyBelongAnotherTableGroup() {
        TableGroup tableGroup = new TableGroup();
        List<OrderTable> tempOrderTables = new ArrayList<>(orderTables);
        orderTables.get(0).setTableGroupId(1L);
        tableGroup.setOrderTables(tempOrderTables);

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("정상적으로 저장된 경우 ID가 발급된다.")
    @Test
    public void create() {
        TableGroup tableGroup = new TableGroup();
        List<OrderTable> tempOrderTables = new ArrayList<>(orderTables);
        tableGroup.setOrderTables(tempOrderTables);

        TableGroup savedTableGroup = tableGroupService.create(tableGroup);
        assertThat(savedTableGroup.getId()).isNotNull();
    }
}
