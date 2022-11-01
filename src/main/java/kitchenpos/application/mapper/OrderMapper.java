package kitchenpos.application.mapper;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderTable;
import kitchenpos.dto.request.OrderLineItemRequest;
import kitchenpos.dto.request.OrderRequest;
import kitchenpos.repository.OrderTableRepository;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {

    private final OrderTableRepository orderTableRepository;

    public OrderMapper(final OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    public Order from(final OrderRequest orderRequest) {
        OrderTable orderTable = getOrderTable(orderRequest.getOrderTableId());
        List<OrderLineItem> orderLineItems = mapToOrderLineItems(orderRequest.getOrderLineItems());

        return new Order(orderTable, orderLineItems);
    }

    private OrderTable getOrderTable(final Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
    }

    private List<OrderLineItem> mapToOrderLineItems(final List<OrderLineItemRequest> orderLineItemRequests) {
        return orderLineItemRequests.stream()
                .map(OrderLineItemRequest::toEntity)
                .collect(Collectors.toList());
    }
}
