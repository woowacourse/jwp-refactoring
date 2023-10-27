package kitchenpos.fixture;

import kitchenpos.table.domain.OrderTable;

@SuppressWarnings("NonAsciiCharacters")
public class OrderTableFixture {

    public static OrderTable createOrderTable(Long id, boolean empty, int numberOfGuest) {
        return OrderTable.builder()
                .empty(empty)
                .numberOfGuests(numberOfGuest)
                .build();
    }
}
