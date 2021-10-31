package kitchenpos.tablegroup.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.tablegroup.domain.TableGroup;

public class TableGroupCreateResponseDto {
    private Long id;
    private LocalDateTime createdDate;
    private List<Long> orderTableIds = new ArrayList<>();

    public TableGroupCreateResponseDto(TableGroup tableGroup) {
        this.id = tableGroup.getId();
        this.createdDate = tableGroup.getCreatedDate();
        for (OrderTable orderTable : tableGroup.getOrderTables()) {
            orderTableIds.add(orderTable.getId());
        }
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<Long> getOrderTableIds() {
        return orderTableIds;
    }
}
