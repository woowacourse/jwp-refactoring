package kitchenpos.dto;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class OrderTableResponse {

    private Long id;
    private Long tableGroupId;
    private int numberOfGuests;
    private boolean empty;

    public static List<OrderTableResponse> listFrom(final List<OrderTable> tables) {
        return tables.stream()
            .map(OrderTableResponse::from)
            .collect(Collectors.toList());
    }

    public static OrderTableResponse from(final OrderTable table) {
        Long tableGroupId = Optional.ofNullable(table.getTableGroup())
            .map(TableGroup::getId)
            .orElse(null);

        return OrderTableResponse.builder()
            .id(table.getId())
            .tableGroupId(tableGroupId)
            .numberOfGuests(table.getNumberOfGuests())
            .empty(table.isEmpty())
            .build();
    }
}
