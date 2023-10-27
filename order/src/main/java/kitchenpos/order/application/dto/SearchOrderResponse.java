package kitchenpos.order.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.domain.Order;

public class SearchOrderResponse {

    @JsonProperty("id")
    private Long id;
    @JsonProperty("orderTableId")
    private Long orderTableId;
    @JsonProperty("orderStatus")
    private String orderStatus;
    @JsonProperty("orderedTime")
    private LocalDateTime orderedTime;
    @JsonProperty("orderLineItems")
    private List<OrderLineItemResponse> orderLineItemResponses;

    private SearchOrderResponse(
            Long id,
            Long orderTableId,
            String orderStatus,
            LocalDateTime orderedTime,
            List<OrderLineItemResponse> orderLineItemResponses
    ) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItemResponses = orderLineItemResponses;
    }

    public static SearchOrderResponse from(Order order) {
        return new SearchOrderResponse(
                order.id(),
                order.orderTableId(),
                order.orderStatus().name(),
                order.orderedTime(),
                order.orderLineItems().items().stream()
                        .map(OrderLineItemResponse::from)
                        .collect(Collectors.toList())
        );
    }

    public Long id() {
        return id;
    }

    public Long orderTableId() {
        return orderTableId;
    }

    public String orderStatus() {
        return orderStatus;
    }

    public LocalDateTime orderedTime() {
        return orderedTime;
    }

    public List<OrderLineItemResponse> orderLineItemResponses() {
        return orderLineItemResponses;
    }
}
