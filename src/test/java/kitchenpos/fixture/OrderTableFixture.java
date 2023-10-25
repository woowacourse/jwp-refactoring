package kitchenpos.fixture;

import java.util.function.Consumer;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTable.Builder;
import kitchenpos.dto.OrderTableDto;

public enum OrderTableFixture {

    EMPTY_TABLE1(1L, 0, true, null),
    EMPTY_TABLE2(2L, 0, true, null),
    EMPTY_TABLE_OF_GROUP(3L, 0, true, 1L),
    OCCUPIED_TABLE(4L, 4, false, null),
    OCCUPIED_TABLE_OF_GROUP1(5L, 4, false, 1L),
    OCCUPIED_TABLE_OF_GROUP2(6L, 4, false, 1L);

    private final Long id;
    private final int numberOfGuests;
    private final boolean empty;
    private final Long tableGroupId;

    OrderTableFixture(Long id, int numberOfGuests, boolean empty, Long tableGroupId) {
        this.id = id;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
        this.tableGroupId = tableGroupId;
    }

    public static OrderTableDto computeDefaultOrderTableDto(Consumer<OrderTableDto> consumer) {
        OrderTableDto table = new OrderTableDto();
        table.setId(null);
        table.setNumberOfGuests(0);
        table.setEmpty(true);
        table.setTableGroupId(null);
        consumer.accept(table);
        return table;
    }

    public OrderTableDto toDto() {
        OrderTableDto table = new OrderTableDto();
        table.setId(id);
        table.setNumberOfGuests(numberOfGuests);
        table.setEmpty(empty);
        table.setTableGroupId(tableGroupId);
        return table;
    }

    public OrderTable toEntity() {
        return new Builder()
            .setId(id)
            .setNumberOfGuests(numberOfGuests)
            .setEmpty(empty)
            .build();
    }
}
