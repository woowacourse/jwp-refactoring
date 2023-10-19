package kitchenpos.application.dto.tablegroup;

import static java.util.stream.Collectors.toList;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.TableGroup;

public class TableGroupCreateResponse {

    private final Long id;
    private final LocalDateTime createdDate;
    private final List<GroupOrderTableResponse> orderTables;

    public TableGroupCreateResponse(final Long id, final LocalDateTime createdDate,
                                    final List<GroupOrderTableResponse> orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public static TableGroupCreateResponse of(final TableGroup tableGroup) {
        final List<GroupOrderTableResponse> groupOrderTableResponses = tableGroup.getOrderTables().stream()
                .map(GroupOrderTableResponse::of)
                .collect(toList());
        return new TableGroupCreateResponse(
                tableGroup.getId(),
                tableGroup.getCreatedDate(),
                groupOrderTableResponses
        );
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<GroupOrderTableResponse> getOrderTables() {
        return orderTables;
    }
}
