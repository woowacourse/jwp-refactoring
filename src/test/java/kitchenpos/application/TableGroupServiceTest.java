package kitchenpos.application;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@DisplayName("TableGroupService를 테스트한다.")
@SpringBootTest
class TableGroupServiceTest {

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private TableService tableService;

    @Autowired
    private TableGroupDao tableGroupDao;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private OrderDao orderDao;

    @BeforeEach
    void setUp() {
        tableGroupDao.deleteAll();
    }

    @DisplayName("단체 지정을 등록한다.")
    @Test
    public void create() {
        //given
        OrderTable savedOrderTable1 = tableService.create(new OrderTable(null, null, 10, true));
        OrderTable savedOrderTable2 = tableService.create(new OrderTable(null, null, 20, true));
        TableGroup tableGroup = new TableGroup(null, null, Arrays.asList(savedOrderTable1, savedOrderTable2));

        //when
        TableGroup result = tableGroupService.create(tableGroup);

        //then
        List<OrderTable> resultOrderTables = result.getOrderTables();
        assertThat(resultOrderTables).hasSize(2);
        OrderTable resultOrderTable1 = resultOrderTables.get(0);
        OrderTable resultOrderTable2 = resultOrderTables.get(1);
        assertThat(resultOrderTables).hasSize(2);
        assertThat(resultOrderTable1.getNumberOfGuests()).isEqualTo(savedOrderTable1.getNumberOfGuests());
        assertThat(resultOrderTable2.getNumberOfGuests()).isEqualTo(savedOrderTable2.getNumberOfGuests());
    }

    @DisplayName("단체 지정 등록시 주문 테이블이 존재하지 않으면 예외를 던진다.")
    @Test
    void create_not_exist_order_table_exception() {
        OrderTable orderTable1 = new OrderTable(null, null, 10, true);
        OrderTable orderTable2 = new OrderTable(null, null, 20, true);
        TableGroup tableGroup = new TableGroup(null, null, Arrays.asList(orderTable1, orderTable2));

        assertThatThrownBy(() -> tableGroupService.create(tableGroup)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정 등록시 주문 테이블 개수가 2개 미만이라면 예외를 던진다.")
    @Test
    void create_order_table_count_exception() {
        OrderTable savedOrderTable1 = tableService.create(new OrderTable(null, null, 10, true));
        TableGroup tableGroup = new TableGroup(null, null, Arrays.asList(savedOrderTable1));

        assertThatThrownBy(() -> tableGroupService.create(tableGroup)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정 등록시 주문 테이블이 비어있지 않으면 예외를 던진다.")
    @Test
    void create_order_table_not_empty_exception() {
        OrderTable savedOrderTable1 = tableService.create(new OrderTable(null, null, 10, false));
        OrderTable savedOrderTable2 = tableService.create(new OrderTable(null, null, 20, true));
        TableGroup tableGroup = new TableGroup(null, null, Arrays.asList(savedOrderTable1, savedOrderTable2));

        assertThatThrownBy(() -> tableGroupService.create(tableGroup)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정 등록시 이미 단체 지정된 주문 테이블이 존재하면 예외를 던진다.")
    @Test
    void create_order_table_grouping_exception() {
        OrderTable savedOrderTable1 = tableService.create(new OrderTable(null, null, 10, true));
        OrderTable savedOrderTable2 = tableService.create(new OrderTable(null, null, 20, true));
        TableGroup tableGroup = new TableGroup(null, null, Arrays.asList(savedOrderTable1, savedOrderTable2));
        tableGroupService.create(tableGroup);

        assertThatThrownBy(() -> tableGroupService.create(tableGroup)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정을 해제한다.")
    @Test
    void ungroup() {
        //given
        OrderTable savedOrderTable1 = tableService.create(new OrderTable(null, null, 10, true));
        OrderTable savedOrderTable2 = tableService.create(new OrderTable(null, null, 20, true));
        TableGroup tableGroup = new TableGroup(null, null, Arrays.asList(savedOrderTable1, savedOrderTable2));
        TableGroup savedTableGroup = tableGroupService.create(tableGroup);

        //when
        tableGroupService.ungroup(savedTableGroup.getId());

        //then
        List<OrderTable> results = orderTableDao.findAllByTableGroupId(savedTableGroup.getId());
        assertThat(results).isEmpty();
    }

    @DisplayName("단체 지정 해제시 주문 테이블의 주문 상태가 '조리'라면 예외를 던진다.")
    @Test
    void ungroup_order_status_cooking_exception() {
        OrderTable savedOrderTable1 = tableService.create(new OrderTable(null, null, 10, true));
        OrderTable savedOrderTable2 = tableService.create(new OrderTable(null, null, 20, true));
        TableGroup tableGroup = new TableGroup(null, null, Arrays.asList(savedOrderTable1, savedOrderTable2));
        TableGroup savedTableGroup = tableGroupService.create(tableGroup);
        Order order = new Order(null, savedOrderTable1.getId(), OrderStatus.COOKING.toString(), LocalDateTime.now(), null);
        orderDao.save(order);

        assertThatThrownBy(() -> tableGroupService.ungroup(savedTableGroup.getId())).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정 해제시 주문 테이블의 주문 상태가 '식사'라면 예외를 던진다.")
    @Test
    void ungroup_order_status_meal_exception() {
        OrderTable savedOrderTable1 = tableService.create(new OrderTable(null, null, 10, true));
        OrderTable savedOrderTable2 = tableService.create(new OrderTable(null, null, 20, true));
        TableGroup tableGroup = new TableGroup(null, null, Arrays.asList(savedOrderTable1, savedOrderTable2));
        TableGroup savedTableGroup = tableGroupService.create(tableGroup);
        Order order = new Order(null, savedOrderTable1.getId(), OrderStatus.MEAL.toString(), LocalDateTime.now(), null);
        orderDao.save(order);

        assertThatThrownBy(() -> tableGroupService.ungroup(savedTableGroup.getId())).isInstanceOf(IllegalArgumentException.class);
    }
}