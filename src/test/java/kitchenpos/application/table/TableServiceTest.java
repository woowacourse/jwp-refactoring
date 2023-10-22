package kitchenpos.application.table;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import kitchenpos.application.IntegrationTest;
import kitchenpos.application.dto.OrderTableCreationRequest;
import kitchenpos.application.dto.OrderTableEmptyStatusChangeRequest;
import kitchenpos.application.dto.OrderTableGuestAmountChangeRequest;
import kitchenpos.application.dto.result.OrderTableResult;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.TableGroup;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class TableServiceTest extends IntegrationTest {

    @Autowired
    private TableService tableService;

    @Test
    void create_order_table() {
        // given
        final OrderTableCreationRequest request = new OrderTableCreationRequest(1, true);

        // when
        final OrderTableResult createdOrderTable = tableService.create(request);

        // then
        assertThat(createdOrderTable.getId()).isNotNull();
    }

    @Test
    void list() {
        // given
        generateOrderTable(3);
        generateOrderTable(2);
        generateOrderTable(1);

        // when
        final List<OrderTableResult> findAll = tableService.list();

        // then
        assertThat(findAll).hasSize(3);
    }

    @Test
    void change_empty_success() {
        // given
        final OrderTable orderTable = generateOrderTableWithOutTableGroup(1, false);
        generateOrder(OrderStatus.COMPLETION, orderTable);
        generateOrder(OrderStatus.COMPLETION, orderTable);
        final OrderTableEmptyStatusChangeRequest request = new OrderTableEmptyStatusChangeRequest(false);

        // when
        final OrderTableResult changedOrderTable = tableService.changeEmpty(orderTable.getId(), request);

        // then
        assertThat(changedOrderTable.isEmpty()).isFalse();
    }

    @Nested
    class change_empty_failure {

        @Test
        void order_table_is_not_exist() {
            final Long notExistId = 10000L;

            // when & then
            assertThatThrownBy(() -> tableService.changeEmpty(notExistId, null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Order table does not exist.");
        }

        @Test
        void order_table_is_still_group_by_table_group() {
            // given
            final OrderTable orderTableA = generateOrderTable(1, true);
            final OrderTable orderTableB = generateOrderTable(1, true);
            final TableGroup savedTableGroup = generateTableGroup();
            orderTableA.groupByTableGroup(savedTableGroup);
            orderTableB.groupByTableGroup(savedTableGroup);
            final OrderTableEmptyStatusChangeRequest request = new OrderTableEmptyStatusChangeRequest(false);

            // when & then
            assertThatThrownBy(() -> tableService.changeEmpty(orderTableA.getId(), request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Cannot change empty status of table in group");
        }

        //        @Disabled
        @Test
        void any_order_in_order_table_status_is_not_completion() {
            // given
            final OrderTable orderTable = generateOrderTableWithOutTableGroup(1, false);
            generateOrder(OrderStatus.COOKING, orderTable);
            generateOrder(OrderStatus.COMPLETION, orderTable);
            final OrderTableEmptyStatusChangeRequest request = new OrderTableEmptyStatusChangeRequest(false);

            // when & then
            assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Cannot change empty status of table with order status not completion");
        }
    }

    @Test
    void change_number_of_guests_success() {
        // given
        final OrderTable orderTable = generateOrderTableWithOutTableGroup(1, false);
        final OrderTableGuestAmountChangeRequest request = new OrderTableGuestAmountChangeRequest(3);

        // when
        final OrderTableResult changedOrderTable = tableService.changeNumberOfGuests(orderTable.getId(), request);

        // then
        assertThat(changedOrderTable.getNumberOfGuests()).isEqualTo(3);
    }

    @Nested
    class change_number_of_guests_failure {

        @Test
        void order_table_is_not_exist() {
            final Long notExistId = 1000L;
            // when
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(notExistId, null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Order table does not exist.");
        }

        @Test
        void order_table_is_empty() {
            // given
            final OrderTable orderTable = generateOrderTableWithOutTableGroup(1, true);
            final OrderTableGuestAmountChangeRequest request = new OrderTableGuestAmountChangeRequest(3);
            // when
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Cannot change number of guests of empty table");
        }

        @Test
        void change_number_is_less_than_zero() {
            // given
            final OrderTable orderTable = generateOrderTableWithOutTableGroup(1, true);
            final OrderTableGuestAmountChangeRequest request = new OrderTableGuestAmountChangeRequest(-1);

            // when
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Number of guests must be greater than 0");
        }
    }
}
