package kitchenpos.application.dto.tablegroup;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.ordertable.OrderTableResponse;
import kitchenpos.domain.TableGroup;

public class CreateTableGroupResponse {

    @JsonProperty("id")
    private final Long id;
    @JsonProperty("createdDate")
    private final LocalDateTime createdDate;
    @JsonProperty("orderTables")
    private final List<OrderTableResponse> orderTableResponses;

    private CreateTableGroupResponse(Long id, LocalDateTime createdDate, List<OrderTableResponse> orderTableResponses) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTableResponses = orderTableResponses;
    }

    public static CreateTableGroupResponse from(TableGroup tableGroup) {
        return new CreateTableGroupResponse(
                tableGroup.id(),
                tableGroup.createdDate(),
                tableGroup.orderTables().stream()
                        .map(OrderTableResponse::from)
                        .collect(Collectors.toList())
        );
    }

    public Long id() {
        return id;
    }

    public LocalDateTime createdDate() {
        return createdDate;
    }

    public List<OrderTableResponse> orderTableResponses() {
        return orderTableResponses;
    }
}
