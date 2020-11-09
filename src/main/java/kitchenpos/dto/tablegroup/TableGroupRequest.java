package kitchenpos.dto.tablegroup;

import java.time.LocalDateTime;
import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import kitchenpos.domain.tablegroup.TableGroup;

public class TableGroupRequest {

    @NotEmpty
    @Size(min = 2)
    private List<Long> orderTableIds;

    protected TableGroupRequest() {
    }

    public TableGroupRequest(final List<Long> orderTableIds) {
        this.orderTableIds = orderTableIds;
    }

    public List<Long> getOrderTableIds() {
        return orderTableIds;
    }

    public TableGroup toEntity() {
        return new TableGroup(LocalDateTime.now());
    }
}
