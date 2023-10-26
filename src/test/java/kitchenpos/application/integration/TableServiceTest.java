package kitchenpos.application.integration;

import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.dto.order.ChangeOrderStatusRequest;
import kitchenpos.dto.order.OrderResponse;
import kitchenpos.dto.table.ChangeNumberOfGuestsRequest;
import kitchenpos.dto.table.ChangeOrderTableOrderableRequest;
import kitchenpos.dto.table.CreateOrderTableRequest;
import kitchenpos.dto.table.CreateTableGroupRequest;
import kitchenpos.dto.table.OrderTableRequest;
import kitchenpos.dto.table.OrderTableResponse;
import kitchenpos.exception.table.OrderIsNotCompletedBadRequestException;
import kitchenpos.exception.table.OrderTableIsInTableGroupBadRequest;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TableServiceTest extends ApplicationIntegrationTest {

    @Test
    void create_table() {
        //given
        final CreateOrderTableRequest createOrderTableRequest = CreateOrderTableRequest.of(4, true);

        //when
        final OrderTableResponse createdOrderTable = tableService.create(createOrderTableRequest);

        //then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(createdOrderTable.getId()).isNotNull();
            softAssertions.assertThat(createdOrderTable.getNumberOfGuests()).isEqualTo(createOrderTableRequest.getNumberOfGuests());
        });
    }

    @Test
    void cannot_create_table_with_negative_number_of_guests() {
        //given
        final CreateOrderTableRequest createOrderTableRequest = CreateOrderTableRequest.of(-1, true);

        //when & then
        assertThatThrownBy(() -> tableService.create(createOrderTableRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(OrderTable.NUMBER_OF_GUESTS_IS_BELOW_ZERO_ERROR_MESSAGE);
    }

    @Test
    void throw_when_unorderable_table_with_cooking_order_status() {
        //given
        final CreateOrderTableRequest createOrderTableRequest = CreateOrderTableRequest.of(4, true);
        final OrderTableResponse createdOrderTable = tableService.create(createOrderTableRequest);
        final OrderResponse order = createOrder(createdOrderTable.getId());
        orderService.changeOrderStatus(order.getId(), ChangeOrderStatusRequest.of(OrderStatus.MEAL));

        //when & then
        assertThatThrownBy(() -> tableService.changeOrderable(createdOrderTable.getId(), ChangeOrderTableOrderableRequest.of(false)))
                .isInstanceOf(OrderIsNotCompletedBadRequestException.class);
    }

    @Test
    void throw_when_unorderable_table_with_table_group() {
        //given
        final CreateOrderTableRequest createOrderTableRequest = CreateOrderTableRequest.of(4, false);
        final OrderTableResponse createdOrderTable = tableService.create(createOrderTableRequest);
        final OrderResponse order = createOrder(createdOrderTable.getId());
        orderService.changeOrderStatus(order.getId(), ChangeOrderStatusRequest.of(OrderStatus.COMPLETION));
        tableGroupService.create(CreateTableGroupRequest.of(List.of(OrderTableRequest.of(createdOrderTable.getId()), OrderTableRequest.of(createOrderTable(4, false).getId()))));

        //when & then
        assertThatThrownBy(() -> tableService.changeOrderable(createdOrderTable.getId(), ChangeOrderTableOrderableRequest.of(false)))
                .isInstanceOf(OrderTableIsInTableGroupBadRequest.class);
    }


    @Test
    void change_number_of_guests() {
        //given
        final CreateOrderTableRequest createOrderTableRequest = CreateOrderTableRequest.of(4, true);
        final OrderTableResponse createdOrderTable = tableService.create(createOrderTableRequest);

        //when
        final OrderTableResponse changedOrderTable = tableService.changeNumberOfGuests(createdOrderTable.getId(), ChangeNumberOfGuestsRequest.of(5));

        //then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(changedOrderTable.getId()).isEqualTo(createdOrderTable.getId());
            softAssertions.assertThat(changedOrderTable.getNumberOfGuests()).isEqualTo(5);
        });

    }

    @Test
    void throw_when_number_of_guests_with_negative_number_of_guests() {
        //given
        final CreateOrderTableRequest createOrderTableRequest = CreateOrderTableRequest.of(4, true);
        final OrderTableResponse createdOrderTable = tableService.create(createOrderTableRequest);

        //when & then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(createdOrderTable.getId(), ChangeNumberOfGuestsRequest.of(-1)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(OrderTable.NUMBER_OF_GUESTS_IS_BELOW_ZERO_ERROR_MESSAGE);

    }

    @Test
    void throw_when_number_of_guests_with_unorderable_order_table() {
        //given
        final CreateOrderTableRequest createOrderTableRequest = CreateOrderTableRequest.of(4, false);
        final OrderTableResponse createdOrderTable = tableService.create(createOrderTableRequest);

        //when & then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(createdOrderTable.getId(), ChangeNumberOfGuestsRequest.of(5)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(OrderTable.CHANGE_UNORDERABLE_TABLE_NUMBER_OF_TABLE_ERROR_MESSAGE);
    }
}