package kitchenpos.fixture;

import kitchenpos.domain.OrderTable;
import kitchenpos.ui.dto.OrderTableCreateRequest;
import kitchenpos.ui.dto.OrderTableNumOfGuestRequest;
import kitchenpos.ui.dto.OrderTableStatusRequest;

public class OrderTableFixture {
    public static OrderTable createOrderTable(Long id, boolean empty, Long tableGroupId, int numberOfGuests) {
        return new OrderTable(id, tableGroupId, numberOfGuests, empty);
    }

    public static OrderTableCreateRequest createOrderTableRequest(int numberOfGuests, boolean empty) {
        return new OrderTableCreateRequest(numberOfGuests, empty);
    }

    public static OrderTableStatusRequest modifyOrderTableEmptyRequest(boolean empty) {
        return new OrderTableStatusRequest(empty);
    }

    public static OrderTableNumOfGuestRequest modifyOrderTableNumOfGuestRequest(int numberOfGuests) {
        return new OrderTableNumOfGuestRequest(numberOfGuests);
    }
}
