package kitchenpos.application.dto.request;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

public class TableGroupCreateRequest {

    private Long id;
    private LocalDateTime createdDate;
    private List<OrderTableIdDto> orderTableIdsDto;

    public TableGroupCreateRequest() {
    }

    public TableGroupCreateRequest(final Long id, final LocalDateTime createdDate,
                                   final List<OrderTableIdDto> orderTableIdsDto) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTableIdsDto = orderTableIdsDto;
    }

    public TableGroup toTableGroup(final LocalDateTime createdDate) {
        return new TableGroup(null, createdDate);
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<OrderTableIdDto> getOrderTableIdsDto() {
        return orderTableIdsDto;
    }
}
