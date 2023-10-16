package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import kitchenpos.application.dto.OrderTableCreationRequest;
import kitchenpos.application.dto.OrderTableEmptyStatusChangeRequest;
import kitchenpos.application.dto.OrderTableGuestAmountChangeRequest;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
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
        final OrderTable createdOrderTable = tableService.create(request);

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
        final List<OrderTable> findAll = tableService.list();

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
        final OrderTable changedOrderTable = tableService.changeEmpty(orderTable.getId(), request);

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
            final TableGroup savedTableGroup = generateTableGroup();
            final OrderTable orderTable = generateOrderTable(1, true, savedTableGroup);
            final OrderTableEmptyStatusChangeRequest request = new OrderTableEmptyStatusChangeRequest(false);

            // when & then
            assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Cannot change empty status of table in group");
        }

        @Test
        void any_order_in_order_table_status_is_not_completion() {
            // given
            final OrderTable orderTable = generateOrderTableWithOutTableGroup(1, false);
            orderTable.addOrder(generateOrder(OrderStatus.COOKING, orderTable));
            orderTable.addOrder(generateOrder(OrderStatus.COMPLETION, orderTable));
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
        final OrderTable changedOrderTable = tableService.changeNumberOfGuests(orderTable.getId(), request);

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
