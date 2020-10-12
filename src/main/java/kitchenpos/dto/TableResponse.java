package kitchenpos.dto;

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

    public static TableResponse from(OrderTable table) {
        TableGroupResponse tableGroup = TableGroupResponse.from(table.getTableGroup());
        return TableResponse.builder()
            .id(table.getId())
            .tableGroup(tableGroup)
            .numberOfGuests(table.getNumberOfGuests())
            .empty(table.isEmpty())
            .build();
    }
}
