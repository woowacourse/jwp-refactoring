package kitchenpos.fixture;

import kitchenpos.application.dto.OrderTableChangeEmptyRequest;
import kitchenpos.application.dto.OrderTableChangeNumberOfGuestsRequest;
import kitchenpos.application.dto.OrderTableCreateRequest;
import kitchenpos.domain.OrderTable;

public class OrderTableFixture {
    public static OrderTable createOrderTable(
        Long id,
        boolean empty,
        int numberOfGuests,
        Long tableGroupId
    ) {
        return new OrderTable(id, tableGroupId, numberOfGuests, empty);
    }

    public static OrderTableCreateRequest createOrderTableRequest(
        boolean empty,
        int numberOfGuests
    ) {
        return new OrderTableCreateRequest(numberOfGuests, empty);
    }

    public static OrderTableChangeNumberOfGuestsRequest createOrderTableChangeNumberOfGuestsRequest(
        int numberOfGuests
    ) {
        return new OrderTableChangeNumberOfGuestsRequest(numberOfGuests);
    }

    public static OrderTableChangeEmptyRequest createOrderTableChangeEmptyRequest(boolean empty) {
        return new OrderTableChangeEmptyRequest(empty);
    }
}
