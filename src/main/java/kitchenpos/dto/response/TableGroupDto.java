package kitchenpos.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.TableGroup;

public class TableGroupDto {
    private Long id;
    private LocalDateTime createdDate;
    private List<OrderTableReseponse> orderTables;

    public static TableGroupDto from(TableGroup tableGroup) {
        List<OrderTableReseponse> orderTableReseponses = tableGroup.getOrderTables().stream()
                .map(OrderTableReseponse::from)
                .collect(Collectors.toList());
        return new TableGroupDto(tableGroup.getId(), tableGroup.getCreatedDate(),
                orderTableReseponses);
    }

    public TableGroupDto(List<OrderTableReseponse> orderTables) {
        this(null, null, orderTables);
    }

    public TableGroupDto(Long id, LocalDateTime createdDate, List<OrderTableReseponse> orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<OrderTableReseponse> getOrderTables() {
        return orderTables;
    }
}
