package kitchenpos.application;

import static kitchenpos.application.ServiceTestFixture.ORDER_LINE_ITEMS;
import static kitchenpos.domain.OrderStatus.COOKING;
import static kitchenpos.domain.OrderStatus.MEAL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class TableServiceTest extends ServiceTest {

    @Test
    void create_success() {
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(false);
        orderTable.setNumberOfGuests(5);

        OrderTable savedTable = tableService.create(orderTable);

        assertThat(savedTable.getNumberOfGuests()).isEqualTo(5);
    }

    @Test
    void list_success() {
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(false);
        orderTable.setNumberOfGuests(5);

        assertThat(tableService.list()).hasSize(8);
    }

    @Nested
    class ChangeEmptyTest {

        private OrderTable orderTable;
        private OrderTable savedOrderTable;

        @BeforeEach
        void setup() {
            orderTable = new OrderTable();
            orderTable.setNumberOfGuests(5);
            orderTable.setEmpty(false);
        }

        @Test
        void changeEmpty_fail_when_orderTableId_not_exist() {
            savedOrderTable = tableService.create(orderTable);

            assertThatThrownBy(() -> tableService.changeEmpty(100L, orderTable))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void changeEmpty_fail_when_tableGroupId_exist() {
            TableGroup tableGroup = new TableGroup();
            tableGroup.setOrderTables(List.of(orderTable));
            tableGroup.setCreatedDate(LocalDateTime.now());
            TableGroup savedTableGroup = tableGroupDao.save(tableGroup);

            orderTable.setTableGroupId(savedTableGroup.getId());

            savedOrderTable = orderTableDao.save(orderTable);

            assertThatThrownBy(() -> tableService.changeEmpty(savedOrderTable.getId(), orderTable))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void changeEmpty_fail_when_orderStatus_in_COOKING() {
            savedOrderTable = tableService.create(orderTable);

            Order order = new Order();
            order.setOrderStatus(COOKING.name());
            order.setOrderLineItems(ORDER_LINE_ITEMS);
            order.setOrderTableId(savedOrderTable.getId());
            order.setOrderedTime(LocalDateTime.now());

            Order savedOrder = orderDao.save(order);

            assertThatThrownBy(() -> tableService.changeEmpty(savedOrderTable.getId(), orderTable))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void changeEmpty_fail_when_orderStatus_in_MEAL() {
            savedOrderTable = tableService.create(orderTable);

            Order order = new Order();
            order.setOrderStatus(MEAL.name());
            order.setOrderLineItems(ORDER_LINE_ITEMS);
            order.setOrderTableId(savedOrderTable.getId());
            order.setOrderedTime(LocalDateTime.now());

            Order savedOrder = orderDao.save(order);

            assertThatThrownBy(() -> tableService.changeEmpty(savedOrderTable.getId(), orderTable))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void changeEmpty_success() {
            savedOrderTable = tableService.create(orderTable);
            orderTable.setEmpty(true);

            OrderTable changedTable = tableService.changeEmpty(savedOrderTable.getId(), this.orderTable);

            assertThat(changedTable.isEmpty()).isTrue();
        }
    }

    @Nested
    class ChangeNumberOfGuestsTest {

        private OrderTable orderTable;
        private OrderTable savedOrderTable;

        @BeforeEach
        void setup() {
            orderTable = new OrderTable();
            orderTable.setNumberOfGuests(5);
            orderTable.setEmpty(false);
        }

        @Test
        void changeNumberOfGuests_fail_when_smaller_than_zero() {
            savedOrderTable = tableService.create(orderTable);

            orderTable.setNumberOfGuests(-1);

            assertThatThrownBy(() -> tableService.changeNumberOfGuests(savedOrderTable.getId(), orderTable))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void changeNumberOfGuests_fail_when_orderTable_not_exist() {
            orderTable.setNumberOfGuests(-1);

            assertThatThrownBy(() -> tableService.changeNumberOfGuests(1000L, orderTable))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void changeNumberOfGuests_fail_when_orderTable_is_empty() {
            orderTable.setEmpty(true);
            savedOrderTable = tableService.create(orderTable);

            assertThatThrownBy(() -> tableService.changeNumberOfGuests(savedOrderTable.getId(), orderTable))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void changeNumberOfGuests_success() {
            savedOrderTable = tableService.create(orderTable);
            orderTable.setNumberOfGuests(10);

            OrderTable changedOrderTable = tableService.changeNumberOfGuests(savedOrderTable.getId(), this.orderTable);

            assertThat(changedOrderTable.getNumberOfGuests()).isEqualTo(10);
        }
    }
}