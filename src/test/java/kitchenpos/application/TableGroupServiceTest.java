package kitchenpos.application;

import static kitchenpos.application.ServiceTestFixture.INVALID_GROUPID_ORDER_TABLE_REQUEST;
import static kitchenpos.application.ServiceTestFixture.INVALID_ORDER_TABLE_REQUEST;
import static kitchenpos.application.ServiceTestFixture.ORDER_TABLE1;
import static kitchenpos.application.ServiceTestFixture.ORDER_TABLE2;
import static kitchenpos.application.ServiceTestFixture.ORDER_TABLE3;
import static kitchenpos.application.ServiceTestFixture.ORDER_TABLES;
import static kitchenpos.application.ServiceTestFixture.ORDER_TABLE_REQUEST1;
import static kitchenpos.application.ServiceTestFixture.ORDER_TABLE_REQUESTS;
import static kitchenpos.domain.OrderStatus.COMPLETION;
import static kitchenpos.domain.OrderStatus.COOKING;
import static kitchenpos.domain.OrderStatus.MEAL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.TableGroupRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class TableGroupServiceTest {

    @Nested
    class CreateTest extends ServiceTest {

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
            assertThatThrownBy(() -> tableGroupService.create(new TableGroupRequest(new ArrayList<>())))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void create_fail_when_orderTables_size_is_smaller_than_2() {
            assertThatThrownBy(() -> tableGroupService.create(new TableGroupRequest(List.of(ORDER_TABLE_REQUEST1))))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void create_fail_when_orderTable_is_not_exist() {
            assertThatThrownBy(() -> tableGroupService.create(
                    new TableGroupRequest(List.of(ORDER_TABLE_REQUEST1, INVALID_ORDER_TABLE_REQUEST))))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void create_fail_when_orderTable_is_not_empty() {
            assertThatThrownBy(() -> tableGroupService.create(new TableGroupRequest(List.of())))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void create_fail_when_orderTable_groupId_not_exist() {
            assertThatThrownBy(() -> tableGroupService.create(
                    new TableGroupRequest(List.of(ORDER_TABLE_REQUEST1, INVALID_GROUPID_ORDER_TABLE_REQUEST))))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void create_success() {
            TableGroup savedTableGroup = tableGroupService.create(new TableGroupRequest(ORDER_TABLE_REQUESTS));

            assertThat(savedTableGroup.getOrderTables()).hasSize(3);
        }
    }

    @Nested
    class UngroupTest extends ServiceTest {

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

            TableGroup savedTableGroup = tableGroupService.create(new TableGroupRequest(ORDER_TABLE_REQUESTS));

            assertThatThrownBy(() -> tableGroupService.ungroup(savedTableGroup.getId()))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void ungroup_success() {
            List<Order> orders = orderDao.findAll().stream()
                    .filter(it -> it.getOrderStatus().equals(MEAL.name()) || it.getOrderStatus().equals(COOKING.name()))
                    .collect(Collectors.toList());

            for (Order order : orders) {
                order.setOrderStatus(COMPLETION.name());
                orderDao.save(order);
            }

            tableGroup.setOrderTables(ORDER_TABLES);
            TableGroup savedTableGroup = tableGroupService.create(new TableGroupRequest(ORDER_TABLE_REQUESTS));
            tableGroupService.ungroup(savedTableGroup.getId());

            OrderTable changedOrderTable = orderTableDao.findById(1L)
                    .orElseThrow(IllegalArgumentException::new);

            assertThat(changedOrderTable.getTableGroupId()).isNull();
        }
    }
}