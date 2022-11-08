package kitchenpos.table.dto;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.table.domain.OrderTable;

public class OrderTableDto {

    private Long id;

    public OrderTableDto() {
    }

    public OrderTableDto(final Long id) {
        this.id = id;
    }

    public static List<OrderTableDto> from(final List<OrderTable> orderTables) {
        return orderTables.stream()
                .map(OrderTableDto::from)
                .collect(Collectors.toList());
    }

    private static OrderTableDto from(final OrderTable orderTable) {
        return new OrderTableDto(orderTable.getId());
    }

    public Long getId() {
        return id;
    }
}
