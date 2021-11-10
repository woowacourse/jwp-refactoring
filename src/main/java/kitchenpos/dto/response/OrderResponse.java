package kitchenpos.dto.response;

import kitchenpos.domain.Order;

import java.util.List;
import java.util.stream.Collectors;

public class OrderResponse {

    private Long id;
    private OrderTableResponse orderTable;
    private String orderStatus;
    private List<OrderLineItemResponse> orderLineItems;

    private OrderResponse(Long id, OrderTableResponse orderTable, String orderStatus, List<OrderLineItemResponse> orderLineItems) {
        this.id = id;
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
        this.orderLineItems = orderLineItems;
    }

    protected OrderResponse() {
    }

    public Long getId() {
        return id;
    }

    public OrderTableResponse getOrderTable() {
        return orderTable;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public List<OrderLineItemResponse> getOrderLineItems() {
        return orderLineItems;
    }

    public static OrderResponse toDTO(Order order) {
        List<OrderLineItemResponse> orderLineItemResponses = order.getOrderLineItems().stream()
                .map(OrderLineItemResponse::toDTO)
                .collect(Collectors.toList());

        return new OrderResponse(order.getId(), OrderTableResponse.toDTO(order.getOrderTable()),
                order.getOrderStatus(), orderLineItemResponses);
    }
}
