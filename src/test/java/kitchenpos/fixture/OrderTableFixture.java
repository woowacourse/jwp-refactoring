package kitchenpos.fixture;

import kitchenpos.domain.OrderTable;

@SuppressWarnings("NonAsciiCharacters")
public class OrderTableFixture {

    public static OrderTable createOrderTable(Long id, boolean empty, int numberOfGuest) {
        return OrderTable.builder()
                .empty(empty)
                .numberOfGuests(numberOfGuest)
                .build();
    }
}
