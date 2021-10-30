package kitchenpos.factory;

import kitchenpos.domain.OrderTable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class OrderTableFactory {

    private Long id;
    private Long tableGroupId;
    private int numberOfGuests;
    private boolean empty;

    private OrderTableFactory() {

    }

    private OrderTableFactory(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static OrderTableFactory builder() {
        return new OrderTableFactory();
    }

    public static OrderTableFactory copy(OrderTable orderTable) {
        return new OrderTableFactory(
                orderTable.getId(),
                orderTable.getTableGroupId(),
                orderTable.getNumberOfGuests(),
                orderTable.isEmpty()
        );
    }

    public OrderTableFactory id(Long id) {
        this.id = id;
        return this;
    }

    public OrderTableFactory tableGroupId(Long tableGroupId) {
        this.tableGroupId = tableGroupId;
        return this;
    }

    public OrderTableFactory numberOfGuests(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
        return this;
    }

    public OrderTableFactory empty(boolean empty) {
        this.empty = empty;
        return this;
    }

    public OrderTable build() {
        OrderTable orderTable = new OrderTable();
        orderTable.setId(id);
        orderTable.setTableGroupId(tableGroupId);
        orderTable.setNumberOfGuests(numberOfGuests);
        orderTable.setEmpty(empty);
        return orderTable;
    }

    public List<OrderTable> buildList(int size) {
        return IntStream.range(0, size)
                .boxed()
                .map(num -> build())
                .collect(Collectors.toList());
    }
}
