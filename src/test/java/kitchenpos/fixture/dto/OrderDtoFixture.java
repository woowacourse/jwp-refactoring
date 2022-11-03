package kitchenpos.fixture.dto;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.order.dto.request.OrderLineItemRequest;
import kitchenpos.order.dto.request.OrderRequest;
import kitchenpos.order.dto.request.OrderStatusRequest;
import kitchenpos.menu.dto.response.MenuResponse;

public class OrderDtoFixture {

    public static OrderRequest createOrderRequest(final OrderTable orderTable, final Menu... menus) {
        final List<OrderLineItemRequest> orderLineItemRequests = Arrays.stream(menus)
                .map(it -> new OrderLineItemRequest(it.getId(), 1L))
                .collect(Collectors.toList());

        return new OrderRequest(orderTable.getId(), orderLineItemRequests);
    }

    public static OrderRequest createOrderRequest(final OrderTable orderTable, final MenuResponse... menus) {
        final List<OrderLineItemRequest> orderLineItemRequests = Arrays.stream(menus)
                .map(it -> new OrderLineItemRequest(it.getId(), 1L))
                .collect(Collectors.toList());

        return new OrderRequest(orderTable.getId(), orderLineItemRequests);
    }

    public static OrderStatusRequest forUpdateStatus(final OrderStatus orderStatus) {
        return new OrderStatusRequest(orderStatus);
    }
}
