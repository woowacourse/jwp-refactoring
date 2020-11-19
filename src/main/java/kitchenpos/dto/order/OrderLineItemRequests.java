package kitchenpos.dto.order;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class OrderLineItemRequests {
    List<OrderLineItemRequest> orderLineItemRequests;

    public OrderLineItemRequests(List<OrderLineItemRequest> orderLineItemRequests) {
        validate(orderLineItemRequests);
        this.orderLineItemRequests = orderLineItemRequests;
    }

    private void validate(List<OrderLineItemRequest> orderLineItemRequests) {
        if (Objects.isNull(orderLineItemRequests) || orderLineItemRequests.isEmpty()) {
            throw new IllegalArgumentException("잘못된 주문 메뉴 요청입니다.");
        }
    }

    public List<Long> getMenuIds() {
        return orderLineItemRequests
                .stream()
                .map(OrderLineItemRequest::getMenuId)
                .collect(Collectors.toList());
    }

    public Map<Long, Long> getMenuQuantityMatcher() {
        return orderLineItemRequests.stream()
                .collect(Collectors.toMap(OrderLineItemRequest::getMenuId, OrderLineItemRequest::getQuantity));
    }
}
