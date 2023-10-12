package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class TableGroupServiceTest {

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private OrderDao orderDao;

    @DisplayName("주문 테이블이 2개 미만이면, 주문 테이블 그룹을 생성할 수 없다.")
    @Test
    void createFailTest_ByOrderTableCountIsLessThanTwo() {
        //given
        OrderTable orderTable = new OrderTable();
        OrderTable savedOrderTable = orderTableDao.save(orderTable);

        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(List.of(savedOrderTable));

        //when then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블이 존재하지 않으면, 주문 테이블 그룹을 생성할 수 없다.")
    @Test
    void createFailTest_ByOrderTableIsNotExists() {
        //given
        OrderTable orderTable1 = new OrderTable();
        orderTable1.setId(99L);

        OrderTable orderTable2 = new OrderTable();
        orderTable2.setId(100L);

        //when
        assertThat(orderTableDao.findById(99L)).isEmpty();
        assertThat(orderTableDao.findById(100L)).isEmpty();

        //then
        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(List.of(orderTable1, orderTable2));

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문이 가능한 테이블이 존재하면(Not Empty), 주문 테이블 그룹을 생성할 수 없다.")
    @Test
    void createFailTest_ByTableGroupContainsNotEmptyOrderTable() {
        //given
        OrderTable orderTable1 = new OrderTable();
        orderTable1.setEmpty(false);

        OrderTable orderTable2 = new OrderTable();
        orderTable2.setEmpty(true);

        //when
        OrderTable savedOrderTable1 = orderTableDao.save(orderTable1);
        OrderTable savedOrderTable2 = orderTableDao.save(orderTable2);

        //then
        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(List.of(savedOrderTable1, savedOrderTable2));

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("다른 테이블 그룹에 속해있는 주문 테이블이 있는 경우, 주문 테이블 그룹을 생성할 수 없다.")
    @Test
    void createFailTest_ByOrderTableIsAlreadyGrouped() {
        //given
        OrderTable orderTable1 = new OrderTable();
        orderTable1.setEmpty(true);
        OrderTable orderTable2 = new OrderTable();
        orderTable2.setEmpty(true);

        OrderTable savedOrderTable1 = orderTableDao.save(orderTable1);
        OrderTable savedOrderTable2 = orderTableDao.save(orderTable2);

        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(List.of(savedOrderTable1, savedOrderTable2));

        //when
        tableGroupService.create(tableGroup);

        OrderTable newOrderTable = new OrderTable();
        newOrderTable.setEmpty(true);

        OrderTable savedNewOrderTable = orderTableDao.save(newOrderTable);

        TableGroup newTableGroup = new TableGroup();
        newTableGroup.setOrderTables(List.of(savedOrderTable1, savedNewOrderTable));

        //then
        assertThatThrownBy(() -> tableGroupService.create(newTableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블 그룹을 생성하면, 생성 시점을 초기화 하고 주문 테이블은 해당 그룹에 속하며 각 주문 테이블은 주문 가능한 상태로 변한다.")
    @Test
    void createSuccessTest_InitializeRelatedData() {
        //given
        OrderTable orderTable1 = new OrderTable();
        orderTable1.setEmpty(true);
        OrderTable orderTable2 = new OrderTable();
        orderTable2.setEmpty(true);

        OrderTable savedOrderTable1 = orderTableDao.save(orderTable1);
        OrderTable savedOrderTable2 = orderTableDao.save(orderTable2);

        //when
        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(List.of(savedOrderTable1, savedOrderTable2));

        TableGroup savedTableGroup = tableGroupService.create(tableGroup);

        //then
        assertAll(
                () -> assertThat(savedTableGroup.getId()).isNotNull(),
                () -> assertThat(savedTableGroup.getCreatedDate()).isNotNull(),
                () -> assertThat(savedTableGroup.getOrderTables())
                        .extractingResultOf("getTableGroupId")
                        .containsExactly(savedTableGroup.getId(), savedTableGroup.getId()),
                () -> assertThat(savedTableGroup.getOrderTables())
                        .extractingResultOf("isEmpty")
                        .containsExactly(false, false)
        );
    }

    @ParameterizedTest(name = "완료(COMPLETION)되지 않은 상태의 테이블이 존재할 경우, 삭제할 수 없다.")
    @ValueSource(strings = {"COOKING", "MEAL"})
    void ungroupFailTest_ByTableOrderStatusIsNotCompletion(String status) {
        OrderTable orderTable1 = new OrderTable();
        orderTable1.setEmpty(true);
        OrderTable orderTable2 = new OrderTable();
        orderTable2.setEmpty(true);

        OrderTable savedOrderTable1 = orderTableDao.save(orderTable1);
        OrderTable savedOrderTable2 = orderTableDao.save(orderTable2);

        Order order = new Order();
        order.setOrderStatus(status);
        order.setOrderedTime(LocalDateTime.now());
        order.setOrderTableId(savedOrderTable1.getId());

        orderDao.save(order);

        //when
        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(List.of(savedOrderTable1, savedOrderTable2));

        Long savedTableGroupId = tableGroupService.create(tableGroup).getId();

        //then
        assertThatThrownBy(() -> tableGroupService.ungroup(savedTableGroupId))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("그룹을 해제하면, 각 주문 테이블은 해당 그룹에 속하지 않고, 주문이 가능한 상태(Not Empty)로 변한다.")
    @Test
    void ungroupSuccessTest_InitializeRelatedData() {
        OrderTable orderTable1 = new OrderTable();
        orderTable1.setEmpty(true);
        OrderTable orderTable2 = new OrderTable();
        orderTable2.setEmpty(true);

        OrderTable savedOrderTable1 = orderTableDao.save(orderTable1);
        OrderTable savedOrderTable2 = orderTableDao.save(orderTable2);

        Order order = new Order();
        order.setOrderStatus("COMPLETION");
        order.setOrderedTime(LocalDateTime.now());
        order.setOrderTableId(savedOrderTable1.getId());

        orderDao.save(order);

        //when
        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(List.of(savedOrderTable1, savedOrderTable2));

        Long savedTableGroupId = tableGroupService.create(tableGroup).getId();

        tableGroupService.ungroup(savedTableGroupId);

        //then
        assertThat(orderTableDao.findAllByTableGroupId(savedTableGroupId)).isEmpty();
        assertThat(orderTableDao.findAllByIdIn(List.of(savedOrderTable1.getId(), savedOrderTable2.getId())))
                .extractingResultOf("isEmpty")
                .containsExactly(false, false);
    }

}
