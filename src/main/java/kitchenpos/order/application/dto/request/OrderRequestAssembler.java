package kitchenpos.order.application.dto.request;

import kitchenpos.order.application.dto.request.order.OrderLineItemRequest;
import kitchenpos.order.application.dto.request.order.OrderRequest;
import kitchenpos.order.application.dto.request.table.OrderTableRequest;
import kitchenpos.order.application.dto.request.tablegroup.OrderTableIdRequest;
import kitchenpos.order.application.dto.request.tablegroup.TableGroupRequest;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderTable;
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
