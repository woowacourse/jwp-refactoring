package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

@SpringBootTest
@Transactional
class TableGroupServiceTest {

    @Autowired
    private TableService tableService;

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderTableDao orderTableDao;

    private TableGroup tableGroup;
    private OrderTable orderTable1;
    private OrderTable orderTable2;

    @BeforeEach
    void setUp() {
        tableGroup = new TableGroup();
        orderTable1 = generateEmptyTable();
        orderTable2 = generateEmptyTable();
    }

    @Test
    @DisplayName("orderTable을 생성한다.")
    void create() {
        //given
        tableGroup.setOrderTables(generateTableList(orderTable1, orderTable2));

        //when
        TableGroup actual = tableGroupService.create(tableGroup);

        //then
        assertAll(
            () -> assertThat(actual.getOrderTables().get(0).getTableGroupId()).isEqualTo(actual.getId()),
            () -> assertThat(actual.getOrderTables().get(0).isEmpty()).isFalse(),
            () -> assertThat(actual.getOrderTables().get(1).getTableGroupId()).isEqualTo(actual.getId()),
            () -> assertThat(actual.getOrderTables().get(1).isEmpty()).isFalse()
        );
    }

    @Test
    @DisplayName("테이블 개수가 2개 미만인 경우 예외를 발생시킨다.")
    void createWithTableAmountError() {
        //given
        tableGroup.setOrderTables(generateTableList(orderTable1));

        //when, then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("존재하지 않는 테이블이 포함된 경우 예외를 발생시킨다.")
    void createNotExistTableError() {
        //given
        OrderTable tableNotExist = new OrderTable();
        tableNotExist.setId(999999999L);
        tableGroup.setOrderTables(generateTableList(orderTable1, tableNotExist));

        //when, then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("테이블을 분리시킨다.")
    void ungroup() {
        //given
        tableGroup.setOrderTables(generateTableList(orderTable1, orderTable2));
        TableGroup savedTableGroup = tableGroupService.create(this.tableGroup);

        //when
        tableGroupService.ungroup(savedTableGroup.getId());
        OrderTable orderTable1 = orderTableDao.findById(this.orderTable1.getId()).get();
        OrderTable orderTable2 = orderTableDao.findById(this.orderTable2.getId()).get();

        //then
        assertAll(
            () -> assertThat(orderTable1.getTableGroupId()).isNull(),
            () -> assertThat(orderTable1.isEmpty()).isFalse(),
            () -> assertThat(orderTable2.getTableGroupId()).isNull(),
            () -> assertThat(orderTable2.isEmpty()).isFalse()
        );
    }

    @Test
    @DisplayName("COOKING이나 MEAL 상태인 테이블이 포함된 테이블을 분리시킬 경우 예외를 발생시킨다.")
    void ungroupWithInvalidStatusError() {
        //given
        tableGroup.setOrderTables(generateTableList(orderTable1, orderTable2));
        Long savedTableGroupId = tableGroupService.create(tableGroup).getId();

        OrderLineItem orderLineItem = new OrderLineItem(1L, 1L, 1);
        List<OrderLineItem> orderLineItems = new ArrayList<>();
        orderLineItems.add(orderLineItem);

        Order order = new Order(orderTable1.getId(), "COOKING", LocalDateTime.now(), orderLineItems);
        orderService.create(order);

        assertThatThrownBy(() -> tableGroupService.ungroup(savedTableGroupId))
            .isInstanceOf(IllegalArgumentException.class);
    }

    private OrderTable generateEmptyTable() {
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(true);
        return tableService.create(orderTable);
    }

    private List<OrderTable> generateTableList(OrderTable... orderTables) {
        return Arrays.stream(orderTables)
            .collect(Collectors.toList());
    }
}
