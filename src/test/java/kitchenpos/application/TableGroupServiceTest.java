package kitchenpos.application;

import static kitchenpos.application.fixture.OrderTableFixture.세명있는_테이블;
import static kitchenpos.application.fixture.OrderTableFixture.한명있는_테이블;
import static kitchenpos.application.fixture.TableGroupFixture.첫번째테이블그룹;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.fixture.OrderFixture;
import kitchenpos.application.fixture.OrderTableFixture;
import kitchenpos.application.fixture.TableGroupFixture;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("단체 지정 기능에서")
class TableGroupServiceTest {

    private OrderDao orderDao;
    private OrderTableDao orderTableDao;
    private TableGroupDao tableGroupDao;
    private TableGroupService tableGroupService;

    @BeforeEach
    void setUp() {
        orderTableDao = OrderTableFixture.createFixture().getOrderTableDao();
        orderDao = OrderFixture.createFixture().getOrderDao();
        tableGroupDao = TableGroupFixture.createFixture().getTableGroupDao();

        tableGroupService = new TableGroupService(orderDao, orderTableDao, tableGroupDao);
    }

    @Nested
    @DisplayName("단체 지정을 생성하면")
    class TableGroupTest {

        @DisplayName("주문 테이블이 2개 이하인 단체 지정은 생설할 수 없다.")
        @Test
        void whenOrderLineItemsIsEmpty() {
            //given
            OrderTable orderTable = new OrderTable();

            TableGroup tableGroup = new TableGroup();
            tableGroup.setOrderTables(Collections.singletonList(orderTable));

            //when & then
            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("주문 테이블에 그룹아이디가 있으면 실패한다.")
        @Test
        void whenTableGroupNonNull() {
            //given
            List<OrderTable> orderTables = orderTableDao
                .findAllByIdIn(Arrays.asList(한명있는_테이블, 세명있는_테이블));

            TableGroup tableGroup = new TableGroup();
            tableGroup.setOrderTables(orderTables);

            //when & then
            for (OrderTable orderTable : orderTables) {
                assertThat(orderTable.getTableGroupId()).isNotNull();
            }
            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("주문 테이블이 비어있지 않으면 실패한다.")
        @Test
        void whenOrderTableNotEmpty() {
            //given
            List<OrderTable> orderTables = orderTableDao
                .findAllByIdIn(Arrays.asList(한명있는_테이블, 세명있는_테이블));
            orderTables.forEach(orderTable -> orderTable.setTableGroupId(null));
            orderTables.forEach(orderTable -> orderTable.setEmpty(false));

            TableGroup tableGroup = new TableGroup();
            tableGroup.setOrderTables(orderTables);

            //when & then
            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("성공한다.")
        @Test
        void createTest() {
            //given
            List<OrderTable> orderTables = orderTableDao
                .findAllByIdIn(Arrays.asList(한명있는_테이블, 세명있는_테이블));
            orderTables.forEach(orderTable -> orderTable.setTableGroupId(null));
            orderTables.forEach(orderTable -> orderTable.setEmpty(true));

            TableGroup tableGroup = new TableGroup();
            tableGroup.setOrderTables(orderTables);

            //when
            TableGroup persistedTableGroup = tableGroupService.create(tableGroup);

            //then
            for (OrderTable orderTable : persistedTableGroup.getOrderTables()) {
                assertThat(orderTable.getTableGroupId()).isEqualTo(persistedTableGroup.getId());
            }
        }
    }

    @DisplayName("단체 지정을 해제하면")
    @Nested
    class TableUngroupTest {

        @DisplayName("단체지정에 속한 테이블들에 주문이 남아있으면 예외")
        @Test
        void whenExistTest() {
            //given
            TableGroup tableGroup = tableGroupDao.findById(첫번째테이블그룹).get();

            List<OrderTable> orderTables = orderTableDao
                .findAllByTableGroupId(tableGroup.getId());

            List<Long> tableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

            List<Order> tableGroupOrders = orderDao.findAll().stream()
                .filter(order -> tableIds.contains(order.getOrderTableId()))
                .collect(Collectors.toList());

            tableGroupOrders.forEach(order -> order.setOrderStatus(OrderStatus.MEAL.name()));

            //when & then
            assertThatThrownBy(() -> tableGroupService.ungroup(첫번째테이블그룹))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("성공한다.")
        @Test
        void ungroupTest() {
            //given
            TableGroup tableGroup = tableGroupDao.findById(첫번째테이블그룹).get();

            List<OrderTable> orderTables = orderTableDao
                .findAllByTableGroupId(tableGroup.getId());

            List<Long> tableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

            List<Order> tableGroupOrders = orderDao.findAll().stream()
                .filter(order -> tableIds.contains(order.getOrderTableId()))
                .collect(Collectors.toList());

            tableGroupOrders.forEach(order -> order.setOrderStatus(OrderStatus.COMPLETION.name()));
            //when
            tableGroupService.ungroup(tableGroup.getId());

            //then
            List<OrderTable> tableGroupOrderTables = orderTableDao
                .findAllByTableGroupId(tableGroup.getId());
            assertThat(tableGroupOrderTables.size()).isEqualTo(0);
        }
    }
}