package kitchenpos.fixture.dto;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Menu;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.request.OrderLineItemRequest;
import kitchenpos.dto.request.OrderRequest;
import kitchenpos.dto.request.OrderStatusRequest;
import kitchenpos.dto.response.MenuResponse;

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
