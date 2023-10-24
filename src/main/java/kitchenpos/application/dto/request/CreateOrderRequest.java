package kitchenpos.application.dto.request;

import java.time.LocalDateTime;
import java.util.List;

public class CreateOrderRequest {

    private Long orderTableId;
    private String orderStatus;
    private LocalDateTime orderedTime;
    private List<Long> orderLineItemIds;

    private CreateOrderRequest() {
    }

    public static CreateOrderRequestBuilder builder() {
        return new CreateOrderRequestBuilder();
    }

    public static final class CreateOrderRequestBuilder {
        private Long orderTableId;
        private String orderStatus;
        private LocalDateTime orderedTime;
        private List<Long> orderLineItemIds;

        private CreateOrderRequestBuilder() {
        }

        public CreateOrderRequestBuilder orderTableId(Long orderTableId) {
            this.orderTableId = orderTableId;
            return this;
        }

        public CreateOrderRequestBuilder orderStatus(String orderStatus) {
            this.orderStatus = orderStatus;
            return this;
        }

        public CreateOrderRequestBuilder orderedTime(LocalDateTime orderedTime) {
            this.orderedTime = orderedTime;
            return this;
        }

        public CreateOrderRequestBuilder orderLineItemIds(List<Long> orderLineItemIds) {
            this.orderLineItemIds = orderLineItemIds;
            return this;
        }

        public CreateOrderRequest build() {
            CreateOrderRequest createOrderRequest = new CreateOrderRequest();
            createOrderRequest.orderStatus = this.orderStatus;
            createOrderRequest.orderTableId = this.orderTableId;
            createOrderRequest.orderedTime = this.orderedTime;
            createOrderRequest.orderLineItemIds = this.orderLineItemIds;
            return createOrderRequest;
        }
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public List<Long> getOrderLineItemIds() {
        return orderLineItemIds;
    }
}
