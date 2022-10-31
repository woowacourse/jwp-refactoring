package kitchenpos.application.order.dto.request;

import kitchenpos.application.order.dto.request.order.OrderLineItemRequest;
import kitchenpos.application.order.dto.request.order.OrderRequest;
import kitchenpos.application.order.dto.request.table.OrderTableRequest;
import kitchenpos.application.order.dto.request.tablegroup.OrderTableIdRequest;
import kitchenpos.application.order.dto.request.tablegroup.TableGroupRequest;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderTable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderRequestAssembler {

    public Order asOrder(final OrderRequest request) {
        return new Order(
                request.getOrderTableId(),
                asOrderLineItems(request.getOrderLineItems())
        );
    }

    private List<OrderLineItem> asOrderLineItems(final List<OrderLineItemRequest> requests) {
        return requests.stream()
                .map(this::asOrderLineItem)
                .collect(Collectors.toUnmodifiableList());
    }

    private OrderLineItem asOrderLineItem(final OrderLineItemRequest request) {
        return new OrderLineItem(
                request.getMenuId(),
                request.getQuantity()
        );
    }

    public List<Long> asOrderTableIds(final TableGroupRequest request) {
        return request.getOrderTables()
                .stream()
                .map(OrderTableIdRequest::getId)
                .collect(Collectors.toUnmodifiableList());
    }

    public OrderTable asOrderTable(final OrderTableRequest request) {
        return new OrderTable(
                request.getNumberOfGuests(),
                request.isEmpty()
        );
    }
}
