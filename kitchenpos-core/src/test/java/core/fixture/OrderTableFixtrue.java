package core.fixture;

import core.application.dto.OrderTableNumberOfGuestRequest;
import core.application.dto.OrderTableRequest;
import core.domain.OrderTable;

public class OrderTableFixtrue {
    public static OrderTable orderTable(int numberOfGuests, boolean empty) {
        return new OrderTable(numberOfGuests, empty);
    }

    public static OrderTable orderTable(Long tableGroupId, int numberOfGuests, boolean empty) {
        return new OrderTable(tableGroupId, numberOfGuests, empty);
    }

    public static OrderTableRequest orderTableRequest(int numberOfGuests, boolean empty) {
        return new OrderTableRequest(numberOfGuests, empty);
    }

    public static OrderTableNumberOfGuestRequest orderTableNumberOfGuestsRequest(int numberOfGuests) {
        return new OrderTableNumberOfGuestRequest(numberOfGuests);
    }
}
