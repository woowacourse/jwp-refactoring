package kitchenpos.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import javax.validation.constraints.NotNull;

public class OrderCreateRequest {

    @NotNull
    @JsonProperty(value = "orderTableId")
    private Long tableId;

    @NotNull
    private List<OrderLineItemCreateRequest> orderLineItems;

    private OrderCreateRequest() {
    }

    public OrderCreateRequest(final Long tableId,
                              final List<OrderLineItemCreateRequest> orderLineItems) {
        this.tableId = tableId;
        this.orderLineItems = orderLineItems;
    }

    public Long getTableId() {
        return tableId;
    }

    public List<OrderLineItemCreateRequest> getOrderLineItems() {
        return orderLineItems;
    }
}
