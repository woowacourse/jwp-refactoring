package kitchenpos.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

public class TableGroupDto {

    private Long id;
    private LocalDateTime createdDate;
    private List<Long> orderTables;

    public TableGroupDto() {
    }

    public TableGroupDto(List<Long> orderTables) {
        this.createdDate = LocalDateTime.now();
        this.orderTables = orderTables;
    }

    public TableGroupDto(TableGroup tableGroup) {
        this.id = tableGroup.getId();
        this.createdDate = tableGroup.getCreatedDate();
        this.orderTables = tableGroup.getOrderTables()
                .stream()
                .map(OrderTable::getId)
                .collect(Collectors.toUnmodifiableList());
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<Long> getOrderTables() {
        return orderTables;
    }
}
