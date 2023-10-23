package kitchenpos.dto.request;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public class OrderTableCreateRequest {

    private final Long tableGroupId;
    private final int numberOfGuests;
    private final List<Long> orderIds;

    public OrderTableCreateRequest(Long tableGroupId, int numberOfGuests, List<Long> orderIds) {
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.orderIds = orderIds;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public List<Long> getOrderIds() {
        return orderIds;
    }

}
