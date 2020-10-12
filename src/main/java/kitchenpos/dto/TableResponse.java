package kitchenpos.dto;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.OrderTable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TableResponse {

    private Long id;
    private TableGroupResponse tableGroup;
    private int numberOfGuests;
    private boolean empty;

    public static List<TableResponse> listFrom(List<OrderTable> tables) {
        return tables.stream()
            .map(TableResponse::from)
            .collect(Collectors.toList());
    }

    public static TableResponse from(OrderTable table) {
        TableGroupResponse tableGroup = TableGroupResponse.from(table.getTableGroup());
        return TableResponse.builder()
            .id(table.getId())
            .tableGroup(tableGroup)
            .numberOfGuests(table.getNumberOfGuests())
            .empty(table.isEmpty())
            .build();
    }

    public Long getId() {
        return id;
    }

    public TableGroupResponse getTableGroup() {
        return tableGroup;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }
}
