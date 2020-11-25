package kitchenpos.dto;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

public class OrderTableResponse {

    private Long id;
    private Long tableGroupId;
    private int numberOfGuests;
    private boolean empty;

    public OrderTableResponse() {
    }

    private OrderTableResponse(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static OrderTableResponse of(OrderTable orderTable) {
        TableGroup tableGroup = orderTable.getTableGroup();

        if (Objects.isNull(tableGroup)) {
            return new OrderTableResponse(orderTable.getId(), null,
                orderTable.getNumberOfGuests(), orderTable.isEmpty());
        }

        return new OrderTableResponse(orderTable.getId(), tableGroup.getId(),
            orderTable.getNumberOfGuests(), orderTable.isEmpty());
    }

    public static List<OrderTableResponse> toResponseList(List<OrderTable> orderTables) {
        return orderTables.stream()
            .map(OrderTableResponse::of)
            .collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public OrderTable toEntity(TableGroup tableGroup) {
        return new OrderTable(id, tableGroup, numberOfGuests, empty);
    }
}
