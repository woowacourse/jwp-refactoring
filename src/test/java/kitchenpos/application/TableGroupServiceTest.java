package kitchenpos.application;

import static kitchenpos.application.ServiceTestFixture.INVALID_GROUPID_ORDER_TABLE;
import static kitchenpos.application.ServiceTestFixture.INVALID_ORDER_TABLE;
import static kitchenpos.application.ServiceTestFixture.NOT_EMPTY_ORDER_TABLE;
import static kitchenpos.application.ServiceTestFixture.ORDER_TABLE1;
import static kitchenpos.application.ServiceTestFixture.ORDER_TABLE2;
import static kitchenpos.application.ServiceTestFixture.ORDER_TABLE3;
import static kitchenpos.application.ServiceTestFixture.ORDER_TABLES;
import static kitchenpos.domain.OrderStatus.COMPLETION;
import static kitchenpos.domain.OrderStatus.COOKING;
import static kitchenpos.domain.OrderStatus.MEAL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class TableGroupServiceTest extends ServiceTest {

    @Nested
    class CreateTest {

        private TableGroup tableGroup;

        @BeforeEach
        void setup() {
            tableGroup = new TableGroup();
            tableGroup.setOrderTables(ORDER_TABLES);

            orderTableDao.save(ORDER_TABLE1);
            orderTableDao.save(ORDER_TABLE2);
            orderTableDao.save(ORDER_TABLE3);
        }

        @Test
        void create_fail_when_orderTables_isEmpty() {
            tableGroup.setOrderTables(null);

            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void create_fail_when_orderTables_size_is_smaller_than_2() {
            tableGroup.setOrderTables(List.of(ORDER_TABLE1));

            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void create_fail_when_orderTable_is_not_exist() {
            tableGroup.setOrderTables(List.of(ORDER_TABLE1, INVALID_ORDER_TABLE));

            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void create_fail_when_orderTable_is_not_empty() {
            tableGroup.setOrderTables(List.of(ORDER_TABLE1, NOT_EMPTY_ORDER_TABLE));

            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void create_fail_when_orderTable_groupId_not_exist() {
            tableGroup.setOrderTables(List.of(ORDER_TABLE1, INVALID_GROUPID_ORDER_TABLE));

            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void create_success() {
            TableGroup savedTableGroup = tableGroupService.create(tableGroup);

            assertThat(savedTableGroup.getOrderTables()).hasSize(3);
        }
    }

    @Nested
    class UngroupTest {

        private TableGroup tableGroup;
        private Order order;

        @BeforeEach
        void setup() {
            tableGroup = new TableGroup();
            tableGroup.setOrderTables(ORDER_TABLES);

            order = new Order();

            orderTableDao.save(ORDER_TABLE1);
            orderTableDao.save(ORDER_TABLE2);
            orderTableDao.save(ORDER_TABLE3);
        }

        @Test
        void ungroup_fail_when_orderStatus_MEAL() {
            order.setOrderStatus(MEAL.name());
            order.setOrderTableId(1L);
            order.setOrderedTime(LocalDateTime.now());
            orderDao.save(order);

            TableGroup savedTableGroup = tableGroupService.create(tableGroup);

            assertThatThrownBy(() -> tableGroupService.ungroup(savedTableGroup.getId()))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void ungroup_success() {
            List<Order> orders = orderDao.findAll().stream()
                    .filter(it -> it.getOrderStatus().equals(MEAL.name()) || it.getOrderStatus().equals(COOKING.name()))
                    .collect(Collectors.toList());

            for (Order order: orders) {
                order.setOrderStatus(COMPLETION.name());
                orderDao.save(order);
            }

            tableGroup.setOrderTables(ORDER_TABLES);
            TableGroup savedTableGroup = tableGroupService.create(tableGroup);
            tableGroupService.ungroup(savedTableGroup.getId());

            OrderTable changedOrderTable = orderTableDao.findById(1L)
                    .orElseThrow(IllegalArgumentException::new);

            assertThat(changedOrderTable.getTableGroupId()).isNull();
        }
    }
}