package kitchenpos.order.application.request;

import java.util.List;

public class OrderRequest {

    private Long id;
    private Long orderTableId;
    private String orderStatus;
    private List<OrderLineItemRequest> orderLineItemRequests;

    private OrderRequest() {
    }

    public OrderRequest(Long id, Long orderTableId, String orderStatus,
        List<OrderLineItemRequest> orderLineItems) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderLineItemRequests = orderLineItems;
    }

    public Long getId() {
        return id;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    // public List<OrderLineItem> getOrderLineItems() {
    //     List<OrderLineItem> list = new ArrayList<>();
    //     for (OrderLineItemRequest orderLineItem : orderLineItemRequests) {
    //         OrderLineItem toEntity = orderLineItem.toEntity();
    //         list.add(toEntity);
    //     }
    //     return Collections.unmodifiableList(list);
    // }

    public List<OrderLineItemRequest> getOrderLineItemRequests() {
        return orderLineItemRequests;
    }
}
