package kitchenpos.ui.dto.request.tablegroup;

import kitchenpos.application.dto.request.tablegroup.OrderTableGroupRequestDto;

public class OrderTableGroupRequest {

    private Long id;

    private OrderTableGroupRequest() {
    }

    public OrderTableGroupRequest(Long id) {
        this.id = id;
    }

    public OrderTableGroupRequestDto toDto() {
        return new OrderTableGroupRequestDto(id);
    }

    public Long getId() {
        return id;
    }
}
