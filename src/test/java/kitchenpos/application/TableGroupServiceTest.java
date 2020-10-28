package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.utils.KitchenPosClassCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
@Sql("/truncate.sql")
@SpringBootTest
public class TableGroupServiceTest {
    private static final int 테이블_사람_4명 = 4;

    @Autowired
    private OrderDao orderDao;
    @Autowired
    private OrderTableDao orderTableDao;
    @Autowired
    private TableGroupService tableGroupService;

    private TableGroup tableGroup;

    @BeforeEach
    void setUp() {
        OrderTable orderTable1 = KitchenPosClassCreator.createOrderTable(테이블_사람_4명, true);
        OrderTable orderTable2 = KitchenPosClassCreator.createOrderTable(테이블_사람_4명, true);
        orderTable1 = orderTableDao.save(orderTable1);
        orderTable2 = orderTableDao.save(orderTable2);

        List<OrderTable> orderTables = Arrays.asList(orderTable1, orderTable2);

        tableGroup = KitchenPosClassCreator.createTableGroup(orderTables);
    }

    @DisplayName("TableGroup 생성이 올바르게 수행된다.")
    @Test
    void createTest() {
        TableGroup savedTableGroup = tableGroupService.create(tableGroup);

        assertThat(savedTableGroup.getOrderTables())
                .extracting("tableGroupId")
                .containsOnly(savedTableGroup.getId());
    }

    @DisplayName("예외 테스트 : TableGroup 생성 중 빈 테이블 목록을 보내면, 예외가 발생한다.")
    @Test
    void createWithEmptyTablesExceptionTest() {
        tableGroup.setOrderTables(Collections.emptyList());

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("예외 테스트 : TableGroup 생성 중 2개 미만의 테이블 목록을 보내면, 예외가 발생한다.")
    @Test
    void createWithLowTablesExceptionTest() {
        OrderTable orderTable = KitchenPosClassCreator.createOrderTable(테이블_사람_4명, true);
        orderTable = orderTableDao.save(orderTable);
        List<OrderTable> orderTables = Arrays.asList(orderTable);
        tableGroup.setOrderTables(orderTables);

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("예외 테스트 : TableGroup 생성 중 DB에 없는 테이블 목록을 보내면, 예외가 발생한다.")
    @Test
    void createWithDBNotFoundTablesExceptionTest() {
        OrderTable orderTable = KitchenPosClassCreator.createOrderTable(테이블_사람_4명, true);
        List<OrderTable> orderTables = new ArrayList<>(tableGroup.getOrderTables());
        orderTables.add(orderTable);
        tableGroup.setOrderTables(orderTables);

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("예외 테스트 : TableGroup 생성 중 비어있지 않은 테이블 목록을 보내면, 예외가 발생한다.")
    @Test
    void createWithNotEmptyTablesExceptionTest() {
        OrderTable orderTable = KitchenPosClassCreator.createOrderTable(테이블_사람_4명, false);
        orderTableDao.save(orderTable);
        List<OrderTable> orderTables = new ArrayList<>(tableGroup.getOrderTables());
        orderTables.add(orderTable);
        tableGroup.setOrderTables(orderTables);

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("예외 테스트 : TableGroup 생성 중 다른 그룹에 속해 있는 테이블 목록을 보내면, 예외가 발생한다.")
    @Test
    void createWithNotFreeTablesExceptionTest() {
        TableGroup savedTableGroup = tableGroupService.create(tableGroup);
        OrderTable orderTable = KitchenPosClassCreator.createOrderTable(테이블_사람_4명, false);
        orderTable.setTableGroupId(savedTableGroup.getId());
        orderTableDao.save(orderTable);

        List<OrderTable> orderTables = new ArrayList<>(tableGroup.getOrderTables());
        orderTables.add(orderTable);
        tableGroup.setOrderTables(orderTables);

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("TableGroup에 대한 그룹 해제가 올바르게 수행된다")
    @Test
    void ungroupTest() {
        TableGroup savedTableGroup = tableGroupService.create(tableGroup);

        List<OrderTable> orderTables = savedTableGroup.getOrderTables();
        List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        assertThat(orderTableDao.findAllByIdIn(orderTableIds))
                .extracting("tableGroupId")
                .containsOnly(savedTableGroup.getId());

        tableGroupService.ungroup(savedTableGroup.getId());

        assertThat(orderTableDao.findAllByIdIn(orderTableIds))
                .extracting("tableGroupId")
                .containsOnlyNulls();
    }

    @DisplayName("TableGroup에 대한 그룹 해제 중 주문 상태가 완료되지 않았다면 예외가 발생한다")
    @Test
    void ungroupWithStatusExceptionTest() {
        TableGroup savedTableGroup = tableGroupService.create(tableGroup);

        List<OrderTable> orderTables = savedTableGroup.getOrderTables();
        OrderTable orderTable = orderTables.get(0);
        Order order = new Order();
        order.setOrderStatus(OrderStatus.COOKING.name());
        order.setOrderTableId(orderTable.getId());
        order.setOrderedTime(LocalDateTime.now());
        orderDao.save(order);

        assertThatThrownBy(() -> tableGroupService.ungroup(savedTableGroup.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
