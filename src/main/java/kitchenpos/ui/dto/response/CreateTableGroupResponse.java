package kitchenpos.ui.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.TableGroup;

public class CreateTableGroupResponse {

    private Long id;
    private LocalDateTime createdDate;
    private List<CreateTableGroupOrderTableResponse> orderTables;

    public CreateTableGroupResponse(final TableGroup tableGroup) {
        this.id = tableGroup.getId();
        this.createdDate = tableGroup.getCreatedDate();
        this.orderTables = tableGroup.getOrderTables()
                                     .stream()
                                     .map(CreateTableGroupOrderTableResponse::new)
                                     .collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<CreateTableGroupOrderTableResponse> getOrderTables() {
        return orderTables;
    }
}
