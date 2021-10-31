package kitchenpos.application;

import static kitchenpos.domain.OrderStatus.COMPLETION;
import static kitchenpos.domain.OrderStatus.COOKING;
import static kitchenpos.domain.OrderStatus.MEAL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@DisplayName("TableGroup Service 테스트")
@SpringBootTest
class TableGroupServiceTest {

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private TableGroupDao tableGroupDao;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private OrderDao orderDao;

    @DisplayName("TableGroup을 생성할 때")
    @Nested
    class CreateTableGroup {

        @DisplayName("TableGroup의 OrderTable의 개수가 2개 미만일 경우 예외가 발생한다.")
        @Test
        void orderTablesUnderTwoException() {
            // given
            TableGroup tableGroup = TableGroup을_생성한다();
            tableGroup.setOrderTables(Collections.singletonList(new OrderTable()));

            // when, then
            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isExactlyInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("TableGroup의 OrderTable들과 실제 저장된 OrderTable들이 일치하지 않을 경우 예외가 발생한다.")
        @Test
        void nonMatchOrderTablesException() {
            // given
            OrderTable orderTable1 = orderTableDao.save(OrderTable을_생성한다(1));
            OrderTable orderTable2 = orderTableDao.save(OrderTable을_생성한다(2));
            OrderTable orderTable3 = OrderTable을_생성한다(3);
            orderTable3.setId(-1L);

            TableGroup tableGroup = TableGroup을_생성한다();
            tableGroup.setOrderTables(Arrays.asList(orderTable1, orderTable2, orderTable3));

            // when, then
            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isExactlyInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("TableGroup의 OrderTable이 비어있는 상태가 아닐 경우 예외가 발생한다.")
        @Test
        void orderTableIsNotEmptyException() {
            // given
            OrderTable orderTable1 = orderTableDao.save(OrderTable을_생성한다(1, false));
            OrderTable orderTable2 = orderTableDao.save(OrderTable을_생성한다(2, false));
            OrderTable orderTable3 = orderTableDao.save(OrderTable을_생성한다(3, false));

            TableGroup tableGroup = TableGroup을_생성한다();
            tableGroup.setOrderTables(Arrays.asList(orderTable1, orderTable2, orderTable3));

            // when, then
            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isExactlyInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("TableGroup의 OrderTable에 이미 TableGroupId가 등록되어 있는 경우 예외가 발생한다.")
        @Test
        void alreadyMappingTableGroupException() {
            // given
            TableGroup savedTableGroup = tableGroupDao.save(TableGroup을_생성한다());
            OrderTable orderTable1 = orderTableDao.save(OrderTable을_생성한다(1, savedTableGroup.getId(), true));
            OrderTable orderTable2 = orderTableDao.save(OrderTable을_생성한다(2, savedTableGroup.getId(), true));
            OrderTable orderTable3 = orderTableDao.save(OrderTable을_생성한다(3, savedTableGroup.getId(), true));

            TableGroup tableGroup = TableGroup을_생성한다();
            tableGroup.setOrderTables(Arrays.asList(orderTable1, orderTable2, orderTable3));

            // when, then
            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isExactlyInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("TableGroup의 OrderTable이 정상적인 경우 TableGroup과 연결된다.")
        @Test
        void success() {
            // given
            OrderTable orderTable1 = orderTableDao.save(OrderTable을_생성한다(1, true));
            OrderTable orderTable2 = orderTableDao.save(OrderTable을_생성한다(2, true));
            OrderTable orderTable3 = orderTableDao.save(OrderTable을_생성한다(3, true));

            TableGroup tableGroup = TableGroup을_생성한다();
            tableGroup.setOrderTables(Arrays.asList(orderTable1, orderTable2, orderTable3));

            // when
            TableGroup savedTableGroup = tableGroupService.create(tableGroup);

            // then
            assertThat(savedTableGroup.getId()).isNotNull();
            for (OrderTable orderTable : savedTableGroup.getOrderTables()) {
                assertThat(orderTable.getTableGroupId()).isEqualTo(savedTableGroup.getId());
                assertThat(orderTable.isEmpty()).isFalse();
            }
        }
    }
    
    @DisplayName("TableGroup을 해제할 때")
    @Nested
    class Ungroup {
        
        @DisplayName("COOKING 중인 OrderTable이 포함된 Order가 존재하는 경우 예외가 발생한다.")
        @Test
        void existsByOrderTableIdInAndCOOKINGStatusInException() {
            // given
            OrderTable orderTable1 = orderTableDao.save(OrderTable을_생성한다(1, true));
            OrderTable orderTable2 = orderTableDao.save(OrderTable을_생성한다(2, true));

            TableGroup tableGroup = TableGroup을_생성한다();
            tableGroup.setOrderTables(Arrays.asList(orderTable1, orderTable2));
            TableGroup savedTableGroup = tableGroupService.create(tableGroup);

            Order order = orderDao.save(Order를_생성한다(orderTable1.getId(), COOKING));

            // when, then
            assertThatThrownBy(() -> tableGroupService.ungroup(savedTableGroup.getId()))
                .isExactlyInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("MEAL 중인 OrderTable이 포함된 Order가 존재하는 경우 예외가 발생한다.")
        @Test
        void existsByOrderTableIdInAndMEALStatusInException() {
            // given
            OrderTable orderTable1 = orderTableDao.save(OrderTable을_생성한다(1, true));
            OrderTable orderTable2 = orderTableDao.save(OrderTable을_생성한다(2, true));

            TableGroup tableGroup = TableGroup을_생성한다();
            tableGroup.setOrderTables(Arrays.asList(orderTable1, orderTable2));
            TableGroup savedTableGroup = tableGroupService.create(tableGroup);

            Order order = orderDao.save(Order를_생성한다(orderTable1.getId(), MEAL));

            // when, then
            assertThatThrownBy(() -> tableGroupService.ungroup(savedTableGroup.getId()))
                .isExactlyInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("COOKING, MEAL 중인 OrderTable이 포함된 Order가 존재하지 않는 경우 그룹 해제가 된다.")
        @Test
        void success() {
            // given
            OrderTable orderTable1 = orderTableDao.save(OrderTable을_생성한다(1, true));
            OrderTable orderTable2 = orderTableDao.save(OrderTable을_생성한다(2, true));

            TableGroup tableGroup = TableGroup을_생성한다();
            tableGroup.setOrderTables(Arrays.asList(orderTable1, orderTable2));
            TableGroup savedTableGroup = tableGroupService.create(tableGroup);

            Order order = orderDao.save(Order를_생성한다(orderTable1.getId(), COMPLETION));

            // when
            tableGroupService.ungroup(savedTableGroup.getId());
            List<OrderTable> ungroupedOrderTables = orderTableDao.findAllByTableGroupId(savedTableGroup.getId());

            // then
            for (OrderTable orderTable : ungroupedOrderTables) {
                assertThat(orderTable.getTableGroupId()).isNull();
                assertThat(orderTable.isEmpty()).isFalse();
            }
        }
    }

    private TableGroup TableGroup을_생성한다() {
        TableGroup tableGroup = new TableGroup();
        tableGroup.setCreatedDate(LocalDateTime.now());

        return tableGroup;
    }

    private OrderTable OrderTable을_생성한다(int numberOfGuests) {
        return OrderTable을_생성한다(numberOfGuests, null, false);
    }

    private OrderTable OrderTable을_생성한다(int numberOfGuests, boolean isEmpty) {
        return OrderTable을_생성한다(numberOfGuests, null, isEmpty);
    }

    private OrderTable OrderTable을_생성한다(int numberOfGuests, Long tableGroupId, boolean isEmpty) {
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(numberOfGuests);
        orderTable.setTableGroupId(tableGroupId);
        orderTable.setEmpty(isEmpty);

        return orderTable;
    }

    private Order Order를_생성한다(Long orderTableId, OrderStatus orderStatus) {
        Order order = new Order();
        order.setOrderTableId(orderTableId);
        order.setOrderStatus(orderStatus.name());
        order.setOrderedTime(LocalDateTime.now());

        return order;
    }
}