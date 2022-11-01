package kitchenpos.application;

import static kitchenpos.application.ServiceTestFixture.ORDER_LINE_ITEMS;
import static kitchenpos.domain.OrderStatus.COOKING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.OrderTableRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class TableServiceTest {

    @Nested
    class CreateTest extends ServiceTest {
        @Test
        void create_success() {
            final OrderTableRequest orderTableRequest = new OrderTableRequest(5, false);

            final OrderTable savedTable = tableService.create(orderTableRequest);

            assertThat(savedTable.getNumberOfGuests()).isEqualTo(5);
        }
    }

    @Nested
    class ListTest extends ServiceTest {
        @Test
        void list_success() {
            assertThat(tableService.list()).hasSize(8);
        }
    }

    @Nested
    class ChangeEmptyTest extends ServiceTest {

        private OrderTable orderTable;
        private OrderTable savedOrderTable;

        @BeforeEach
        void setup() {
            orderTable = new OrderTable(1L, 5, false);
        }

        @Test
        void changeEmpty_fail_when_orderTableId_not_exist() {
            final OrderTableRequest orderTableRequest = new OrderTableRequest(5, false);
            savedOrderTable = tableService.create(orderTableRequest);

            final OrderTableRequest changeOrderTableRequest = new OrderTableRequest(true);

            assertThatThrownBy(() -> tableService.changeEmpty(100L, changeOrderTableRequest))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void changeEmpty_fail_when_tableGroupId_exist() {
            final TableGroup tableGroup = new TableGroup(LocalDateTime.now(), List.of(orderTable));
            final TableGroup savedTableGroup = tableGroupDao.save(tableGroup);

            final OrderTable newOrderTable = new OrderTable(1L, savedTableGroup.getId(), 5, false);

            savedOrderTable = orderTableDao.save(newOrderTable);

            assertThatThrownBy(() -> tableService.changeEmpty(savedOrderTable.getId(), new OrderTableRequest(true)))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void changeEmpty_fail_when_orderStatus_in_COOKING() {
            final OrderTableRequest orderTableRequest = new OrderTableRequest(5, false);
            savedOrderTable = tableService.create(orderTableRequest);

            final Order order = new Order(savedOrderTable.getId(), COOKING, LocalDateTime.now(), ORDER_LINE_ITEMS);

            orderDao.save(order);

            assertThatThrownBy(() -> tableService.changeEmpty(savedOrderTable.getId(), new OrderTableRequest(true)))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void changeEmpty_fail_when_orderStatus_in_MEAL() {
            final OrderTableRequest orderTableRequest = new OrderTableRequest(5, false);
            savedOrderTable = tableService.create(orderTableRequest);

            final Order order = new Order(savedOrderTable.getId(), COOKING, LocalDateTime.now(), ORDER_LINE_ITEMS);

            orderDao.save(order);

            assertThatThrownBy(() -> tableService.changeEmpty(savedOrderTable.getId(), new OrderTableRequest(true)))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void changeEmpty_success() {
            final OrderTableRequest orderTableRequest = new OrderTableRequest(5, false);
            savedOrderTable = tableService.create(orderTableRequest);

            OrderTable changedTable = tableService.changeEmpty(savedOrderTable.getId(), new OrderTableRequest(true));

            assertThat(changedTable.isEmpty()).isTrue();
        }
    }

    @Nested
    class ChangeNumberOfGuestsTest extends ServiceTest {

        @Test
        void changeNumberOfGuests_fail_when_smaller_than_zero() {
            final OrderTableRequest orderTableRequest = new OrderTableRequest(5, false);

            final OrderTable savedOrderTable = tableService.create(orderTableRequest);

            assertThatThrownBy(
                    () -> tableService.changeNumberOfGuests(savedOrderTable.getId(), new OrderTableRequest(-1)))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void changeNumberOfGuests_fail_when_orderTable_not_exist() {
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(1000L, new OrderTableRequest(6)))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void changeNumberOfGuests_fail_when_orderTable_is_empty() {
            final OrderTableRequest orderTableRequest = new OrderTableRequest(5, true);
            final OrderTable savedOrderTable = tableService.create(orderTableRequest);

            assertThatThrownBy(
                    () -> tableService.changeNumberOfGuests(savedOrderTable.getId(), new OrderTableRequest(6)))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void changeNumberOfGuests_success() {
            final OrderTableRequest orderTableRequest = new OrderTableRequest(5, false);
            final OrderTable savedOrderTable = tableService.create(orderTableRequest);

            final OrderTable changedOrderTable = tableService.changeNumberOfGuests(savedOrderTable.getId(),
                    new OrderTableRequest(10));

            assertThat(changedOrderTable.getNumberOfGuests()).isEqualTo(10);
        }
    }
}