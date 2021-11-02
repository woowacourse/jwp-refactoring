package kitchenpos.factory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderTableRequest;
import kitchenpos.dto.OrderTableResponse;

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

    public static OrderTable copy(OrderTableResponse orderTableResponse) {
        return new OrderTable(
            orderTableResponse.getId(),
            orderTableResponse.getTableGroupId(),
            orderTableResponse.getNumberOfGuests(),
            orderTableResponse.isEmpty()
        );
    }

    public static List<OrderTable> copyList(List<OrderTableResponse> orderTableResponses) {
        return orderTableResponses.stream()
            .map(OrderTableFactory::copy)
            .collect(Collectors.toList());
    }

    public static OrderTableRequest dto(OrderTable orderTable) {
        return new OrderTableRequest(
            orderTable.getId(),
            orderTable.getTableGroupId(),
            orderTable.getNumberOfGuests(),
            orderTable.isEmpty()
        );
    }

    public static List<OrderTableRequest> dtoList(List<OrderTable> orderTables) {
        return orderTables.stream()
            .map(orderTable -> new OrderTableRequest(
                orderTable.getId(),
                orderTable.getTableGroupId(),
                orderTable.getNumberOfGuests(),
                orderTable.isEmpty()
            ))
            .collect(Collectors.toList());
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
        return new OrderTable(id, tableGroupId, numberOfGuests, empty);
    }
}
